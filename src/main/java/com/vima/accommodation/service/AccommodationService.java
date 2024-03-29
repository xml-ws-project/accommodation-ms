package com.vima.accommodation.service;

import com.vima.accommodation.dto.FilterAccommodationRequest;
import com.vima.gateway.*;
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
	SearchList searchAccommodations(SearchRequest request);
	void deleteAllByHostId(String hostId);
	List<Accommodation> findRecommended(List<String> ids);
	SearchList filterAccommodation(AccommodationFilterRequest request);
}
