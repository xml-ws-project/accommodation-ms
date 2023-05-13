package com.vima.accommodation.service.impl;

import com.vima.accommodation.model.AdditionalBenefit;
import com.vima.accommodation.repository.AdditionalBenefitRepository;
import com.vima.accommodation.service.AdditionalBenefitService;
import com.vima.gateway.AdditionalBenefitRequest;

import java.util.UUID;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdditionalBenefitServiceImpl implements AdditionalBenefitService {

	private final AdditionalBenefitRepository repository;

	@Override
	public AdditionalBenefit addBenefit(final AdditionalBenefitRequest request) {
		var benefit = AdditionalBenefit.builder()
			.id(UUID.randomUUID())
			.name(request.getName())
			.icon(request.getIcon())
			.build();
		return repository.save(benefit);
	}
}
