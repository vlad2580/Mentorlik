package com.mentorlik.mentorlik_backend.dto;

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

    @NotBlank(message = "Email must not be blank")
    @Email(message = "Invalid email format")
    private String email;

    @NotBlank(message = "Password must not be blank")
    @Size(min = 8, message = "Password must be at least 8 characters")
    private String password;
}