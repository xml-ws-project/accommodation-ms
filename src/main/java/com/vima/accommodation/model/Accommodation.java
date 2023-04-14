package com.vima.accommodation.model;

import com.vima.accommodation.model.enums.PaymentType;
import com.vima.accommodation.model.enums.Period;

import java.util.HashSet;
import java.util.Map;
import java.util.UUID;

import org.hibernate.annotations.GenericGenerator;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapKeyColumn;
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

	@OneToOne
	Address address;

	@Column
	boolean hasWifi;

	@Column
	boolean hasAirConditioning;

	@Column
	boolean hasFreeParking;

	@Column
	boolean hasBalcony;

	@Column
	boolean hasKitchen;

	@Column
	HashSet<String> images;

	@Column
	int minGuests;

	@Column
	int maxGuests;

	@ElementCollection
	@CollectionTable(name = "pricelist",
		joinColumns = {@JoinColumn(name = "accommodation_id", referencedColumnName = "id")})
	@Enumerated(EnumType.STRING)
	@MapKeyColumn(name = "period")
	@Column(name = "price")
	Map<Period, Double> pricelist;

	@Enumerated(EnumType.STRING)
	PaymentType paymentType;
}