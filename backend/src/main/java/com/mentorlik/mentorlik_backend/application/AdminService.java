package com.mentorlik.mentorlik_backend.application;

import com.mentorlik.mentorlik_backend.model.AdminProfile;
import com.mentorlik.mentorlik_backend.repository.AdminProfileRepository;
import org.springframework.stereotype.Service;

/**
 * Application service for admin-related operations.
 * <p>
 * This service acts as a facade between the API layer and the domain layer,
 * orchestrating domain objects and handling application-specific concerns like
 * transaction management and data mapping.
 * </p>
 */
@Service
public class AdminService {

    private final AdminProfileRepository adminProfileRepository;

    /**
     * Constructs a new AdminService with the specified repository.
     *
     * @param adminProfileRepository the repository for admin data access
     */
    public AdminService(AdminProfileRepository adminProfileRepository) {
        this.adminProfileRepository = adminProfileRepository;
    }

    /**
     * Retrieves an admin by their ID.
     *
     * @param id the ID of the admin to retrieve
     * @return the admin profile
     * @throws RuntimeException if no admin with the specified ID is found
     */
    public AdminProfile getAdminById(Long id) {
        return adminProfileRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Admin not found with id: " + id));
    }

    /**
     * Retrieves an admin by their email.
     *
     * @param email the email of the admin to retrieve
     * @return the admin profile
     * @throws RuntimeException if no admin with the specified email is found
     */
    public AdminProfile getAdminByEmail(String email) {
        return adminProfileRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Admin not found with email: " + email));
    }
} 