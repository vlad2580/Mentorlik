package com.mentorlik.mentorlik_backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;

@Slf4j
@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        log.info("Configuring security filter chain");
        
        http
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            .csrf(csrf -> csrf.disable())
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(authorize -> authorize
                // Permit public endpoints
                .requestMatchers("/api/mentors/**").permitAll()
                .requestMatchers("/api/auth/**").permitAll()
                .requestMatchers("/auth/**").permitAll()
                .requestMatchers("/api/students/create-student").permitAll()
                .requestMatchers("/api/students/create-student/verify").permitAll()
                .requestMatchers("/api/students/verify").permitAll()
                .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()
                .requestMatchers("/actuator/health").permitAll()
                // Require authentication for all other requests
                .anyRequest().authenticated()
            );
        
        log.info("Security filter chain configured successfully");
        return http.build();
    }
    
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        final CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:4200", "http://localhost", "http://localhost:80"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowCredentials(true);
        configuration.setAllowedHeaders(Arrays.asList(
            "Authorization", "Cache-Control", "Content-Type", "Accept", "X-Requested-With", 
            "Access-Control-Request-Method", "Access-Control-Request-Headers", "Origin"
        ));
        configuration.setExposedHeaders(Arrays.asList("Authorization"));
        configuration.setMaxAge(3600L);
        
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        
        log.info("CORS configuration set up with allowed origins: {}", configuration.getAllowedOrigins());
        return source;
    }
} 