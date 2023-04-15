package com.vima.accommodation.model;

import java.time.LocalDate;

import jakarta.persistence.Embeddable;

@Embeddable
public class DateRange {

	LocalDate start;
	LocalDate end;
}
