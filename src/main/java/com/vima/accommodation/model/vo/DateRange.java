package com.vima.accommodation.model.vo;

import java.time.LocalDate;

import jakarta.persistence.Embeddable;

@Embeddable
public class DateRange {

	LocalDate start;
	LocalDate end;
}
