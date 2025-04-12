package com.mentorlik.mentorlik_backend.domain.repository;

import com.mentorlik.mentorlik_backend.domain.entity.StudentProfile;
import java.util.Optional;

/**
 * Repository interface for StudentProfile in the domain layer.
 */
public interface StudentProfileRepository {
    
    /**
     * Finds a student profile by its ID.
     *
     * @param id the ID of the student profile to find
     * @return an Optional containing the student profile if found, empty otherwise
     */
    Optional<StudentProfile> findById(Long id);
    
    /**
     * Checks if a student profile exists with the given email.
     *
     * @param email the email to check
     * @return true if a student profile exists with the email, false otherwise
     */
    boolean existsByEmail(String email);
} 