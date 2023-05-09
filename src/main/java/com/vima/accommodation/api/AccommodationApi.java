package com.vima.accommodation.api;

import com.vima.accommodation.dto.AdditionalBenefitRequest;
import com.vima.accommodation.dto.AdditionalBenefitResponse;
import com.vima.accommodation.dto.SpecialInfoRequest;
import com.vima.accommodation.dto.SpecialInfoResponse;
import com.vima.accommodation.dto.accommodation.AccommodationRequest;
import com.vima.accommodation.dto.accommodation.AccommodationResponse;
import com.vima.accommodation.dto.accommodation.UpdateAccommodationRequest;
import com.vima.accommodation.model.AdditionalBenefit;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;

public interface AccommodationApi {

	@PostMapping("/")
	ResponseEntity<AccommodationResponse> create(AccommodationRequest request);

	@PatchMapping("/")
	ResponseEntity<?> update(UpdateAccommodationRequest request);

	@GetMapping("/{id}")
	ResponseEntity<AccommodationResponse> findById(UUID id);

	@GetMapping("/all")
	ResponseEntity<List<AccommodationResponse>> findAll();

	@GetMapping("/all/{id}")
	ResponseEntity<List<AccommodationResponse>> findAllByHostId(String hostId);

	@PostMapping("/benefit")
	ResponseEntity<AdditionalBenefit> addBenefit(AdditionalBenefitRequest benefit);

	@PostMapping("/special-period")
	ResponseEntity<SpecialInfoResponse> createSpecialPeriod(SpecialInfoRequest request);
}
