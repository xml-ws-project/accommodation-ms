package com.vima.accommodation.dto;

import com.vima.accommodation.model.enums.PeriodType;
import com.vima.accommodation.model.vo.DateRange;

import java.util.UUID;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class SpecialInfoResponse {

	UUID id;
	UUID accommodationId;
	DateRange specialPeriod;
	double specialPrice;
	PeriodType periodType;
}
