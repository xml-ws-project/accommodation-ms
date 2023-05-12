package com.vima.accommodation.model;

import java.util.UUID;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
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
public class Address {

	@Id
	@GeneratedValue(generator = "UUID")
	@GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
	@Type(type = "org.hibernate.type.UUIDCharType")
	@Column(nullable = false, updatable = false, unique = true, columnDefinition = "char(36)")
	UUID id;

	@Column(nullable = false)
	String country;

	@Column(nullable = false)
	String city;

	@Column(nullable = false)
	String street;

	@Column(nullable = false)
	String number;

	@Column(nullable = false)
	String postalCode;

	Double longitude;

	Double latitude;
}
