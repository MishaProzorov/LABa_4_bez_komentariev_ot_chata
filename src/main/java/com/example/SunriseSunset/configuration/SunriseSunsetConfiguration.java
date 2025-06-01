package com.example.SunriseSunset.configuration;

import com.example.SunriseSunset.cache.Cache;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
/**Class.*/

@Configuration
public class SunriseSunsetConfiguration {
    /**Function.*/

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
    /**Function*/

    @Bean
    public Cache sunriseSunsetCache() {
        return new Cache();
    }
}