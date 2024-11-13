package com.mentorlik.mentorlik_backend.controller;

import com.mentorlik.mentorlik_backend.dto.profile.AdminProfileDto;
import com.mentorlik.mentorlik_backend.dto.profile.MentorProfileDto;
import com.mentorlik.mentorlik_backend.dto.profile.StudentProfileDto;
import com.mentorlik.mentorlik_backend.exception.ResourceNotFoundException;
import com.mentorlik.mentorlik_backend.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controller responsible for handling user-related endpoints.
 * Provides endpoints for retrieving user profile information for various user types.
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
     * Endpoint to retrieve user information by ID, based on user type.
     * Accepts a user type and ID as path variables and returns the user data.
     *
     * @param userType the type of the user (e.g., "admin", "mentor", "student")
     * @param id       the unique identifier of the user
     * @return a {@code ResponseEntity} containing the user profile DTO for the requested user
     */
    @GetMapping("/{userType}/{id}")
    public ResponseEntity<?> getUser(@PathVariable String userType, @PathVariable Long id) {
        log.info("Attempting to retrieve {} with ID: {}", userType, id);

        try {
            return switch (userType.toLowerCase()) {
                case "mentor" -> getMentorProfile(id);
                case "student" -> getStudentProfile(id);
                case "admin" -> getAdminProfile(id);
                default -> ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid user type: " + userType);
            };
        } catch (ResourceNotFoundException ex) {
            log.warn("{} not found with ID: {}", userType, id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(userType + " not found");
        } catch (Exception ex) {
            log.error("Unexpected error while retrieving {} with ID: {}: {}", userType, id, ex.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred");
        }
    }

    /**
     * Retrieves a mentor profile by ID.
     *
     * @param id the ID of the mentor
     * @return a {@code ResponseEntity} containing the {@code MentorProfileDto}
     */
    private ResponseEntity<MentorProfileDto> getMentorProfile(Long id) {
        MentorProfileDto mentor = userService.getMentorById(id);
        log.info("Mentor retrieved successfully with ID: {}", id);
        return ResponseEntity.ok(mentor);
    }

    /**
     * Retrieves a student profile by ID.
     *
     * @param id the ID of the student
     * @return a {@code ResponseEntity} containing the {@code StudentProfileDto}
     */
    private ResponseEntity<StudentProfileDto> getStudentProfile(Long id) {
        StudentProfileDto student = userService.getStudentById(id);
        log.info("Student retrieved successfully with ID: {}", id);
        return ResponseEntity.ok(student);
    }

    /**
     * Retrieves an admin profile by ID.
     *
     * @param id the ID of the admin
     * @return a {@code ResponseEntity} containing the {@code AdminProfileDto}
     */
    private ResponseEntity<AdminProfileDto> getAdminProfile(Long id) {
        AdminProfileDto admin = userService.getAdminById(id);
        log.info("Admin retrieved successfully with ID: {}", id);
        return ResponseEntity.ok(admin);
    }
}