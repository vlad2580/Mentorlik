package com.mentorlik.mentorlik_backend.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for transferring admin profile data between the API layer and application layer.
 * <p>
 * This class contains only the data needed for API requests/responses and
 * prevents exposing domain entities directly to the API.
 * </p>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminProfileDto {
    
    /**
     * The ID of the admin profile.
     */
    private Long id;
    
    /**
     * The name of the admin.
     */
    private String name;
    
    /**
     * The email address of the admin.
     */
    private String email;
    
    /**
     * The access level of the admin (e.g., "FULL", "LIMITED").
     */
    private String accessLevel;
    
    /**
     * The role of the admin (e.g., "SUPER_ADMIN", "CONTENT_ADMIN").
     */
    private String role;
} 