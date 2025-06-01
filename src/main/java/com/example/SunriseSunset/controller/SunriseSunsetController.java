package com.example.SunriseSunset.controller;

import com.example.SunriseSunset.dto.SunriseSunsetDto;
import com.example.SunriseSunset.exception.ErrorResponse;
import com.example.SunriseSunset.service.LocationService;
import com.example.SunriseSunset.service.SunriseSunsetService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


/**REST controller for managing sunrise and sunset data.*/
@RestController
@RequestMapping("/sun/times")
@Tag(name = "Sunrise Sunset API", description = "Endpoints for managing sunrise and sunset data")
public class SunriseSunsetController {

    private final SunriseSunsetService sunService;
    private final LocationService locationService;

    /**Constructs a new SunriseSunsetController with the specified services.*/
    @Autowired
    public SunriseSunsetController(SunriseSunsetService sunService, LocationService locationService) {
        this.sunService = sunService;
        this.locationService = locationService;
    }

    /**Creates a new sunrise/sunset entry.*/
    @PostMapping
    @Operation(summary = "Create sunrise/sunset entry", description = "Adds a new sunrise/sunset record")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Entry created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid input data",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<SunriseSunsetDto> createSunriseSunset(@Valid @RequestBody SunriseSunsetDto dto) {
        try {
            SunriseSunsetDto savedDto = sunService.createSunriseSunset(dto);
            return ResponseEntity.ok(savedDto);
        } catch (Exception e) {
            throw new RuntimeException("Ошибка при создании записи: " + e.getMessage(), e);
        }
    }

    /**Retrieves a sunrise/sunset entry by its ID.*/
    @GetMapping("/{id}")
    @Operation(summary = "Get sunrise/sunset by ID", description = "Retrieves a sunrise/sunset record by ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Entry found"),
        @ApiResponse(responseCode = "404", description = "Entry not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<SunriseSunsetDto> getSunriseSunsetById(
            @Parameter(description = "ID of the sunrise/sunset entry") @PathVariable Integer id) {
        SunriseSunsetDto dto = sunService.getSunriseSunsetById(id);
        return dto != null ? ResponseEntity.ok(dto) : ResponseEntity.notFound().build();
    }

    /**Retrieves all sunrise/sunset entries.*/
    @GetMapping("/all")
    @Operation(summary = "Get all sunrise/sunset entries", description = "Retrieves all sunrise/sunset records")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Entries retrieved successfully"),
        @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<List<SunriseSunsetDto>> getAllSunriseSunsets() {
        List<SunriseSunsetDto> dtos = sunService.getAllSunriseSunsets();
        return ResponseEntity.ok(dtos);
    }

    /**Updates an existing sunrise/sunset entry.*/
    @PutMapping("/{id}")
    @Operation(summary = "Update sunrise/sunset entry", description = "Updates an existing sunrise/sunset record")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Entry updated successfully"),
        @ApiResponse(responseCode = "404", description = "Entry not found"),
        @ApiResponse(responseCode = "400", description = "Invalid input data",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<SunriseSunsetDto> updateSunriseSunset(
            @Parameter(description = "ID of the sunrise/sunset entry") @PathVariable Integer id,
            @Valid @RequestBody SunriseSunsetDto dto) {
        try {
            SunriseSunsetDto updatedDto = sunService.updateSunriseSunset(id, dto);
            return updatedDto != null ? ResponseEntity.ok(updatedDto) : ResponseEntity.notFound().build();
        } catch (Exception e) {
            throw new RuntimeException("Ошибка при обновлении записи: " + e.getMessage(), e);
        }
    }

    /**Deletes a sunrise/sunset entry by its ID.*/
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete sunrise/sunset entry", description = "Deletes a sunrise/sunset record by ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Entry deleted successfully"),
        @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<Void> deleteSunriseSunset(
            @Parameter(description = "ID of the sunrise/sunset entry") @PathVariable Integer id) {
        try {
            sunService.deleteSunriseSunset(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            throw new RuntimeException("Ошибка при удалении записи: " + e.getMessage(), e);
        }
    }

    /**Retrieves sunrise/sunset entries by location name and date.*/
    @GetMapping("/location/{locationName}")
    @Operation(summary = "Get sunrise/sunset by location and date", description = "Retrieves sunrise/sunset records by location name and date")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Entries retrieved successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid date format",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<List<SunriseSunsetDto>> getSunriseSunsetByLocationAndDate(
            @Parameter(description = "Name of the location") @PathVariable String locationName,
            @Parameter(description = "Date in YYYY-MM-DD format") @RequestParam String date) {
        LocalDate localDate;
        try {
            localDate = LocalDate.parse(date);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Некорректный формат даты: " + date);
        }
        List<SunriseSunsetDto> dtos = sunService.getSunriseSunsetByLocationAndDate(locationName, localDate);
        return ResponseEntity.ok(dtos);
    }

    /**Retrieves sunrise/sunset entries by location name and date range.*/
    @GetMapping("/location/{locationName}/range")
    @Operation(summary = "Get sunrise/sunset by location and date range",
            description = "Retrieves sunrise/sunset records by location name and date range")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Entries retrieved successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid date format",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<List<SunriseSunsetDto>> getSunriseSunsetByLocationAndDateRange(
            @Parameter(description = "Name of the location") @PathVariable String locationName,
            @Parameter(description = "Start date in YYYY-MM-DD format") @RequestParam String startDate,
            @Parameter(description = "End date in YYYY-MM-DD format") @RequestParam String endDate) {
        LocalDate start;
        LocalDate end;
        try {
            start = LocalDate.parse(startDate);
            end = LocalDate.parse(endDate);
            if (start.isAfter(end)) {
                throw new IllegalArgumentException("Start date must be before end date");
            }
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Некорректный формат даты: " + startDate + " или " + endDate);
        }
        List<SunriseSunsetDto> dtos = sunService.getSunriseSunsetByLocationAndDateRange(locationName, start, end);
        return ResponseEntity.ok(dtos);
    }

    /**Clears the cache of sunrise/sunset data.*/
    @DeleteMapping("/cache")
    @Operation(summary = "Clear cache", description = "Clears the cache of sunrise/sunset data")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Cache cleared successfully"),
        @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<Void> clearCache() {
        try {
            sunService.clearCache();
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            throw new RuntimeException("Ошибка при очистке кэша: " + e.getMessage(), e);
        }
    }
}