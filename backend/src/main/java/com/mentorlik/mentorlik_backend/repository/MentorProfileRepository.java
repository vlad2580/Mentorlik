package com.mentorlik.mentorlik_backend.repository;

import com.mentorlik.mentorlik_backend.model.MentorProfile;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for managing {@link MentorProfile} entities.
 * <p>
 * This interface extends the {@link AbstractUserRepository} to inherit
 * common user operations and provides a layer of abstraction for accessing
 * and managing Mentor-specific data in the database.
 * </p>
 * <p>
 * This repository is a part of the persistence layer and is responsible for
 * querying and managing Mentor profile data, enabling integration with the
 * data access layer and the application business logic.
 * </p>
 */
@Repository
public interface MentorProfileRepository extends AbstractUserRepository<MentorProfile> {

    // Define any Mentor-specific data access methods here if needed

}