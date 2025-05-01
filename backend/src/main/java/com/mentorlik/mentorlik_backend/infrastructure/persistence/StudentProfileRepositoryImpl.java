package com.mentorlik.mentorlik_backend.infrastructure.persistence;

import com.mentorlik.mentorlik_backend.model.StudentProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Implementation of the StudentProfileRepository interface.
 * <p>
 * This class provides direct access to the database through Spring Data JPA.
 * </p>
 */
@Repository
public interface StudentProfileRepositoryImpl extends JpaRepository<StudentProfile, Long> {
    // All methods are inherited from JpaRepository
} 