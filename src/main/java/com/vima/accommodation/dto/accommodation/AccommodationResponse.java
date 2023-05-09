package com.vima.accommodation.dto.accommodation;

import com.vima.accommodation.dto.AdditionalBenefitResponse;
import com.vima.accommodation.model.enums.PaymentType;

import java.util.HashSet;
import java.util.List;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class AccommodationResponse {

	String id;
	String name;
	String hostId;
	String country;
	String city;
	String street;
	String number;
	String postalCode;
	List<AdditionalBenefitResponse> benefits;
	List<String> images;
	int minGuests;
	int maxGuests;
	double regularPrice;
	PaymentType paymentType;
}
