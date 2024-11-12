package com.mentorlik.mentorlik_backend.controller;

import com.mentorlik.mentorlik_backend.dto.AuthRequestDto;
import com.mentorlik.mentorlik_backend.dto.UserDto;
import com.mentorlik.mentorlik_backend.exception.ResourceNotFoundException;
import com.mentorlik.mentorlik_backend.service.AuthService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * Controller responsible for handling authentication and registration endpoints.
 * Provides endpoints for login and user registration.
 */
@Slf4j
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    /**
     * Constructs an instance of {@code AuthController} with a specified {@code AuthService}.
     *
     * @param authService the authentication service used to handle login and registration logic
     */
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    /**
     * Endpoint for user login.
     * Accepts an authentication request and returns a response with login information.
     *
     * @param authRequest the authentication request data transfer object containing email and password
     * @return a {@code ResponseEntity} with the login response, typically a token or success message
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody AuthRequestDto authRequest) {
        log.info("Login attempt for user with email: {}", authRequest.getEmail());

        try {
            UserDto user = authService.login(authRequest);
            log.info("Login successful for user with email: {}", authRequest.getEmail());
            return ResponseEntity.ok(user);
        } catch (ResourceNotFoundException ex) {
            log.warn("Login failed for user with email {}: {}", authRequest.getEmail(), ex.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found or incorrect credentials");
        } catch (Exception ex) {
            log.error("An unexpected error occurred during login for user with email {}: {}", authRequest.getEmail(), ex.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred during login");
        }
    }

    /**
     * Endpoint for user registration.
     * Accepts a user data transfer object containing registration details and returns a response with registration status.
     *
     * @param userDto the user data transfer object containing registration details such as email and password
     * @return a {@code ResponseEntity} with the registration response, typically a success message or created user information
     */
    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody UserDto userDto) {
        log.info("Registration attempt for user with email: {}", userDto.getEmail());

        try {
            UserDto registeredUser = authService.register(userDto);
            log.info("Registration successful for user with email: {}", userDto.getEmail());
            return ResponseEntity.status(HttpStatus.CREATED).body(registeredUser);
        } catch (IllegalArgumentException ex) {
            log.warn("Registration failed for user with email {}: {}", userDto.getEmail(), ex.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid registration data: " + ex.getMessage());
        } catch (Exception ex) {
            log.error("An unexpected error occurred during registration for user with email {}: {}", userDto.getEmail(), ex.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred during registration");
        }
    }
}