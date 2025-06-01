package com.example.SunriseSunset.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.time.OffsetDateTime;
import java.util.List;

/**Data Transfer Object (DTO) representing sunrise and sunset data.*/
public class SunriseSunsetDto {
    private Integer id;
    @NotNull(message = "Date must not be null")
    private String date;
    @Min(value = -90, message = "Latitude must be greater than or equal to -90")
    @Max(value = 90, message = "Latitude must be less than or equal to 90")
    private double latitude;
    @Min(value = -180, message = "Longitude must be greater than or equal to -180")
    @Max(value = 180, message = "Longitude must be less than or equal to 180")
    private double longitude;
    private OffsetDateTime sunrise;
    private OffsetDateTime sunset;
    private List<Integer> locationIds;

    /**Default constructor for SunriseSunsetDto.*/
    public SunriseSunsetDto() {}

    /**Constructs a new SunriseSunsetDto with the specified parameters.*/
    public SunriseSunsetDto(Integer id, String date, double latitude, double longitude,
                            OffsetDateTime sunrise, OffsetDateTime sunset, List<Integer> locationIds) {
        this.id = id;
        this.date = date;
        this.latitude = latitude;
        this.longitude = longitude;
        this.sunrise = sunrise;
        this.sunset = sunset;
        this.locationIds = locationIds;
    }

    /**Returns the unique identifier of the sunrise/sunset entry.*/
    public Integer getId() { return id; }

    /**Sets the unique identifier of the sunrise/sunset entry.*/
    public void setId(Integer id) { this.id = id; }

    /**Returns the date in YYYY-MM-DD format.*/
    public String getDate() { return date; }

    /**Sets the date in YYYY-MM-DD format.*/
    public void setDate(String date) { this.date = date; }

    /**Returns the latitude coordinate.*/
    public double getLatitude() { return latitude; }

    /**Sets the latitude coordinate.*/
    public void setLatitude(double latitude) { this.latitude = latitude; }

    /**Returns the longitude coordinate.*/
    public double getLongitude() { return longitude; }

    /**Sets the longitude coordinate.*/
    public void setLongitude(double longitude) { this.longitude = longitude; }

    /**Returns the sunrise time.*/
    public OffsetDateTime getSunrise() { return sunrise; }

    /**Sets the sunrise time.*/
    public void setSunrise(OffsetDateTime sunrise) { this.sunrise = sunrise; }

    /**Returns the sunset time.*/
    public OffsetDateTime getSunset() { return sunset; }

    /**Sets the sunset time.*/
    public void setSunset(OffsetDateTime sunset) { this.sunset = sunset; }

    /**Returns the list of location IDs associated with this entry.*/
    public List<Integer> getLocationIds() { return locationIds; }

    /**Sets the list of location IDs associated with this entry.*/
    public void setLocationIds(List<Integer> locationIds) { this.locationIds = locationIds; }
}