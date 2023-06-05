package com.vima.accommodation.repository;

import com.vima.accommodation.model.RatingAccommodation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface RatingAccommodationRepository extends JpaRepository<RatingAccommodation, Long> {

    @Query(value = "SELECT sum(a.value)/count(a.id) FROM rating a WHERE a.accommodation_id = ?1",nativeQuery = true)
    double findAvg(String accomodationId);

    @Query(value = "SELECT count(a.id) FROM rating a WHERE a.accommodation_id = ?1", nativeQuery = true)
    int findNumberOfAccommodationRatings(String accommodationId);
}
