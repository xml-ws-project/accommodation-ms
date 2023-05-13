package com.vima.accommodation.service;

import com.vima.accommodation.model.AdditionalBenefit;
import com.vima.gateway.AdditionalBenefitRequest;

public interface AdditionalBenefitService {
	AdditionalBenefit addBenefit(AdditionalBenefitRequest benefit);
}
