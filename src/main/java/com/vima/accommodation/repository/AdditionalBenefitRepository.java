package com.vima.accommodation.repository;

import com.vima.accommodation.model.AdditionalBenefit;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdditionalBenefitRepository extends JpaRepository<AdditionalBenefit, UUID> {
}
