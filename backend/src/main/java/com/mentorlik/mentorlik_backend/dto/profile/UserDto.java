package com.mentorlik.mentorlik_backend.dto.profile;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * Abstract Data Transfer Object (DTO) for user information.
 * Provides shared fields and validation for specific user types.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public abstract class UserDto {

    private Long id;

    @NotBlank(message = "Name must not be blank")
    @Size(max = 100, message = "Name must not exceed 100 characters")
    private String name;

    @NotBlank(message = "Email must not be blank")
    @Email(message = "Invalid email format")
    private String email;

    @NotBlank(message = "Password must not be blank")
    @Size(min = 8, message = "Password must be at least 8 characters")
    private String password;
    
    /**
     * JWT token for authentication.
     * This field is populated after successful login/registration.
     * Not required during validation as it's set by the server.
     */
    private String token;
    
    /**
     * Constructor without token field
     */
    public UserDto(Long id, String name, String email, String password) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
    }
}