package com.mentorlik.mentorlik_backend.repository;

import com.mentorlik.mentorlik_backend.model.MentorRegistration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for {@link MentorRegistration} entity.
 * <p>
 * Provides methods to interact with mentor registration data in the database.
 * </p>
 */
@Repository
public interface MentorRegistrationRepository extends JpaRepository<MentorRegistration, Long> {
    
    /**
     * Finds all mentor registrations with the given status.
     *
     * @param status status to filter by
     * @return list of mentor registrations
     */
    List<MentorRegistration> findByStatus(String status);
    
    /**
     * Checks if a registration exists with the given email.
     *
     * @param email email to check
     * @return true if a registration exists, false otherwise
     */
    boolean existsByEmail(String email);
} 