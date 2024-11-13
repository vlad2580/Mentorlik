package com.mentorlik.mentorlik_backend.controller;

import com.mentorlik.mentorlik_backend.dto.auth.AuthRequestDto;
import com.mentorlik.mentorlik_backend.dto.profile.UserDto;
import com.mentorlik.mentorlik_backend.service.auth.factory.AuthServiceFactory;
import com.mentorlik.mentorlik_backend.service.auth.service.BaseAuthService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;

/**
 * Controller for managing authentication and registration endpoints.
 * <p>
 * This controller provides endpoints for logging in and registering users
 * across different user types (e.g., Admin, Mentor, Student), including OAuth2 authentication via Google and LinkedIn.
 * </p>
 */
@Slf4j
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthServiceFactory authServiceFactory;

    /**
     * Constructs an instance of {@code AuthController} with the specified {@code AuthServiceFactory}.
     *
     * @param authServiceFactory the factory used to obtain the appropriate authentication service
     *                           based on the user type.
     */
    public AuthController(AuthServiceFactory authServiceFactory) {
        this.authServiceFactory = authServiceFactory;
    }

    /**
     * Endpoint for traditional user login based on user type.
     *
     * @param userType    the type of the user (e.g., "admin", "mentor", "student")
     * @param authRequest the authentication request DTO containing email and password
     * @return a {@code ResponseEntity} with the login response, either the user data upon success or an error message
     */
    @PostMapping("/login/{userType}")
    public ResponseEntity<?> login(@PathVariable String userType, @Valid @RequestBody AuthRequestDto authRequest) {
        log.info("Login attempt for {} with email: {}", userType, authRequest.getEmail());

        try {
            UserDto user = authServiceFactory.getAuthService(userType).login(authRequest);
            log.info("Login successful for {} with email: {}", userType, authRequest.getEmail());
            return ResponseEntity.ok(user);
        } catch (Exception ex) {
            log.error("Login failed for {} with email {}: {}", userType, authRequest.getEmail(), ex.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Authentication failed");
        }
    }

    /**
     * Endpoint for OAuth2 login with Google.
     *
     * @param oauthToken the OAuth2 authentication token provided by Google
     * @return a {@code ResponseEntity} with the login response, either the user data upon success or an error message
     */
    @GetMapping("/oauth2/google")
    public ResponseEntity<?> loginWithGoogle(OAuth2AuthenticationToken oauthToken) {
        log.info("OAuth2 login attempt with Google for email: {}", Optional.ofNullable(oauthToken.getPrincipal().getAttribute("email")));

        try {
            UserDto user = authServiceFactory.getAuthService("google").loginWithOAuth(oauthToken);
            log.info("OAuth2 login successful with Google for email: {}", Optional.ofNullable(oauthToken.getPrincipal().getAttribute("email")));
            return ResponseEntity.ok(user);
        } catch (Exception ex) {
            log.error("OAuth2 login failed with Google: {}", ex.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("OAuth2 Authentication failed");
        }
    }

    /**
     * Endpoint for OAuth2 login with LinkedIn.
     *
     * @param oauthToken the OAuth2 authentication token provided by LinkedIn
     * @return a {@code ResponseEntity} with the login response, either the user data upon success or an error message
     */
    @GetMapping("/oauth2/linkedin")
    public ResponseEntity<?> loginWithLinkedIn(OAuth2AuthenticationToken oauthToken) {
        log.info("OAuth2 login attempt with LinkedIn for email: {}", Optional.ofNullable(oauthToken.getPrincipal().getAttribute("email")));

        try {
            UserDto user = authServiceFactory.getAuthService("linkedin").loginWithOAuth(oauthToken);
            log.info("OAuth2 login successful with LinkedIn for email: {}", Optional.ofNullable(oauthToken.getPrincipal().getAttribute("email")));
            return ResponseEntity.ok(user);
        } catch (Exception ex) {
            log.error("OAuth2 login failed with LinkedIn: {}", ex.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("OAuth2 Authentication failed");
        }
    }

    /**
     * Endpoint for user registration based on user type.
     *
     * @param userType the type of the user (e.g., "admin", "mentor", "student")
     * @param userDto  the user data transfer object containing registration details such as email and password
     * @return a {@code ResponseEntity} with the registration response, either the created user data upon success or an error message
     */
    @PostMapping("/register/{userType}")
    public ResponseEntity<?> register(@PathVariable String userType, @Valid @RequestBody UserDto userDto) {
        log.info("Registration attempt for {} with email: {}", userType, userDto.getEmail());

        try {
            BaseAuthService<?, ? extends UserDto> authService = authServiceFactory.getAuthService(userType);
            UserDto registeredUser = authService.register(userDto);
            log.info("Registration successful for {} with email: {}", userType, userDto.getEmail());
            return ResponseEntity.status(HttpStatus.CREATED).body(registeredUser);
        } catch (IllegalArgumentException ex) {
            log.warn("Registration failed for {} with email {}: {}", userType, userDto.getEmail(), ex.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid registration data: " + ex.getMessage());
        } catch (Exception ex) {
            log.error("Unexpected error during registration for {} with email {}: {}", userType, userDto.getEmail(), ex.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred during registration");
        }
    }
}
