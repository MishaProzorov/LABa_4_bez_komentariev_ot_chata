package com.example.SunriseSunset.service;

import com.example.SunriseSunset.cache.Cache;
import com.example.SunriseSunset.dto.LocationDto;
import com.example.SunriseSunset.model.LocationEntity;
import com.example.SunriseSunset.repository.LocationRepository;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**Service class for managing locations.*/
@Service
public class LocationService {

    private static final Logger logger = LoggerFactory.getLogger(LocationService.class);

    private final LocationRepository locationRepository;
    private final Cache sunriseSunsetCache;

    /**Constructs a LocationService with the specified dependencies.*/
    @Autowired
    public LocationService(LocationRepository locationRepository, Cache sunriseSunsetCache) {
        this.locationRepository = locationRepository;
        this.sunriseSunsetCache = sunriseSunsetCache;
    }

    /**Retrieves a location by its ID, using the cache if available.*/
    public LocationEntity getLocationById(Integer id) {
        String cacheKey = "location_" + id;
        if (sunriseSunsetCache.containsKey(cacheKey)) {
            logger.info("Returning location from cache for key: {}", cacheKey);
            @SuppressWarnings("unchecked")
            List<LocationEntity> cachedLocations = (List<LocationEntity>) sunriseSunsetCache.get(cacheKey);
            return cachedLocations.get(0);
        }

        Optional<LocationEntity> entity = locationRepository.findById(id);
        if (entity.isPresent()) {
            LocationEntity location = entity.get();
            sunriseSunsetCache.put(cacheKey, Collections.singletonList(location));
            logger.info("Saved location to cache for key: {}", cacheKey);
            return location;
        }
        return null;
    }

    /**Creates a new location and saves it to the repository.*/
    public LocationEntity createLocation(LocationDto dto) {
        LocationEntity entity = new LocationEntity();
        entity.setName(dto.getName());
        entity.setCountry(dto.getCountry());
        entity.setSunriseSunsets(Collections.emptyList());

        LocationEntity savedEntity = locationRepository.save(entity);
        String cacheKey = "location_" + savedEntity.getId();
        sunriseSunsetCache.put(cacheKey, Collections.singletonList(savedEntity));
        logger.info("Saved new location to cache for key: {}", cacheKey);
        return savedEntity;
    }
}