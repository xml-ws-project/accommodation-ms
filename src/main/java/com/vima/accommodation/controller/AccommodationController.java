package com.vima.accommodation.controller;

import com.vima.accommodation.api.AccommodationApi;
import com.vima.accommodation.dto.AdditionalBenefitRequest;
import com.vima.accommodation.dto.AdditionalBenefitResponse;
import com.vima.accommodation.dto.SpecialInfoRequest;
import com.vima.accommodation.dto.SpecialInfoResponse;
import com.vima.accommodation.dto.accommodation.AccommodationRequest;
import com.vima.accommodation.dto.accommodation.AccommodationResponse;
import com.vima.accommodation.dto.accommodation.UpdateAccommodationRequest;
import com.vima.accommodation.mapper.AccommodationMapper;
import com.vima.accommodation.mapper.SpecialInfoMapper;
import com.vima.accommodation.model.AdditionalBenefit;
import com.vima.accommodation.service.AccommodationService;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/accommodation")
@RequiredArgsConstructor
public class AccommodationController implements AccommodationApi {

	private final AccommodationService accommodationService;

	@PostMapping("/")
	@Override
	public ResponseEntity<AccommodationResponse> create(@RequestBody @Valid final AccommodationRequest request) {
		var accommodation = AccommodationMapper.convertEntityToDto(accommodationService.create(request));
		return ResponseEntity.ok(accommodation);
	}

	@Override
	@PatchMapping("/")
	public ResponseEntity<?> update(final @RequestBody @Valid UpdateAccommodationRequest request) {
		accommodationService.update(request);
		return ResponseEntity.ok().build();
	}

	@Override
	@GetMapping("/{id}")
	public ResponseEntity<AccommodationResponse> findById(@PathVariable("id") final UUID id) {
		var accommodation = AccommodationMapper.convertEntityToDto(accommodationService.findById(id));
		return ResponseEntity.ok(accommodation);
	}

	@Override
	@GetMapping("/all")
	public ResponseEntity<List<AccommodationResponse>> findAll() {
		var accommodationList = AccommodationMapper.convertEntityToDtoList(accommodationService.findAll());
		return ResponseEntity.ok(accommodationList);
	}

	@Override
	@GetMapping("/all/{id}")
	public ResponseEntity<List<AccommodationResponse>> findAllByHostId(@PathVariable("id") final String id) {
		var accommodationList = AccommodationMapper.convertEntityToDtoList(accommodationService.findAllByHostId(id));
		return ResponseEntity.ok(accommodationList);
	}

	@Override
	@PostMapping("/benefit")
	public ResponseEntity<AdditionalBenefit> addBenefit(@RequestBody @Valid final AdditionalBenefitRequest benefit) {
		return ResponseEntity.ok(accommodationService.addBenefit(benefit));
	}

	@Override
	@PostMapping("/special-period")
	public ResponseEntity<SpecialInfoResponse> createSpecialPeriod(@RequestBody @Valid final SpecialInfoRequest request) {
		var specialInfo = accommodationService.createSpecialPeriod(request);
		return ResponseEntity.ok(SpecialInfoMapper.convertEntityToDto(specialInfo));
	}

}
