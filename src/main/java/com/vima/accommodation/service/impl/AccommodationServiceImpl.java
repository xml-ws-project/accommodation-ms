package com.vima.accommodation.service.impl;

import com.vima.accommodation.converter.StringToUUIDListConverter;
import com.vima.accommodation.dto.FilterAccommodationRequest;
import com.vima.gateway.*;
import com.vima.accommodation.dto.SearchPriceList;
import com.vima.gateway.SearchList;
import com.vima.gateway.SearchRequest;
import com.vima.accommodation.converter.LocalDateConverter;
import com.vima.accommodation.exception.NotFoundException;
import com.vima.accommodation.mapper.AccommodationMapper;
import com.vima.accommodation.dto.gRPCReservationObject;
import com.vima.accommodation.model.Accommodation;
import com.vima.accommodation.model.AdditionalBenefit;
import com.vima.accommodation.model.SpecialInfo;
import com.vima.accommodation.model.vo.DateRange;
import com.vima.accommodation.repository.AccommodationRepository;
import com.vima.accommodation.repository.AdditionalBenefitRepository;
import com.vima.accommodation.repository.AddressRepository;
import com.vima.accommodation.repository.SpecialInfoRepository;
import com.vima.accommodation.service.AccommodationService;

import java.time.Duration;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AccommodationServiceImpl implements AccommodationService {

	private final AccommodationRepository accommodationRepository;
	private final SpecialInfoRepository specialInfoRepository;
	private final AdditionalBenefitRepository benefitRepository;
	private final AddressRepository addressRepository;
	private final EntityManager em;
	@Value("${channel.address.reservation-ms}")
	private String channelReservationAddress;
	private static final String ADDRESS = "address";
	private static final String CITY = "city";
	private static final String COUNTRY = "country";
	private static final String MAX_GUESTS = "maxGuests";
	private static final String MIN_GUESTS = "minGuests";
	private static final String AVAILABLE_PERIOD = "availableInPeriod";
	private static final String START = "start";
	private static final String END = "end";

	@Override
	public Accommodation create(final AccommodationRequest request) {
		List<AdditionalBenefit> benefits = new ArrayList<>();
		request.getBenefitsIdsList().forEach(id -> {
			var benefit = benefitRepository.findById(UUID.fromString(id)).orElseThrow(NotFoundException::new);
			benefits.add(benefit);
		});
		var accommodation = AccommodationMapper.convertDtoToEntity(request, benefits);
		addressRepository.save(accommodation.getAddress());
		return accommodationRepository.save(accommodation);
	}

	@Override
	public void update(final UpdateAccommodationRequest request) {
		var accommodation = accommodationRepository.findById(UUID.fromString(request.getAccommodationId())).orElseThrow(NotFoundException::new);
		LocalDate start = LocalDateConverter.convertGoogleTimeStampToLocalDate(request.getPeriod().getStart());
		LocalDate end = LocalDateConverter.convertGoogleTimeStampToLocalDate(request.getPeriod().getEnd());
		DateRange period = new DateRange(start, end);
		if (!period.isEmpty()) {
			accommodation.setAvailableInPeriod(period);
		}
		if (!Double.isNaN(request.getPrice())) {
			accommodation.setRegularPrice(request.getPrice());
		}
		accommodationRepository.save(accommodation);
	}

	@Override
	public Accommodation findById(final UUID id) {
		return accommodationRepository.findById(id).orElseThrow(NotFoundException::new);
	}

	@Override
	public List<Accommodation> findAllByHostId(final String hostId) {
		return accommodationRepository.findAllByHostId(UUID.fromString(hostId));
	}

	@Override
	public List<Accommodation> findAll() {
		return accommodationRepository.findAll();
	}

	@Override
	public SearchList searchAccommodations(final SearchRequest request) {
		List<SearchResponse> searchResponseList = new ArrayList<>();
		DateRange period = DateRange.builder()
			.start(LocalDateConverter.convertGoogleTimeStampToLocalDate(request.getPeriod().getStart()))
			.end(LocalDateConverter.convertGoogleTimeStampToLocalDate(request.getPeriod().getEnd()))
			.build();
		List<Accommodation> searchResult = search(request);
		List<Accommodation> busyAccommodations = callReservation(request);
		searchResult.removeAll(busyAccommodations);
		for (Accommodation accommodation: searchResult) {
			SearchPriceList priceList = calculatePriceList(accommodation, period);
			searchResponseList.add(AccommodationMapper.convertToSearchResponse(accommodation, priceList));
		}
		return SearchList.newBuilder()
			.addAllResponse(searchResponseList)
			.build();
	}

	@Override
	public void deleteAllByHostId(final String hostId) {
		accommodationRepository.deleteAllByHostId(hostId);
	}




	private List<Accommodation> callReservation(final SearchRequest request) {
		SearchReservationRequest reservationRequest = SearchReservationRequest.newBuilder()
			.setCountry(request.getCountry())
			.setCity(request.getCity())
			.setGuests(request.getGuests())
			.setPeriod(request.getPeriod())
			.build();
		SearchReservationResponse reservationResponse = initGRPC().getStub().searchReservation(reservationRequest);
		List<UUID> accommodationIds = StringToUUIDListConverter.convert(reservationResponse.getIdsList());
		return accommodationRepository.findAllById(accommodationIds);
	}

	private gRPCReservationObject initGRPC() {
		ManagedChannel channel = ManagedChannelBuilder.forAddress(channelReservationAddress, 9094)
			.usePlaintext()
			.build();
		return gRPCReservationObject.builder()
			.channel(channel)
			.stub(ReservationServiceGrpc.newBlockingStub(channel))
			.build();
	}

	private List<Accommodation> search(SearchRequest request) {
		final CriteriaBuilder cb = em.getCriteriaBuilder();
		final CriteriaQuery<Accommodation> criteriaQuery = cb.createQuery(Accommodation.class);
		Root<Accommodation> accommodationRoot = criteriaQuery.from(Accommodation.class);
		List<Predicate> predicates = new ArrayList<>();

		if (!request.getCountry().isEmpty()) {
			predicates.add(cb.like(accommodationRoot.get(ADDRESS).get(COUNTRY), request.getCountry() + "%"));
		}
		if (!request.getCity().isEmpty()) {
			predicates.add(cb.like(accommodationRoot.get(ADDRESS).get(CITY), request.getCity() + "%"));
		}
		predicates.add(cb.greaterThanOrEqualTo(accommodationRoot.get(MAX_GUESTS), request.getGuests()));
		predicates.add(cb.lessThanOrEqualTo(accommodationRoot.get(MIN_GUESTS), request.getGuests()));

		if (request.getPeriod().getStart().getSeconds() != 0 || request.getPeriod().getStart().getNanos() != 0) {
			LocalDate startDate = LocalDateConverter.convertGoogleTimeStampToLocalDate(request.getPeriod().getStart());
			predicates.add(cb.lessThanOrEqualTo(accommodationRoot.get(AVAILABLE_PERIOD).get(START), startDate));
		}
		if (request.getPeriod().getEnd().getSeconds() != 0 || request.getPeriod().getEnd().getNanos() != 0) {
			LocalDate endDate = LocalDateConverter.convertGoogleTimeStampToLocalDate(request.getPeriod().getEnd());
			predicates.add(cb.greaterThanOrEqualTo(accommodationRoot.get(AVAILABLE_PERIOD).get(END), endDate));
		}

		criteriaQuery.where(cb.and(predicates.toArray(new Predicate[0])));

		final TypedQuery<Accommodation> query = em.createQuery(criteriaQuery);
		query.setFirstResult(request.getPageSize() * request.getPageNumber());
		query.setMaxResults(request.getPageSize());

		return query.getResultList();
	}

	private SearchPriceList calculatePriceList(Accommodation accommodation, DateRange period) {
		List<SpecialInfo> specials = specialInfoRepository.findAllByAccommodationId(accommodation.getId());
		SearchPriceList searchPriceList = new SearchPriceList(0, 0);
		boolean specialIsSet = false;

		for (LocalDate currDate = period.getStart(); currDate.isBefore(period.getEnd()); currDate = currDate.plusDays(1)) {
			for (SpecialInfo special : specials) {
				if (special.getSpecialPeriod().isIncludingDate(currDate)) {
					searchPriceList.setTotalPrice(searchPriceList.getTotalPrice() + special.getSpecialPrice());
					specialIsSet = true;
					break;
				}
			}
			if (!specialIsSet) {
				searchPriceList.setTotalPrice(searchPriceList.getTotalPrice() + accommodation.getRegularPrice());
			}
			specialIsSet = false;
		}

		long numberOfDays = Duration.between(period.getStart().atStartOfDay(), period.getEnd().atStartOfDay()).toDays();
		searchPriceList.setUnitPrice(Math.round(searchPriceList.getTotalPrice() / numberOfDays * 100)/100.0);
		return searchPriceList;
	}

	@Override
	public List<Accommodation> findRecommended(List<String> ids) {
		var accommodations = accommodationRepository.findAll();
		return accommodations.stream()
				.filter(accommodation -> ids.contains(accommodation.getId().toString()))
				.collect(Collectors.toList());
	}

	public SearchList filterAccommodation(AccommodationFilterRequest request){

		List<Accommodation> viableAccommodations = getViableAccommodations(request);
		List<Accommodation> accommodations = FilterForBenefits(viableAccommodations,request.getBenefitsList());
		List<Accommodation> uniqueAccommodations = filterUnique(accommodations);
		List<SearchResponse> searchResponseList = new ArrayList<>();
		for (Accommodation accommodation: uniqueAccommodations) {
			searchResponseList.add(AccommodationMapper.convertToFilterResponse(accommodation));
		}
		return SearchList.newBuilder()
				.addAllResponse(searchResponseList)
				.build();
	}

	private  List<Accommodation> FilterForBenefits(List<Accommodation> accommodations, List<String> benefits){
		List<Accommodation> filteredAccommodations = new ArrayList<>();
		for(Accommodation a: accommodations){
			if(CheckIfAllBenefitsExist(a,benefits)) {
				filteredAccommodations.add(a);
			}
		}
		return filteredAccommodations;
	}

	private boolean CheckIfAllBenefitsExist(Accommodation accommodation, List<String> benefits){
		for(String s:benefits){
			if(!DoesBenefitExist(accommodation.getBenefits(),s)){
				return false;
			}
		}
		return true;
	}
	private boolean DoesBenefitExist(List<AdditionalBenefit> addBenefits, String benefit){
		for(AdditionalBenefit ab: addBenefits){
			if(ab.getName().equalsIgnoreCase(benefit)){
				return true;
			}
		}
		return false;
	}

	private List<Accommodation> getViableAccommodations(AccommodationFilterRequest request){
		if(request.getHostId().equals("") && !request.getBenefitsList().isEmpty()){
			return accommodationRepository.findAllByRegularPriceGreaterThanAndRegularPriceLessThanAndBenefitsNameIn(
					request.getMinPrice(), request.getMaxPrice() ,request.getBenefitsList());

		}
		if(!request.getHostId().equals("") && !request.getBenefitsList().isEmpty()) {
			return accommodationRepository.findAllByRegularPriceGreaterThanAndRegularPriceLessThanAndBenefitsNameInAndHostId(
					request.getMinPrice(), request.getMaxPrice(), request.getBenefitsList(), request.getHostId());
		}
		if(request.getHostId().equals("") && request.getBenefitsList().isEmpty()){
			return accommodationRepository.findAllByRegularPriceGreaterThanAndRegularPriceLessThan(request.getMinPrice(), request.getMaxPrice());
		}
		else return accommodationRepository.findAllByRegularPriceGreaterThanAndRegularPriceLessThanAndHostId(
				request.getMinPrice(), request.getMaxPrice(), request.getHostId());
	}

	private List<Accommodation> filterUnique(List<Accommodation> accommodations){
		List<Accommodation> uniqueAccommodations = new ArrayList<>();
		for(Accommodation a:accommodations){
				if(isAccommodationUnique(a,uniqueAccommodations)){
					uniqueAccommodations.add(a);
				}
		}
		return uniqueAccommodations;
	}

	private boolean isAccommodationUnique(Accommodation a, List<Accommodation> accommodations){
		for(Accommodation acc: accommodations){
			if(acc.getId() == a.getId()){
				return false;
			}
		}
		return true;
	}
}
