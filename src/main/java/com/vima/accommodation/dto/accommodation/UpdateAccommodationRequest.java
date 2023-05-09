package com.vima.accommodation.dto.accommodation;

import com.vima.accommodation.model.vo.DateRange;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
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
public class UpdateAccommodationRequest {

	@NotBlank
	String accommodationId;

	DateRange period;

	Double price;
}
