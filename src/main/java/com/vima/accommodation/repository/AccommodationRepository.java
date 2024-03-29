package com.vima.accommodation.repository;

import com.vima.accommodation.model.Accommodation;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AccommodationRepository extends JpaRepository<Accommodation, UUID> {
	List<Accommodation> findAllByHostId(UUID hostId);
	void deleteAllByHostId(String hostId);
	List<Accommodation> findAllByRegularPriceGreaterThanAndRegularPriceLessThanAndBenefitsNameInAndHostId(
			double minPrice, double maxPrice, List<String> benefits, String hostId
	);

	List<Accommodation> findAllByRegularPriceGreaterThanAndRegularPriceLessThanAndBenefitsNameIn(
			double minPrice, double maxPrice, List<String> benefits
	);

	List<Accommodation> findAllByRegularPriceGreaterThanAndRegularPriceLessThanAndHostId(
			double minPrice, double maxPrice,  String hostId
	);

	List<Accommodation> findAllByRegularPriceGreaterThanAndRegularPriceLessThan(
			double minPrice, double maxPrice
	);

}
