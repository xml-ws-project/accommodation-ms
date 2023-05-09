package com.vima.accommodation.service.impl;

import com.vima.accommodation.dto.AdditionalBenefitRequest;
import com.vima.accommodation.dto.SpecialInfoRequest;
import com.vima.accommodation.dto.accommodation.AccommodationRequest;
import com.vima.accommodation.dto.accommodation.UpdateAccommodationRequest;
import com.vima.accommodation.exception.NotFoundException;
import com.vima.accommodation.exception.SpecialInfoException;
import com.vima.accommodation.mapper.AccommodationMapper;
import com.vima.accommodation.mapper.SpecialInfoMapper;
import com.vima.accommodation.model.Accommodation;
import com.vima.accommodation.model.AdditionalBenefit;
import com.vima.accommodation.model.SpecialInfo;
import com.vima.accommodation.model.vo.DateRange;
import com.vima.accommodation.repository.AccommodationRepository;
import com.vima.accommodation.repository.AdditionalBenefitRepository;
import com.vima.accommodation.repository.AddressRepository;
import com.vima.accommodation.repository.SpecialInfoRepository;
import com.vima.accommodation.service.AccommodationService;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AccommodationServiceImpl implements AccommodationService {

	private final AccommodationRepository accommodationRepository;
	private final SpecialInfoRepository specialInfoRepository;
	private final AdditionalBenefitRepository benefitRepository;
	private final AddressRepository addressRepository;

	@Override
	public Accommodation create(final AccommodationRequest request) {
		List<AdditionalBenefit> benefits = new ArrayList<>();
		request.getBenefitsIds().forEach(id -> {
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
		if (!request.getPeriod().isEmpty()) {
			accommodation.setAvailableInPeriod(request.getPeriod());
		}
		if (!request.getPrice().isNaN()) {
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
	public AdditionalBenefit addBenefit(final AdditionalBenefitRequest request) {
		var benefit = AdditionalBenefit.builder()
			.id(UUID.randomUUID())
			.name(request.getName())
			.icon(request.getIcon())
			.build();
		return benefitRepository.save(benefit);
	}

	@Override
	public SpecialInfo createSpecialPeriod(final SpecialInfoRequest request) {
		var accommodation = accommodationRepository.findById(UUID.fromString(request.getAccommodationId())).orElseThrow(NotFoundException::new);
		var specialPeriod = request.getSpecialPeriod();
		if (!isAvailableIncludeSpecialPeriod(accommodation.getAvailableInPeriod(), specialPeriod)
			|| !isSpecialPeriodOverlaps(specialPeriod, accommodation.getId())
			|| !isSpecialPeriodValid(specialPeriod)) throw new SpecialInfoException();
		var specialInfo = SpecialInfoMapper.convertDtoToEntity(request, accommodation);
		return specialInfoRepository.save(specialInfo);
	}

	private boolean isSpecialPeriodValid(DateRange specialPeriod) {
		return specialPeriod.getStart().isBefore(specialPeriod.getEnd());
	}

	private boolean isAvailableIncludeSpecialPeriod(DateRange availablePeriod, DateRange specialPeriod) {
		return availablePeriod.isIncludingPeriod(specialPeriod);
	}

	private boolean isSpecialPeriodOverlaps(DateRange specialPeriod, UUID accommodationId) {
		List<SpecialInfo> specialList = specialInfoRepository.findAllByAccommodationId(accommodationId);
		for (SpecialInfo specialInfo: specialList) {
			if (specialInfo.getSpecialPeriod().isPartlyOverlap(specialPeriod)) return false;
		}
		return true;
	}

}
