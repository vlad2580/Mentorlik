package com.mentorlik.mentorlik_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * Data Transfer Object (DTO) for Mentor Profile information.
 * <p>
 * Extends {@link UserDto} to include fields specific to mentors, such as expertise, bio, years of experience, and certifications.
 * This class is used to transfer mentor-related data across layers of the application, ensuring consistency and type safety.
 * </p>
 */
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class MentorProfileDto extends UserDto {

    /**
     * Area of expertise or specialization for the mentor.
     * Example values could be "Software Development", "Data Science", etc.
     */
    @NotBlank(message = "Expertise field must not be blank")
    @Size(max = 100, message = "Expertise must not exceed 100 characters")
    private String expertise;

    /**
     * Short bio or description about the mentor, summarizing qualifications and relevant experience.
     */
    @NotBlank(message = "Bio must not be blank")
    @Size(max = 500, message = "Bio must not exceed 500 characters")
    private String bio;

    /**
     * Total years of experience in the mentor's area of expertise.
     */
    @NotNull(message = "Experience years must not be null")
    private Integer experienceYears;

    /**
     * List of certifications or credentials the mentor has earned.
     * Example values could be "Certified Scrum Master", "AWS Certified Solutions Architect".
     */
    private List<String> certifications;

    /**
     * Availability status of the mentor.
     * Indicates whether the mentor is actively accepting mentees.
     */
    @NotNull(message = "Availability status must not be null")
    private Boolean isAvailable;
}