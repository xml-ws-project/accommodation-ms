package com.vima.accommodation.mapper;

import com.vima.accommodation.Converter;
import com.vima.accommodation.model.Accommodation;
import com.vima.accommodation.model.SpecialInfo;
import com.vima.accommodation.model.vo.DateRange;
import com.vima.gateway.SpecialInfoRequest;
import com.vima.gateway.SpecialInfoResponse;
import com.vima.gateway.Uuid;

import java.time.LocalDate;
import java.util.UUID;

import org.springframework.stereotype.Component;

@Component
public class SpecialInfoMapper {

	public static SpecialInfo convertDtoToEntity(SpecialInfoRequest request, Accommodation accommodation) {

		LocalDate start = Converter.convertGoogleTimeStampToLocalDate(request.getSpecialPeriod().getStart());
		LocalDate end = Converter.convertGoogleTimeStampToLocalDate(request.getSpecialPeriod().getEnd());
		DateRange dateRange = new DateRange(start, end);

		return SpecialInfo.builder()
			.id(UUID.randomUUID())
			.accommodation(accommodation)
			.specialPeriod(dateRange)
			.specialPrice(request.getSpecialPrice())
			.periodType(request.getPeriodType())
			.build();
	}

	public static SpecialInfoResponse convertEntityToDto(SpecialInfo specialInfo) {
		return SpecialInfoResponse.newBuilder()
			.setId(Uuid.newBuilder().setValue(specialInfo.getId().toString()))
			.setAccommodationId(Uuid.newBuilder().setValue(specialInfo.getAccommodation().getId().toString()))
			.setSpecialPrice(specialInfo.getSpecialPrice())
			.setPeriodType(specialInfo.getPeriodType())
			.build();
	}
}
