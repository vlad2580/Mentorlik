package com.mentorlik.mentorlik_backend.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Web application configuration for setting up CORS and other web interaction parameters.
 * <p>
 * This configuration ensures proper functioning of cross-domain requests
 * between the Angular frontend and Spring Boot backend.
 * </p>
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    /**
     * Configures Cross-Origin Resource Sharing (CORS) rules for the application.
     * <p>
     * This method allows requests from specified sources and configures
     * allowed methods, headers, and the configuration time-to-live.
     * </p>
     *
     * @param registry CORS configuration registry
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")
                .allowedOrigins("http://localhost:4200", "https://mentorlik.com") // Angular application
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true)
                .maxAge(3600); // 1 hour
    }
} 