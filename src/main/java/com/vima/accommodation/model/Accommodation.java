package com.vima.accommodation.model;

import com.vima.accommodation.model.vo.DateRange;
import com.vima.gateway.PaymentType;

import java.util.List;
import java.util.UUID;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.hibernate.annotations.Type;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
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
	@Type(type = "org.hibernate.type.UUIDCharType")
	@Column(nullable = false, updatable = false, unique = true, columnDefinition = "char(36)")
	UUID id;

	@Column(nullable = false)
	String name;

	@Column(nullable = false)
	String hostId;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "address_id", referencedColumnName = "id")
	Address address;

	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "accommodation_benefits",
	joinColumns = @JoinColumn(name = "accommodation_id"),
	inverseJoinColumns = @JoinColumn(name = "benefit_id"))
	List<AdditionalBenefit> benefits;

	@ElementCollection
	@LazyCollection(LazyCollectionOption.FALSE)
	@CollectionTable(name = "images", joinColumns = @JoinColumn(name = "accommodation_id"))
	@Column(name = "image")
	List<String> images;

	@Column
	int minGuests;

	@Column
	int maxGuests;

	@Column(nullable = false)
	double regularPrice;

	@Embedded
	DateRange availableInPeriod;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	PaymentType paymentType;

	@Column
	boolean automaticAcceptance;

	@Override
	public boolean equals(Object obj) {
		try {
			Accommodation accommodation  = (Accommodation) obj;
			return id.equals(accommodation.id);
		}
		catch (Exception e)
		{
			return false;
		}
	}
}