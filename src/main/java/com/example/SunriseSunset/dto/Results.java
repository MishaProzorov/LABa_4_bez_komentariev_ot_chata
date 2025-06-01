package com.example.SunriseSunset.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

/**Class of results.*/
public class Results {
    private String sunrise;
    private String sunset;

    @JsonProperty("sunrise")
    public String getSunrise() {
        return sunrise;
    }

    @JsonProperty("sunset")
    public String getSunset() {
        return sunset;
    }
}

