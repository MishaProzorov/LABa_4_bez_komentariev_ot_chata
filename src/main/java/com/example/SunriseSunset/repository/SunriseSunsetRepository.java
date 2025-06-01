package com.example.SunriseSunset.repository;

import com.example.SunriseSunset.model.SunriseSunsetEntity;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;



/**Repository interface for SunriseSunsetEntity, providing CRUD operations and custom queries.*/
public interface SunriseSunsetRepository extends JpaRepository<SunriseSunsetEntity, Integer> {
    /**Finds all sunrise/sunset entries associated with a location name and a specific date.*/
    List<SunriseSunsetEntity> findByLocationsNameAndDate(String name, LocalDate date);

    /**Finds all sunrise/sunset entries associated with a location name within a date range.*/
    List<SunriseSunsetEntity> findByLocationsNameAndDateBetween(String name, LocalDate startDate, LocalDate endDate);
}