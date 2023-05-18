package com.vima.accommodation.service;

import com.vima.accommodation.model.AdditionalBenefit;
import com.vima.gateway.AdditionalBenefitRequest;

import java.util.List;

public interface AdditionalBenefitService {
	AdditionalBenefit addBenefit(AdditionalBenefitRequest benefit);
	List<AdditionalBenefit> findAll();
}
