package com.vima.accommodation.mapper;

import com.vima.accommodation.dto.SpecialInfoRequest;
import com.vima.accommodation.dto.SpecialInfoResponse;
import com.vima.accommodation.model.Accommodation;
import com.vima.accommodation.model.SpecialInfo;

import java.util.UUID;

import org.springframework.stereotype.Component;

@Component
public class SpecialInfoMapper {

	public static SpecialInfo convertDtoToEntity(SpecialInfoRequest request, Accommodation accommodation) {
		return SpecialInfo.builder()
			.id(UUID.randomUUID())
			.accommodation(accommodation)
			.specialPeriod(request.getSpecialPeriod())
			.specialPrice(request.getSpecialPrice())
			.periodType(request.getPeriodType())
			.build();
	}

	public static SpecialInfoResponse convertEntityToDto(SpecialInfo specialInfo) {
		return SpecialInfoResponse.builder()
			.id(specialInfo.getId())
			.accommodationId(specialInfo.getAccommodation().getId())
			.specialPeriod(specialInfo.getSpecialPeriod())
			.specialPrice(specialInfo.getSpecialPrice())
			.periodType(specialInfo.getPeriodType())
			.build();
	}
}
