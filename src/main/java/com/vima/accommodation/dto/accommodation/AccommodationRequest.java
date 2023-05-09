package com.vima.accommodation.dto.accommodation;

import com.vima.accommodation.model.enums.PaymentType;
import com.vima.accommodation.model.vo.DateRange;

import java.util.HashSet;
import java.util.List;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class AccommodationRequest {

	@NotBlank
	String name;
	@NotBlank
	String hostId;
	@NotBlank
	String country;
	@NotBlank
	String city;
	@NotBlank
	String street;
	@NotBlank
	String number;
	@NotBlank
	String postalCode;
	List<String> images;
	@NotNull
	int minGuests;
	@NotNull
	int maxGuests;
	@NotNull
	PaymentType paymentType;
	@NotNull
	boolean automaticAcceptance;
	@NotNull
	double regularPrice;
	@NotNull
	List<String> benefitsIds;
	@NotNull
	DateRange availablePeriod;
}
