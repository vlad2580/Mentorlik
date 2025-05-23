package com.mentorlik.mentorlik_backend.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * Abstract entity class representing a user in the system.
 * <p>
 * This class serves as a base for different types of users such as Mentor and Student,
 * mapping common fields like email and password to a database table.
 * </p>
 */
@MappedSuperclass
@Data
@NoArgsConstructor
@AllArgsConstructor
public abstract class User {

    /**
     * The unique identifier for each user.
     * Generated automatically by the database.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Name must not be blank")
    @Size(max = 100, message = "Name must not exceed 100 characters")
    private String name;

    /**
     * The email address of the user.
     * <p>
     * Must follow a valid email format, cannot be blank, must be unique, and has a maximum length of 100 characters.
     * </p>
     */
    @NotBlank(message = "Email must not be blank")
    @Email(message = "Invalid email format")
    @Size(max = 100, message = "Email must not exceed 100 characters")
    @Column(nullable = false, unique = true, length = 100)
    private String email;

    /**
     * The password of the user.
     * <p>
     * Must be at least 8 characters long.
     * This field is stored in encrypted form and is not displayed directly.
     * </p>
     */
    @NotBlank(message = "Password must not be blank")
    @Size(min = 8, message = "Password must be at least 8 characters")
    @Column(nullable = false, length = 255)  // Sufficient length for encrypted password
    private String password;
}
