package com.mentorlik.mentorlik_backend.repository;

import com.mentorlik.mentorlik_backend.model.MentorCreation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for {@link MentorCreation} entity.
 * <p>
 * Provides methods to interact with mentor creation data in the database.
 * </p>
 */
@Repository
public interface MentorCreationRepository extends JpaRepository<MentorCreation, Long> {
    
    /**
     * Finds all mentor creations with the given status.
     *
     * @param status status to filter by
     * @return list of mentor creations
     */
    List<MentorCreation> findByStatus(String status);
    
    /**
     * Checks if a creation request exists with the given email.
     *
     * @param email email to check
     * @return true if a creation request exists, false otherwise
     */
    boolean existsByEmail(String email);
} 