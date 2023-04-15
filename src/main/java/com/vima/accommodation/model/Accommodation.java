package com.vima.accommodation.model;

import com.vima.accommodation.model.enums.PaymentType;

import java.util.HashSet;
import java.util.List;
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
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToOne;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Entity
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@Data
@AllArgsConstructor
@Builder(toBuilder = true)
public class Accommodation {

	@Id
	@GeneratedValue(generator = "UUID")
	@GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
	@Column(nullable = false, updatable = false, unique = true)
	UUID id;

	@Column(nullable = false)
	String name;

	@Column
	String hostId;

	@OneToOne
	Address address;

	@ManyToMany
	@JoinTable(name = "accommodation_benefits",
	joinColumns = @JoinColumn(name = "accommodation_id"),
	inverseJoinColumns = @JoinColumn(name = "benefit_id"))
	List<AdditionalBenefit> benefits;

	@Column
	HashSet<String> images;

	@Column
	int minGuests;

	@Column
	int maxGuests;

	@Embedded
	DateRange availableInPeriod;

	@OneToOne
	PriceInfo priceInfo;

	@Enumerated(EnumType.STRING)
	PaymentType paymentType;

	@Column
	boolean automaticAcceptance;
}