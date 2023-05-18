package com.vima.accommodation.mapper;

import com.vima.accommodation.model.AdditionalBenefit;
import com.vima.gateway.AdditionalBenefitResponse;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

@Component
public class AdditionalBenefitMapper {

	public static AdditionalBenefitResponse convertEntityToDto(AdditionalBenefit benefit) {
		return AdditionalBenefitResponse.newBuilder()
			.setId(benefit.getId().toString())
			.setName(benefit.getName())
			.setIcon(benefit.getIcon())
			.build();
	}

	public static List<AdditionalBenefitResponse> convertEntityToDtoList(List<AdditionalBenefit> benefits) {
		List<AdditionalBenefitResponse> responseList = new ArrayList<>();
		benefits.forEach(benefit -> {
			responseList.add(convertEntityToDto(benefit));
		});
		return responseList;
	}
}
