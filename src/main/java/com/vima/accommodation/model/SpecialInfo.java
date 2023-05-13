package com.vima.accommodation.model;

import com.vima.accommodation.model.vo.DateRange;
import com.vima.gateway.PeriodType;

import java.util.UUID;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
	@Type(type = "org.hibernate.type.UUIDCharType")
	@Column(nullable = false, updatable = false, unique = true, columnDefinition = "char(36)")
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
