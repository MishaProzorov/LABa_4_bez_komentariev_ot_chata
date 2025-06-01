package com.example.SunriseSunset.configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**Configuration class for setting up Swagger OpenAPI documentation.*/
@Configuration
public class SwaggerConfig {
    /**Creates and configures the OpenAPI bean for Swagger documentation.*/
    @Bean
    public OpenAPI customOpenApi() {
        return new OpenAPI()
                .info(new Info()
                        .title("Sunrise Sunset API")
                        .version("1.0")
                        .description("API для управления данными о восходе и закате солнца"));
    }
}