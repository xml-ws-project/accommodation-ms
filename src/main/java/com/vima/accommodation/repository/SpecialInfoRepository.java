package com.vima.accommodation.repository;

import com.vima.accommodation.model.SpecialInfo;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SpecialInfoRepository extends JpaRepository<SpecialInfo, UUID> {
	List<SpecialInfo> findAllByAccommodationId(UUID id);
}
