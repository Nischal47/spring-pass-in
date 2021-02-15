package com.example.passin.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

@Configuration
public class CorsConfig extends WebMvcConfigurationSupport {

    @Override
    protected void addCorsMappings(CorsRegistry corsRegistry) {
        corsRegistry.addMapping("/api/**")
                .allowedOrigins("*")
                .allowedMethods("OPTIONS","POST","GET","PUT","DELETE")
                .allowCredentials(false)
                .maxAge(4800);
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry){
        registry.addResourceHandler("/**")
                .addResourceLocations("file:static/");
    }

}
