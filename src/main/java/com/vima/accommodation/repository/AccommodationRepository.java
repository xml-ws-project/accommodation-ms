package com.vima.accommodation.repository;

import com.vima.accommodation.model.Accommodation;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccommodationRepository extends JpaRepository<Accommodation, UUID> {
}
