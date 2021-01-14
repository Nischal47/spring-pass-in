package com.example.base;

import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

public abstract class BaseCorsConfig implements WebMvcConfigurer {

    private static final String CLIENT_URL = "http://localhost:4200";

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping(BaseResource.BASE_URL + "/**")
                .allowedOrigins(CLIENT_URL)
                .allowedMethods("POST", "GET",  "PUT", "OPTIONS", "DELETE")
                .allowCredentials(false)
                .maxAge(4800);
    }
}
