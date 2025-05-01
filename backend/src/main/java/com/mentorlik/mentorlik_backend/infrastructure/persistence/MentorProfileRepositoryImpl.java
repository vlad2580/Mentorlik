package com.mentorlik.mentorlik_backend.infrastructure.persistence;

import com.mentorlik.mentorlik_backend.model.MentorProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Implementation of the MentorProfileRepository interface.
 * <p>
 * This class provides direct access to the database through Spring Data JPA.
 * </p>
 */
@Repository
public interface MentorProfileRepositoryImpl extends JpaRepository<MentorProfile, Long> {
    // All methods are inherited from JpaRepository
} 