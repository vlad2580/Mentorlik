package com.mentorlik.mentorlik_backend.domain.entity;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;

/**
 * Entity representing an admin user profile in the system.
 * <p>
 * Contains admin-specific attributes and validation rules to ensure
 * business rules are enforced at the domain level.
 * </p>
 */
@Data
@EqualsAndHashCode(callSuper=true)
@NoArgsConstructor
@AllArgsConstructor
public class AdminProfile extends User {

    /**
     * The access level of the admin.
     * <p>
     * Must be a valid integer representing the admin's permission level.
     * Example values: "1" (Viewer), "2" (Editor), "3" (Moderator), "4" (Admin), "5" (Superadmin)
     * </p>
     */
    @NotBlank(message = "Access level must not be blank")
    @Pattern(regexp = "^[0-9]+$", message = "Access level must be a valid integer")
    private String accessLevel;
    
    /**
     * The role of the admin.
     * <p>
     * Describes the admin's position or job title within the system.
     * Example values: "System Administrator", "Content Manager"
     * </p>
     */
    @NotBlank(message = "Role must not be blank")
    private String role;
    
    /**
     * Validates if this admin profile has sufficient access level for a given action.
     *
     * @param requiredLevel the minimum access level required
     * @return true if this admin has sufficient access level, false otherwise
     */
    public boolean hasAccessLevel(int requiredLevel) {
        try {
            int level = Integer.parseInt(accessLevel);
            return level >= requiredLevel;
        } catch (NumberFormatException e) {
            return false;
        }
    }
} 