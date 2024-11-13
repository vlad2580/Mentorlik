package com.mentorlik.mentorlik_backend.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

/**
 * Data Transfer Object for handling authentication requests.
 * Contains the user's email and password required for login.
 * <p>
 * This class is designed to support email-based authentication
 * by ensuring that the email is in the correct format and not blank,
 * along with a mandatory password field.
 * </p>
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthRequestDto {

    /**
     * Error message for blank email.
     */
    private static final String EMAIL_NOT_BLANK_MESSAGE = "Email must not be blank";

    /**
     * Error message for blank password.
     */
    private static final String PASSWORD_NOT_BLANK_MESSAGE = "Password must not be blank";

    /**
     * The email of the user attempting to authenticate.
     * <p>
     * Must not be blank and must follow a valid email format.
     * </p>
     */
    @NotBlank(message = EMAIL_NOT_BLANK_MESSAGE)
    @Email(message = "Invalid email format")
    private String email;

    /**
     * The password of the user attempting to authenticate.
     * <p>
     * This field is required and must not be blank.
     * </p>
     */
    @NotBlank(message = PASSWORD_NOT_BLANK_MESSAGE)
    private String password;
}