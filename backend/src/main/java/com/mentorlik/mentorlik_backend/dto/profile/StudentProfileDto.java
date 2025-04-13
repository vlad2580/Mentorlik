package com.mentorlik.mentorlik_backend.dto.profile;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * Data Transfer Object (DTO) for Student Profile information.
 * <p>
 * Extends {@link UserDto} to include fields specific to students, such as learning goals, study field, educational level, and skills.
 * This class is used to transfer student-related data across layers of the application, ensuring consistency and type safety.
 * </p>
 */
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class StudentProfileDto extends UserDto {

    /**
     * The primary field or subject that the student is focusing on.
     * Example values could be "Computer Science", "Mathematics", etc.
     */
    @NotBlank(message = "Field of study must not be blank")
    @Size(max = 100, message = "Field of study must not exceed 100 characters")
    private String fieldOfStudy;

    /**
     * The current educational level of the student.
     * Example values could be "Undergraduate", "Postgraduate", "High School".
     */
    @NotBlank(message = "Education level must not be blank")
    @Size(max = 50, message = "Education level must not exceed 50 characters")
    private String educationLevel;

    /**
     * Short description of the student's learning goals or objectives.
     * This provides mentors with insight into the student's focus.
     */
    @NotBlank(message = "Learning goals must not be blank")
    @Size(max = 500, message = "Learning goals must not exceed 500 characters")
    private String learningGoals;

    /**
     * List of skills or subjects the student is proficient in or currently studying.
     * Example values could be "Java Programming", "Data Analysis".
     */
    private List<String> skills;

    /**
     * Availability status of the student.
     * Indicates whether the student is actively looking for mentors.
     */
    @NotNull(message = "Availability status must not be null")
    @Builder.Default
    private Boolean isAvailableForMentorship = false;

    // Constructor with name parameter
    public StudentProfileDto(Long id, String name, String email, String password, String fieldOfStudy, String educationLevel,
                           String learningGoals, List<String> skills, Boolean isAvailableForMentorship) {
        super(id, name, email, password);
        this.fieldOfStudy = fieldOfStudy;
        this.educationLevel = educationLevel;
        this.learningGoals = learningGoals;
        this.skills = skills;
        this.isAvailableForMentorship = isAvailableForMentorship;
    }
} 