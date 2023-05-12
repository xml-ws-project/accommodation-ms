package com.vima.accommodation.service;

import com.vima.accommodation.model.Accommodation;
import com.vima.accommodation.model.AdditionalBenefit;
import com.vima.accommodation.model.SpecialInfo;
import com.vima.gateway.AccommodationRequest;
import com.vima.gateway.AdditionalBenefitRequest;
import com.vima.gateway.SpecialInfoRequest;
import com.vima.gateway.UpdateAccommodationRequest;

import java.util.List;
import java.util.UUID;

public interface AccommodationService {
	Accommodation create(AccommodationRequest request);
	void update(UpdateAccommodationRequest request);
	Accommodation findById(UUID id);
	List<Accommodation> findAllByHostId(String hostId);
	List<Accommodation> findAll();
	AdditionalBenefit addBenefit(AdditionalBenefitRequest benefit);
	SpecialInfo createSpecialPeriod(SpecialInfoRequest request);
}
