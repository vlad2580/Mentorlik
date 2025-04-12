package com.mentorlik.mentorlik_backend.domain.repository;

import com.mentorlik.mentorlik_backend.domain.entity.MentorProfile;
import java.util.Optional;

/**
 * Repository interface for MentorProfile in the domain layer.
 */
public interface MentorProfileRepository {
    
    /**
     * Finds a mentor profile by its ID.
     *
     * @param id the ID of the mentor profile to find
     * @return an Optional containing the mentor profile if found, empty otherwise
     */
    Optional<MentorProfile> findById(Long id);
    
    /**
     * Checks if a mentor profile exists with the given email.
     *
     * @param email the email to check
     * @return true if a mentor profile exists with the email, false otherwise
     */
    boolean existsByEmail(String email);
} 