package com.mentorlik.mentorlik_backend.application;

import com.mentorlik.mentorlik_backend.api.dto.AdminProfileDto;
import com.mentorlik.mentorlik_backend.domain.entity.AdminProfile;
import com.mentorlik.mentorlik_backend.domain.repository.AdminProfileRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    private final AdminProfileRepository adminRepository;

    /**
     * Constructs a new AdminService with the specified repository.
     *
     * @param adminRepository the repository for admin data access
     */
    public AdminService(AdminProfileRepository adminRepository) {
        this.adminRepository = adminRepository;
    }

    /**
     * Retrieves an admin by their ID and converts it to a DTO.
     *
     * @param id the ID of the admin to retrieve
     * @return a DTO containing the admin's data
     * @throws RuntimeException if no admin with the specified ID is found
     */
    @Transactional(readOnly = true)
    public AdminProfileDto getAdminById(Long id) {
        AdminProfile admin = adminRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Admin not found with ID: " + id));
        
        return mapToDto(admin);
    }

    /**
     * Maps a domain entity to a DTO.
     *
     * @param admin the domain entity to map
     * @return the corresponding DTO
     */
    private AdminProfileDto mapToDto(AdminProfile admin) {
        return AdminProfileDto.builder()
                .id(admin.getId())
                .email(admin.getEmail())
                .accessLevel(admin.getAccessLevel())
                .role(admin.getRole())
                .build();
    }
} 