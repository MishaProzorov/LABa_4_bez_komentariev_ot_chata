package com.example.SunriseSunset.service;

import com.example.SunriseSunset.cache.Cache;
import com.example.SunriseSunset.dto.SunriseSunsetDto;
import com.example.SunriseSunset.dto.SunriseSunsetModel;
import com.example.SunriseSunset.model.LocationEntity;
import com.example.SunriseSunset.model.SunriseSunsetEntity;
import com.example.SunriseSunset.repository.LocationRepository;
import com.example.SunriseSunset.repository.SunriseSunsetRepository;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;



/**Service class for managing sunrise and sunset data.This class provides methods to create, retrieve, update, and delete sunrise/sunset entries.*/
@Service
public class SunriseSunsetService {

    private static final Logger logger = LoggerFactory.getLogger(SunriseSunsetService.class);

    private static final String SUN_API_URL = "https://api.sunrise-sunset.org/json";

    private final RestTemplate restTemplate;
    private final SunriseSunsetRepository sunriseSunsetRepository;
    private final LocationRepository locationRepository;
    private final Cache sunriseSunsetCache;

    /**Constructs a new SunriseSunsetService with the specified dependencies.*/
    @Autowired
    public SunriseSunsetService(RestTemplate restTemplate,
                                SunriseSunsetRepository sunriseSunsetRepository,
                                LocationRepository locationRepository,
                                Cache sunriseSunsetCache) {
        this.restTemplate = restTemplate;
        this.sunriseSunsetRepository = sunriseSunsetRepository;
        this.locationRepository = locationRepository;
        this.sunriseSunsetCache = sunriseSunsetCache;
    }

    /**Creates a new sunrise/sunset entry based on the provided DTO.*/
    public SunriseSunsetDto createSunriseSunset(SunriseSunsetDto dto) {
        if (dto.getLatitude() < -90 || dto.getLatitude() > 90) {
            throw new IllegalArgumentException("Широта должна быть в диапазоне от -90 до 90");
        }
        if (dto.getLongitude() < -180 || dto.getLongitude() > 180) {
            throw new IllegalArgumentException("Долгота должна быть в диапазоне от -180 до 180");
        }

        LocalDate date;
        try {
            date = LocalDate.parse(dto.getDate());
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Некорректный формат даты: " + dto.getDate());
        }

        SunriseSunsetModel sunData = getSunriseSunset(dto.getLatitude(), dto.getLongitude(), dto.getDate());
        SunriseSunsetEntity entity = new SunriseSunsetEntity();
        entity.setDate(date);
        entity.setLatitude(dto.getLatitude());
        entity.setLongitude(dto.getLongitude());
        entity.setSunrise(OffsetDateTime.parse(sunData.getResults().getSunrise()));
        entity.setSunset(OffsetDateTime.parse(sunData.getResults().getSunset()));

        if (dto.getLocationIds() != null && !dto.getLocationIds().isEmpty()) {
            List<LocationEntity> locations = locationRepository.findAllById(dto.getLocationIds());
            entity.setLocations(locations);
        }

        SunriseSunsetEntity savedEntity = sunriseSunsetRepository.save(entity);
        return convertToDto(savedEntity);
    }

    /**Retrieves a sunrise/sunset entry by its ID.*/
    public SunriseSunsetDto getSunriseSunsetById(Integer id) {
        Optional<SunriseSunsetEntity> entity = sunriseSunsetRepository.findById(id);
        return entity.map(this::convertToDto).orElse(null);
    }

