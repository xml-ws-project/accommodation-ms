package com.vima.accommodation.model;

import com.vima.accommodation.model.enums.PeriodType;
import com.vima.accommodation.model.vo.DateRange;

import java.util.UUID;

import org.hibernate.annotations.GenericGenerator;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder(toBuilder = true)
public class SpecialInfo {

	@Id
	@GeneratedValue(generator = "UUID")
	@GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
	@Column(nullable = false, updatable = false, unique = true)
	UUID id;

	@ManyToOne
	@JoinColumn(columnDefinition = "accommodation_id", referencedColumnName = "id")
	Accommodation accommodation;

	@Embedded
	DateRange specialPeriod;

	@Column
	double specialPrice;

	@Enumerated(EnumType.STRING)
	PeriodType periodType;
}
