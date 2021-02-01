package com.example.passin.config;

import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

public class CorsConfig extends WebMvcConfigurationSupport {

    @Override
    protected void addCorsMappings(CorsRegistry corsRegistry) {
        corsRegistry.addMapping("/api/**")
                .allowedOrigins("*")
                .allowedMethods("OPTIONS","POST","GET","PUT","DELETE")
                .allowCredentials(false)
                .maxAge(4800);
    }
}
