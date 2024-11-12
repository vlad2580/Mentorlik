package com.mentorlik.mentorlik_backend.controller;

import com.mentorlik.mentorlik_backend.dto.UserDto;
import com.mentorlik.mentorlik_backend.exception.ResourceNotFoundException;
import com.mentorlik.mentorlik_backend.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controller responsible for handling user-related endpoints.
 * Provides an endpoint for retrieving user profile information.
 */
@Slf4j
@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    /**
     * Constructs an instance of {@code UserController} with a specified {@code UserService}.
     *
     * @param userService the service used to handle user-related operations
     */
    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Endpoint to retrieve user information by ID.
     * Accepts a user ID as a path variable and returns the user data.
     *
     * @param id the unique identifier of the user
     * @return a {@code ResponseEntity} containing the {@code UserDto} for the requested user
     */
    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUser(@PathVariable Long id) {
        log.info("Attempting to retrieve user with ID: {}", id);
        try {
            UserDto user = userService.getUserById(id);
            log.info("User retrieved successfully with ID: {}", id);
            return ResponseEntity.ok(user);
        } catch (ResourceNotFoundException ex) {
            log.warn("User not found with ID: {}", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (Exception ex) {
            log.error("Unexpected error occurred while retrieving user with ID: {}: {}", id, ex.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}