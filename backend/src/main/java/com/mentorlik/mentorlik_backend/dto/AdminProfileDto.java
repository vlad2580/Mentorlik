package com.mentorlik.mentorlik_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Data Transfer Object (DTO) for Admin Profile information.
 * <p>
 * Extends {@link UserDto} to include fields specific to admins, such as role and access level.
 * This class is used to transfer admin-related data across layers of the application, ensuring consistency and type safety.
 * </p>
 */
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class AdminProfileDto extends UserDto {

    /**
     * The role or position of the admin within the system.
     * Example values could be "System Administrator", "Content Moderator", etc.
     */
    @NotBlank(message = "Role must not be blank")
    @Size(max = 50, message = "Role must not exceed 50 characters")
    private String role;

    /**
     * The access level of the admin within the system, typically as an integer.
     * Higher values may represent greater privileges.
     */
    @NotNull(message = "Access level must not be null")
    private Integer accessLevel;

    /**
     * Brief description of the admin's responsibilities or authority within the system.
     */
    @Size(max = 200, message = "Description must not exceed 200 characters")
    private String description;
}