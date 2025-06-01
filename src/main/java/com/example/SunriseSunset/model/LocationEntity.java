package com.example.SunriseSunset.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class LocationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    private String country; // Новое поле

    @ManyToMany(mappedBy = "locations")
    private List<SunriseSunsetEntity> sunriseSunsets = new ArrayList<>();

    // Геттеры и сеттеры
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public List<SunriseSunsetEntity> getSunriseSunsets() {
        return sunriseSunsets;
    }

    public void setSunriseSunsets(List<SunriseSunsetEntity> sunriseSunsets) {
        this.sunriseSunsets = sunriseSunsets;
    }
}