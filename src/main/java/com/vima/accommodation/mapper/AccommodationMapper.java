package com.vima.accommodation.mapper;

import com.vima.accommodation.converter.LocalDateConverter;
import com.vima.accommodation.model.Accommodation;
import com.vima.accommodation.model.AdditionalBenefit;
import com.vima.accommodation.model.Address;
import com.vima.accommodation.model.vo.DateRange;
import com.vima.gateway.AccommodationRequest;
import com.vima.gateway.AccommodationResponse;
import com.vima.gateway.AdditionalBenefitResponse;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Component;

@Component
public class AccommodationMapper {

	public static Accommodation convertDtoToEntity(AccommodationRequest request, List<AdditionalBenefit> benefits) {
		var address = Address.builder()
			.country(request.getCountry())
			.city(request.getCity())
			.street(request.getStreet())
			.number(request.getNumber())
			.postalCode(request.getPostalCode())
			.build();

		LocalDate start = LocalDateConverter.convertGoogleTimeStampToLocalDate(request.getAvailablePeriod().getStart());
		LocalDate end = LocalDateConverter.convertGoogleTimeStampToLocalDate(request.getAvailablePeriod().getEnd());
		DateRange dateRange = new DateRange(start, end);

		return Accommodation.builder()
			.id(UUID.randomUUID())
			.name(request.getName())
			.hostId(request.getHostId())
			.address(address)
			.images(request.getImagesList())
			.minGuests(request.getMinGuests())
			.maxGuests(request.getMaxGuests())
			.paymentType(request.getPaymentType())
			.automaticAcceptance(request.getAutomaticAcceptance())
			.regularPrice(request.getRegularPrice())
			.benefits(benefits)
			.availableInPeriod(dateRange)
			.build();
	}

	public static AccommodationResponse convertEntityToDto(Accommodation accommodation) {

		List<AdditionalBenefitResponse> benefits = new ArrayList<>();
		accommodation.getBenefits().forEach(benefit -> {
			AdditionalBenefitResponse benefitResp = AdditionalBenefitResponse.newBuilder()
				.setId(benefit.getId().toString())
				.setName(benefit.getName())
				.setIcon(benefit.getIcon())
				.build();
			benefits.add(benefitResp);
		});

		return AccommodationResponse.newBuilder()
			.setId(accommodation.getId().toString())
			.setName(accommodation.getName())
			.setHostId(accommodation.getHostId())
			.setCountry(accommodation.getAddress().getCountry())
			.setCity(accommodation.getAddress().getCity())
			.setStreet(accommodation.getAddress().getStreet())
			.setNumber(accommodation.getAddress().getNumber())
			.setPostalCode(accommodation.getAddress().getPostalCode())
			.setRegularPrice(accommodation.getRegularPrice())
			.setMaxGuests(accommodation.getMaxGuests())
			.setMinGuests(accommodation.getMinGuests())
			.addAllBenefits(benefits)
			.setPaymentType(accommodation.getPaymentType())
			.addAllImages(accommodation.getImages())
			.build();
	}

	public static List<AccommodationResponse> convertEntityToDtoList(List<Accommodation> accommodationList) {
		List<AccommodationResponse> responseList = new ArrayList<>();
		accommodationList.forEach(accommodation -> {
			var accommodationResp = convertEntityToDto(accommodation);
			responseList.add(accommodationResp);
		});
		return responseList;
	}


}
