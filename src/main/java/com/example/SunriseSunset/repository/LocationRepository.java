package com.example.SunriseSunset.repository;

import com.example.SunriseSunset.model.LocationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**Repository interface for LocationEntity.*/

public interface LocationRepository extends JpaRepository<LocationEntity, Integer> {
}