package com.example.SunriseSunset.controller;

import com.example.SunriseSunset.dto.LocationDto;
import com.example.SunriseSunset.model.LocationEntity;
import com.example.SunriseSunset.service.LocationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**REST controller for managing locations.*/

@RestController
@RequestMapping("/locations")
@Tag(name = "Location API", description = "Endpoints for managing locations")
public class LocationController {

    private final LocationService locationService;

    /**Constructs a LocationController with the specified LocationService.*/
    public LocationController(LocationService locationService) {
        this.locationService = locationService;
    }

    /**Creates a new location.*/

    @PostMapping
    @Operation(summary = "Create a new location", description = "Adds a new location to the system")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Location created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    public ResponseEntity<LocationEntity> createLocation(@Valid @RequestBody LocationDto dto) {
        LocationEntity savedLocation = locationService.createLocation(dto);
        return savedLocation != null ? ResponseEntity.ok(savedLocation) : ResponseEntity.badRequest().build();
    }
}