    /**Retrieves all sunrise/sunset entries.*/
    public List<SunriseSunsetDto> getAllSunriseSunsets() {
        return sunriseSunsetRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    /**Updates an existing sunrise/sunset entry.*/
    public SunriseSunsetDto updateSunriseSunset(Integer id, SunriseSunsetDto dto) {
        Optional<SunriseSunsetEntity> existing = sunriseSunsetRepository.findById(id);
        if (existing.isPresent()) {
            if (dto.getLatitude() < -90 || dto.getLatitude() > 90) {
                throw new IllegalArgumentException("Широта должна быть в диапазоне от -90 до 90");
            }
            if (dto.getLongitude() < -180 || dto.getLongitude() > 180) {
                throw new IllegalArgumentException("Долгота должна быть в диапазоне от -180 до 180");
            }

            LocalDate date;
            try {
                date = LocalDate.parse(dto.getDate());
            } catch (DateTimeParseException e) {
                throw new IllegalArgumentException("Некорректный формат даты: " + dto.getDate());
            }

            SunriseSunsetEntity entity = existing.get();
            entity.setDate(date);
            entity.setLatitude(dto.getLatitude());
            entity.setLongitude(dto.getLongitude());
            SunriseSunsetModel sunData = getSunriseSunset(dto.getLatitude(), dto.getLongitude(), dto.getDate());
            entity.setSunrise(OffsetDateTime.parse(sunData.getResults().getSunrise()));
            entity.setSunset(OffsetDateTime.parse(sunData.getResults().getSunset()));

            if (dto.getLocationIds() != null && !dto.getLocationIds().isEmpty()) {
                List<LocationEntity> locations = locationRepository.findAllById(dto.getLocationIds());
                entity.setLocations(locations);
            } else {
                entity.setLocations(new java.util.ArrayList<>());
            }

            SunriseSunsetEntity updatedEntity = sunriseSunsetRepository.save(entity);
            return convertToDto(updatedEntity);
        }
        return null;
    }

    /**Deletes a sunrise/sunset entry by its ID.*/
    public void deleteSunriseSunset(Integer id) {
        sunriseSunsetRepository.deleteById(id);
    }

    /**Retrieves sunrise/sunset entries by location name and date, using cache if available.*/
    public List<SunriseSunsetDto> getSunriseSunsetByLocationAndDate(String locationName, LocalDate date) {
        String cacheKey = "sun_" + locationName + "_" + date.toString();
        if (sunriseSunsetCache.containsKey(cacheKey)) {
            logger.info("Returning data from cache for key: {}", cacheKey);
            @SuppressWarnings("unchecked")
            List<SunriseSunsetDto> cachedData = (List<SunriseSunsetDto>) sunriseSunsetCache.get(cacheKey);
            return cachedData;
        }

        List<SunriseSunsetEntity> entities = sunriseSunsetRepository.findByLocationsNameAndDate(locationName, date);
        List<SunriseSunsetDto> dtos = entities.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
        sunriseSunsetCache.put(cacheKey, dtos);
        logger.info("Saved data to cache for key: {}", cacheKey);
        return dtos;
    }

    /**Retrieves sunrise/sunset entries by location name and date range, using cache if available.*/
    public List<SunriseSunsetDto> getSunriseSunsetByLocationAndDateRange(String locationName,
                                                                         LocalDate startDate,
                                                                         LocalDate endDate) {
        String cacheKey = "sun_" + locationName + "_" + startDate.toString() + "_" + endDate.toString();
        if (sunriseSunsetCache.containsKey(cacheKey)) {
            logger.info("Returning data from cache for key: {}", cacheKey);
            @SuppressWarnings("unchecked")
            List<SunriseSunsetDto> cachedData = (List<SunriseSunsetDto>) sunriseSunsetCache.get(cacheKey);
            return cachedData;
        }

        List<SunriseSunsetEntity> entities = sunriseSunsetRepository.findByLocationsNameAndDateBetween(
                locationName, startDate, endDate);
        List<SunriseSunsetDto> dtos = entities.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
        sunriseSunsetCache.put(cacheKey, dtos);
        logger.info("Saved data to cache for key: {}", cacheKey);
        return dtos;
    }

    /**Clears the cache of sunrise/sunset data.*/
    public void clearCache() {
        sunriseSunsetCache.clear();
        logger.info("Cache cleared");
    }

    /**Converts a SunriseSunsetEntity to a SunriseSunsetDto.*/
    private SunriseSunsetDto convertToDto(SunriseSunsetEntity entity) {
        List<Integer> locationIds = entity.getLocations().stream()
                .map(LocationEntity::getId)
                .collect(Collectors.toList());
        return new SunriseSunsetDto(
                entity.getId(), entity.getDate().toString(), entity.getLatitude(), entity.getLongitude(),
                entity.getSunrise(), entity.getSunset(), locationIds
        );
    }

    /**Fetches sunrise and sunset data from an external API.*/
    private SunriseSunsetModel getSunriseSunset(double lat, double lng, String date) {
        String url = String.format("%s?lat=%f&lng=%f&date=%s&formatted=0", SUN_API_URL, lat, lng, date);
        SunriseSunsetModel response = restTemplate.getForObject(url, SunriseSunsetModel.class);
        if (response == null || response.getResults() == null) {
            throw new RuntimeException("Некорректный ответ от API sunrise-sunset");
        }
        return response;
    }
}