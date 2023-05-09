package com.vima.accommodation.service;

import com.vima.accommodation.dto.SpecialInfoRequest;
import com.vima.accommodation.dto.accommodation.AccommodationRequest;
import com.vima.accommodation.dto.accommodation.UpdateAccommodationRequest;
import com.vima.accommodation.model.Accommodation;
import com.vima.accommodation.model.AdditionalBenefit;
import com.vima.accommodation.model.SpecialInfo;

import java.util.List;
import java.util.UUID;

public interface AccommodationService {
	Accommodation create(AccommodationRequest request);
	void update(UpdateAccommodationRequest request);
	Accommodation findById(UUID id);
	List<Accommodation> findAllByHostId(String hostId);
	List<Accommodation> findAll();
	AdditionalBenefit addBenefit(AdditionalBenefit benefit);
	SpecialInfo createSpecialPeriod(SpecialInfoRequest request);
}
