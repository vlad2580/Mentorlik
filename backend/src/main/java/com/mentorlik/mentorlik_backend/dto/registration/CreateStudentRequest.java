package com.mentorlik.mentorlik_backend.dto.registration;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * Data Transfer Object for student creation/registration.
 * <p>
 * Contains all fields required for creating a new student account,
 * including email, password, and student-specific information.
 * </p>
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateStudentRequest {

    /**
     * The full name of the student.
     */
    @NotBlank(message = "Name must not be blank")
    @Size(max = 100, message = "Name must not exceed 100 characters")
    private String name;

    /**
     * The email address of the student, used for account verification and login.
     */
    @NotBlank(message = "Email must not be blank")
    @Email(message = "Invalid email format")
    private String email;

    /**
     * The password for the student's account.
     */
    @NotBlank(message = "Password must not be blank")
    @Size(min = 8, message = "Password must be at least 8 characters")
    private String password;

    /**
     * The primary field or subject that the student is focusing on.
     */
    @NotBlank(message = "Field of study must not be blank")
    @Size(max = 100, message = "Field of study must not exceed 100 characters")
    private String fieldOfStudy;

    /**
     * The current educational level of the student.
     */
    @NotBlank(message = "Education level must not be blank")
    @Size(max = 50, message = "Education level must not exceed 50 characters")
    private String educationLevel;

    /**
     * Short description of the student's learning goals or objectives.
     */
    @NotBlank(message = "Learning goals must not be blank")
    @Size(max = 500, message = "Learning goals must not exceed 500 characters")
    private String learningGoals;

    /**
     * Optional list of skills or proficiencies that the student possesses.
     */
    private List<String> skills;

    /**
     * Indicates whether the student is willing to provide mentorship to others.
     * Default value is false.
     */
    private Boolean isAvailableForMentorship = false;
} 