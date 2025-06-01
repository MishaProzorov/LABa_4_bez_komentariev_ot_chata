package com.example.SunriseSunset.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

/**Entity class representing a sunrise and sunset record in the database.*/
@Entity
public class SunriseSunsetEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private LocalDate date;
    private double latitude;
    private double longitude;
    private OffsetDateTime sunrise;
    private OffsetDateTime sunset;

    @ManyToMany
    @JoinTable(
            name = "sunrise_sunset_location",
            joinColumns = @JoinColumn(name = "sunrise_sunset_id"),
            inverseJoinColumns = @JoinColumn(name = "location_id")
    )
    private List<LocationEntity> locations = new ArrayList<>();

    /**Returns the unique identifier of the sunrise/sunset entry.*/
    public Integer getId() {
        return id;
    }

    /**Sets the unique identifier of the sunrise/sunset entry.*/
    public void setId(Integer id) {
        this.id = id;
    }

    /**Returns the date of the sunrise/sunset entry.*/
    public LocalDate getDate() {
        return date;
    }

    /**Sets the date of the sunrise/sunset entry.*/
    public void setDate(LocalDate date) {
        this.date = date;
    }

    /**Returns the latitude coordinate.*/
    public double getLatitude() {
        return latitude;
    }

    /**Sets the latitude coordinate.*/
    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    /**Returns the longitude coordinate.*/
    public double getLongitude() {
        return longitude;
    }

    /**Sets the longitude coordinate.*/
    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    /**Returns the sunrise time.*/
    public OffsetDateTime getSunrise() {
        return sunrise;
    }

    /**Sets the sunrise time.*/
    public void setSunrise(OffsetDateTime sunrise) {
        this.sunrise = sunrise;
    }

    /**Returns the sunset time.*/
    public OffsetDateTime getSunset() {
        return sunset;
    }

    /**Sets the sunset time.*/
    public void setSunset(OffsetDateTime sunset) {
        this.sunset = sunset;
    }

    /**Returns the list of associated LocationEntity objects.*/
    public List<LocationEntity> getLocations() {
        return locations;
    }

    /**Sets the list of associated LocationEntity objects.*/
    public void setLocations(List<LocationEntity> locations) {
        this.locations = locations;
    }
}