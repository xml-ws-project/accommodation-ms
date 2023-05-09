package com.vima.accommodation.mapper;

import com.vima.accommodation.dto.accommodation.AccommodationRequest;
import com.vima.accommodation.dto.accommodation.AccommodationResponse;
import com.vima.accommodation.dto.AdditionalBenefitResponse;
import com.vima.accommodation.model.Accommodation;
import com.vima.accommodation.model.AdditionalBenefit;
import com.vima.accommodation.model.Address;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Component;

@Component
public class AccommodationMapper {

	public static Accommodation convertDtoToEntity(AccommodationRequest request, List<AdditionalBenefit> benefits) {
		var address = Address.builder()
			.country(request.getCountry())
			.city(request.getCity())
			.street(request.getStreet())
			.number(request.getNumber())
			.postalCode(request.getPostalCode())
			.build();

		return Accommodation.builder()
			.id(UUID.randomUUID())
			.name(request.getName())
			.hostId(request.getHostId())
			.address(address)
			.images(request.getImages())
			.minGuests(request.getMinGuests())
			.maxGuests(request.getMaxGuests())
			.paymentType(request.getPaymentType())
			.automaticAcceptance(request.isAutomaticAcceptance())
			.regularPrice(request.getRegularPrice())
			.benefits(benefits)
			.availableInPeriod(request.getAvailablePeriod())
			.build();
	}

	public static AccommodationResponse convertEntityToDto(Accommodation accommodation) {

		List<AdditionalBenefitResponse> benefits = new ArrayList<>();
		accommodation.getBenefits().forEach(benefit -> {
			AdditionalBenefitResponse benefitResp = AdditionalBenefitResponse.builder()
				.id(benefit.getId().toString())
				.name(benefit.getName())
				.icon(benefit.getIcon())
				.build();
			benefits.add(benefitResp);
		});

		return AccommodationResponse.builder()
			.id(accommodation.getId().toString())
			.name(accommodation.getName())
			.hostId(accommodation.getHostId())
			.country(accommodation.getAddress().getCountry())
			.city(accommodation.getAddress().getCity())
			.street(accommodation.getAddress().getStreet())
			.number(accommodation.getAddress().getNumber())
			.postalCode(accommodation.getAddress().getPostalCode())
			.benefits(benefits)
			.regularPrice(accommodation.getRegularPrice())
			.maxGuests(accommodation.getMaxGuests())
			.minGuests(accommodation.getMinGuests())
			.paymentType(accommodation.getPaymentType())
			.images(accommodation.getImages())
			.build();
	}

	public static List<AccommodationResponse> convertEntityToDtoList(List<Accommodation> accommodationList) {
		List<AccommodationResponse> responseList = new ArrayList<>();
		accommodationList.forEach(accommodation -> {
			var accommodationResp = convertEntityToDto(accommodation);
			responseList.add(accommodationResp);
		});
		return responseList;
	}


}
