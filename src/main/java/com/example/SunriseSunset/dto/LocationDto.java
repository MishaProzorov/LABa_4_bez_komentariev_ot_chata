package com.example.SunriseSunset.dto;

import jakarta.validation.constraints.NotBlank;

/**Data Transfer Object for Location.*/
public class LocationDto {

    @NotBlank(message = "Name cannot be blank")
    private String name;

    @NotBlank(message = "Country cannot be blank")
    private String country;

    /**Default constructor.*/
    public LocationDto() {}

    /**Constructs a LocationDTO with the specified name and country.*/
    public LocationDto(String name, String country) {
        this.name = name;
        this.country = country;
    }

    /**Gets the name of the location.*/
    public String getName() {
        return name;
    }

    /**Sets the name of the location.*/
    public void setName(String name) {
        this.name = name;
    }

    /**Gets the country of the location.*/
    public String getCountry() {
        return country;
    }

    /**Sets the country of the location.*/
    public void setCountry(String country) {
        this.country = country;
    }
}