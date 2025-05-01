package com.mentorlik.mentorlik_backend.controller;

import com.mentorlik.mentorlik_backend.dto.ApiResponse;
import com.mentorlik.mentorlik_backend.dto.MentorCreationRequest;
import com.mentorlik.mentorlik_backend.model.MentorCreation;
import com.mentorlik.mentorlik_backend.service.MentorCreationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Base64;
import java.util.List;

/**
 * Controller for handling mentor creation.
 * <p>
 * Provides an endpoint for creating mentors, including
 * uploading their profile photos.
 * </p>
 */
@Slf4j
@RestController
@RequestMapping("/api/mentors")
public class MentorController {

    private final MentorCreationService mentorCreationService;

    /**
     * Creates an instance of the mentor controller.
     *
     * @param mentorCreationService service for creating mentors
     */
    public MentorController(MentorCreationService mentorCreationService) {
        this.mentorCreationService = mentorCreationService;
    }

    /**
     * Retrieves all mentor creation requests.
     *
     * @return a list of all mentor creation requests
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<MentorCreation>>> getAllMentors() {
        log.info("Retrieving all mentor creation requests");
        List<MentorCreation> mentors = mentorCreationService.getAllMentorCreations();
        return ResponseEntity.ok(ApiResponse.success(mentors));
    }

    /**
     * Retrieves a mentor creation request by ID.
     *
     * @param id the ID of the mentor creation request
     * @return the mentor creation request with the given ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<MentorCreation>> getMentorById(@PathVariable Long id) {
        log.info("Retrieving mentor creation request with ID: {}", id);
        try {
            MentorCreation mentor = mentorCreationService.getMentorCreationById(id);
            return ResponseEntity.ok(ApiResponse.success(mentor));
        } catch (Exception e) {
            log.error("Error retrieving mentor with ID: {}", id, e);
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error("Mentor with ID " + id + " not found"));
        }
    }

    /**
     * Creates a new mentor in the system.
     * <p>
     * Accepts mentor data in JSON format.
     * </p>
     *
     * @param request mentor data in JSON format
     * @return response with information about the created mentor
     */
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse<Void>> createMentor(@RequestBody MentorCreationRequest request) {
        log.info("Received request to create a new mentor (JSON): {}", request.getEmail());
        
        try {
            // Data preparation and service call
            if (request.getPhoto() == null && request.getPhotoBase64() != null) {
                // Convert photo from Base64
                try {
                    byte[] decodedPhoto = Base64.getDecoder().decode(request.getPhotoBase64());
                    request.setPhoto(decodedPhoto);
                    log.info("Photo successfully decoded from Base64, size: {} bytes", decodedPhoto.length);
                } catch (IllegalArgumentException e) {
                    log.error("Error decoding Base64 photo: {}", e.getMessage());
                    return ResponseEntity
                            .badRequest()
                            .body(ApiResponse.error("Invalid photo format. Please check the image file and try again."));
                }
            }
            
            mentorCreationService.createMentor(request);
            
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(ApiResponse.success(null, "Mentor was successfully created"));
            
        } catch (IllegalArgumentException e) {
            // Handle validation errors, including photo format issues
            log.error("Validation error creating mentor: {}", e.getMessage());
            
            String userMessage = e.getMessage();
            if (userMessage.contains("Invalid photo format")) {
                userMessage = "Invalid photo format. Only JPEG and PNG formats are supported.";
            }
            
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error(userMessage));
        } catch (org.springframework.dao.DataIntegrityViolationException e) {
            // Handle duplicate email case
            log.error("Data integrity violation creating mentor", e);
            if (e.getMessage().contains("duplicate key") && e.getMessage().contains("email")) {
                return ResponseEntity
                        .status(HttpStatus.CONFLICT)
                        .body(ApiResponse.error("A mentor request with this email address already exists. Please use a different email address."));
            }
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error("Unable to create mentor due to data constraints. Please check your input."));
        } catch (Exception e) {
            log.error("Error creating mentor", e);
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("An unexpected error occurred. Please try again later."));
        }
    }
}