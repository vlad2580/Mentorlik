package com.mentorlik.mentorlik_backend.dto.profile;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.List;

/**
 * Data Transfer Object (DTO) for Mentor Profile information.
 * <p>
 * This class extends {@link UserDto} and includes additional fields specific to mentors,
 * such as expertise, bio, experience years, location, hourly rate, and rating.
 * </p>
 */
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class MentorProfileDto extends UserDto {

    /**
     * Mentor's area of expertise, e.g., "Software Development", "Data Science".
     */
    @NotBlank(message = "Expertise field must not be blank")
    @Size(max = 100, message = "Expertise must not exceed 100 characters")
    private String expertise;

    /**
     * Short biography or description about the mentor's qualifications and experience.
     */
    @NotBlank(message = "Bio must not be blank")
    @Size(max = 500, message = "Bio must not exceed 500 characters")
    private String bio;

    /**
     * Total years of experience the mentor has in their area of expertise.
     */
    @NotNull(message = "Experience years must not be null")
    private Integer experienceYears;

    /**
     * List of certifications or credentials the mentor holds.
     */
    private List<String> certifications;

    /**
     * Indicates if the mentor is available for new mentees.
     */
    @NotNull(message = "Availability status must not be null")
    private Boolean isAvailable;

    /**
     * Mentor's city of residence.
     */
    @Size(max = 100, message = "City must not exceed 100 characters")
    private String city;

    /**
     * Mentor's country of residence.
     */
    @Size(max = 100, message = "Country must not exceed 100 characters")
    private String country;

    /**
     * Hourly rate for mentoring sessions.
     */
    @NotNull(message = "Hourly rate must not be null")
    private BigDecimal hourlyRate;

    /**
     * Languages the mentor is fluent in.
     */
    private List<String> languages;

    /**
     * Average rating of the mentor.
     */
    private Double rating;

    /**
     * Total number of reviews the mentor has received.
     */
    private Integer reviewCount;

    private Double rate;
    private String about;
    private String photoUrl;
    private String availability;

    /**
     * Constructs a new MentorProfileDto instance with specified mentor details.
     *
     * @param id              the unique identifier of the mentor
     * @param name            the name of the mentor
     * @param email           the email address of the mentor
     * @param expertise       the area of expertise of the mentor
     * @param bio             a brief biography of the mentor
     * @param experienceYears the number of years of experience
     * @param certifications  a list of certifications the mentor holds
     * @param isAvailable     the availability status for mentoring
     * @param city            the mentor's city of residence
     * @param country         the mentor's country of residence
     * @param hourlyRate      the hourly rate for mentoring sessions
     * @param languages       the languages the mentor speaks
     * @param rating          the average rating of the mentor
     * @param reviewCount     the number of reviews the mentor has received
     */
    public MentorProfileDto(
            Long id,
            @NotBlank(message = "Name must not be blank")
            @Size(max = 100, message = "Name must not exceed 100 characters")
            String name,

            @NotBlank(message = "Email must not be blank")
            @Email(message = "Invalid email format")
            @Size(max = 100, message = "Email must not exceed 100 characters")
            String email,

            String expertise,
            String bio,
            Integer experienceYears,
            List<String> certifications,
            Boolean isAvailable,
            String city,
            String country,
            BigDecimal hourlyRate,
            List<String> languages,
            Double rating,
            Integer reviewCount
    ) {
        super(id, name, email, null);
        this.expertise = expertise;
        this.bio = bio;
        this.experienceYears = experienceYears;
        this.certifications = certifications;
        this.isAvailable = isAvailable;
        this.city = city;
        this.country = country;
        this.hourlyRate = hourlyRate;
        this.languages = languages;
        this.rating = rating;
        this.reviewCount = reviewCount;
    }
}