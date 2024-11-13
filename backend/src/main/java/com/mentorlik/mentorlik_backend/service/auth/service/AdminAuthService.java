package com.mentorlik.mentorlik_backend.service.auth.service;

import com.mentorlik.mentorlik_backend.dto.profile.AdminProfileDto;
import com.mentorlik.mentorlik_backend.model.AdminProfile;
import com.mentorlik.mentorlik_backend.repository.AdminProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Service for handling admin authentication and profile management.
 * Extends the base {@link BaseAuthService} to provide admin-specific logic for registration and login.
 */
@Service
public class AdminAuthService extends BaseAuthService<AdminProfile, AdminProfileDto> {

    /**
     * Constructs an instance of {@code AdminAuthService} with the required dependencies.
     *
     * @param adminProfileRepository The repository for managing admin profiles.
     * @param passwordEncoder The encoder for hashing passwords.
     */
    @Autowired
    public AdminAuthService(AdminProfileRepository adminProfileRepository, PasswordEncoder passwordEncoder) {
        super(adminProfileRepository, passwordEncoder);
    }

    /**
     * Creates a new {@link AdminProfile} entity from the provided {@link AdminProfileDto}.
     *
     * @param userDto The DTO containing admin profile data.
     * @return A new {@link AdminProfile} entity with the data from the DTO.
     */
    @Override
    protected AdminProfile createUserEntity(AdminProfileDto userDto) {
        AdminProfile admin = new AdminProfile();
        admin.setEmail(userDto.getEmail());
        admin.setPassword(passwordEncoder.encode(userDto.getPassword()));
        admin.setRole(userDto.getRole());
        admin.setAccessLevel(userDto.getAccessLevel());
        admin.setDescription(userDto.getDescription());
        return admin;
    }

    /**
     * Converts an {@link AdminProfile} entity to an {@link AdminProfileDto}.
     *
     * @param admin The admin entity to convert.
     * @return An {@link AdminProfileDto} containing the data from the entity.
     */
    @Override
    protected AdminProfileDto convertToDto(AdminProfile admin) {
        return AdminProfileDto.builder()
                .id(admin.getId())
                .email(admin.getEmail())
                .role(admin.getRole())
                .accessLevel(admin.getAccessLevel())
                .description(admin.getDescription())
                .build();
    }
}