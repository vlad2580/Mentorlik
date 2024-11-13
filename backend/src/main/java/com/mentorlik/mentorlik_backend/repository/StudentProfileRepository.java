package com.mentorlik.mentorlik_backend.repository;

import com.mentorlik.mentorlik_backend.model.StudentProfile;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for managing {@link StudentProfile} entities.
 * <p>
 * This interface extends {@link AbstractUserRepository} to inherit common user operations
 * and provides a layer of abstraction for accessing and managing Student-specific data in the database.
 * </p>
 * <p>
 * As part of the persistence layer, this repository is responsible for querying and managing
 * Student profile data, facilitating interaction with the data access layer and enabling integration
 * with the application business logic.
 * </p>
 */
@Repository
public interface StudentProfileRepository extends AbstractUserRepository<StudentProfile> {

    // Define any Student-specific data access methods here if needed

}