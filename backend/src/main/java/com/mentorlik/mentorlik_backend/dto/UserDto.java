package com.mentorlik.mentorlik_backend.dto;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Builder;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * Data Transfer Object (DTO) for User information.
 * <p>
 * This class is used to transfer user-related data between
 * different layers of the application, such as between the
 * client and server or service layers.
 * </p>
 * <p>
 * The {@code @Data} annotation from Lombok generates getter, setter,
 * equals, hashCode, and toString methods automatically.
 * </p>
 * <p>
 * Validation constraints are applied to ensure that data fields
 * are not null and meet specified requirements.
 * </p>
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDto {

    /**
     * Unique identifier for the user.
     */
    private Long id;

    /**
     * The email address of the user.
     * <p>
     * This field must not be blank and must follow a valid email format.
     * </p>
     */
    @NotBlank(message = UserDtoConstants.EMAIL_NOT_BLANK)
    @Email(message = UserDtoConstants.EMAIL_FORMAT)
    private String email;

    /**
     * The password of the user.
     * <p>
     * This field must not be blank and must contain at least 8 characters.
     * </p>
     */
    @NotBlank(message = UserDtoConstants.PASSWORD_NOT_BLANK)
    @Size(min = 8, message = UserDtoConstants.PASSWORD_SIZE)
    private String password;

    /**
     * Constructs a {@code UserDto} instance with only ID and email.
     *
     * @param id    the unique identifier for the user
     * @param email the email address of the user
     */
    public UserDto(Long id, String email) {
        this.id = id;
        this.email = email;
    }

    /**
     * Internal class for storing error messages as constants.
     */
    public static class UserDtoConstants {
        public static final String EMAIL_NOT_BLANK = "Email must not be blank";
        public static final String EMAIL_FORMAT = "Invalid email format";
        public static final String PASSWORD_NOT_BLANK = "Password must not be blank";
        public static final String PASSWORD_SIZE = "Password must be at least 8 characters";
    }
}