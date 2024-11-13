package com.mentorlik.mentorlik_backend.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * Entity class representing an admin user in the system.
 * <p>
 * Extends the base {@link User} class to inherit common fields like email and password,
 * and adds fields specific to admin users, such as access level, role, and description.
 * </p>
 */
@Entity
@Table(name = "admins")
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class AdminProfile extends User {

    /**
     * Represents the admin's access level within the system.
     * This could define the level of privileges the admin has.
     * <p>
     * Example values:
     * <ul>
     *     <li>1 - Viewer</li>
     *     <li>2 - Editor</li>
     *     <li>3 - Moderator</li>
     *     <li>4 - Admin</li>
     *     <li>5 - Superadmin</li>
     * </ul>
     */
    @Column(nullable = false)
    private Integer accessLevel;

    /**
     * Represents the admin's role or title within the system.
     * Provides a description of the admin's position, such as "System Administrator" or "Content Manager".
     */
    @Column(nullable = false, length = 100)
    private String role;

    /**
     * Additional description providing details about the admin's responsibilities
     * or notes relevant to their role in the system.
     * This field is optional and can be used to store supplementary information.
     */
    @Column(length = 500)
    private String description;
}