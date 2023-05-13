package com.vima.accommodation.service.impl;

import com.vima.accommodation.converter.LocalDateConverter;
import com.vima.accommodation.exception.NotFoundException;
import com.vima.accommodation.exception.SpecialInfoException;
import com.vima.accommodation.mapper.SpecialInfoMapper;
import com.vima.accommodation.model.SpecialInfo;
import com.vima.accommodation.model.vo.DateRange;
import com.vima.accommodation.repository.AccommodationRepository;
import com.vima.accommodation.repository.SpecialInfoRepository;
import com.vima.accommodation.service.SpecialInfoService;
import com.vima.gateway.SpecialInfoRequest;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SpecialInfoServiceImpl implements SpecialInfoService {

	private final SpecialInfoRepository repository;
	private final AccommodationRepository accommodationRepository;

	@Override
	public SpecialInfo createSpecialPeriod(final SpecialInfoRequest request) {
		var accommodation = accommodationRepository.findById(UUID.fromString(request.getAccommodationId())).orElseThrow(NotFoundException::new);
		LocalDate start = LocalDateConverter.convertGoogleTimeStampToLocalDate(request.getSpecialPeriod().getStart());
		LocalDate end = LocalDateConverter.convertGoogleTimeStampToLocalDate(request.getSpecialPeriod().getEnd());
		DateRange period = new DateRange(start, end);
		if (!isAvailableIncludeSpecialPeriod(accommodation.getAvailableInPeriod(), period)
			|| !isSpecialPeriodOverlaps(period, accommodation.getId())
			|| !isSpecialPeriodValid(period)) throw new SpecialInfoException();
		var specialInfo = SpecialInfoMapper.convertDtoToEntity(request, accommodation);
		return repository.save(specialInfo);
	}

	private boolean isSpecialPeriodValid(DateRange specialPeriod) {
		return specialPeriod.getStart().isBefore(specialPeriod.getEnd());
	}

	private boolean isAvailableIncludeSpecialPeriod(DateRange availablePeriod, DateRange specialPeriod) {
		return availablePeriod.isIncludingPeriod(specialPeriod);
	}

	private boolean isSpecialPeriodOverlaps(DateRange specialPeriod, UUID accommodationId) {
		List<SpecialInfo> specialList = repository.findAllByAccommodationId(accommodationId);
		for (SpecialInfo specialInfo: specialList) {
			if (specialInfo.getSpecialPeriod().isPartlyOverlap(specialPeriod)) return false;
		}
		return true;
	}
}
