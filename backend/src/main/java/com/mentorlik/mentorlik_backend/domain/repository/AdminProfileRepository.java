package com.mentorlik.mentorlik_backend.domain.repository;

import com.mentorlik.mentorlik_backend.domain.entity.AdminProfile;
import java.util.Optional;

/**
 * Repository interface for AdminProfile in the domain layer.
 * <p>
 * This interface defines data access operations for AdminProfile without
 * binding to specific implementation details like JPA or Spring Data.
 * </p>
 */
public interface AdminProfileRepository {
    
    /**
     * Finds an admin profile by its ID.
     *
     * @param id the ID of the admin profile to find
     * @return an Optional containing the admin profile if found, empty otherwise
     */
    Optional<AdminProfile> findById(Long id);
    
    /**
     * Saves an admin profile.
     *
     * @param adminProfile the admin profile to save
     * @return the saved admin profile
     */
    AdminProfile save(AdminProfile adminProfile);
    
    /**
     * Checks if an admin profile exists with the given email.
     *
     * @param email the email to check
     * @return true if an admin profile exists with the email, false otherwise
     */
    boolean existsByEmail(String email);
    
    /**
     * Finds an admin profile by its email.
     *
     * @param email the email of the admin profile to find
     * @return an Optional containing the admin profile if found, empty otherwise
     */
    Optional<AdminProfile> findByEmail(String email);
} 