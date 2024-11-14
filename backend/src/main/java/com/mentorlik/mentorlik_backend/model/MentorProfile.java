package com.mentorlik.mentorlik_backend.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

/**
 * Entity class representing a mentor in the system.
 * Extends the base user class to inherit common fields.
 * <p>
 * This class includes fields specific to mentors, such as expertise, bio,
 * years of experience, certifications, location, hourly rate, languages,
 * rating, and availability status.
 * </p>
 */
@Entity
@Table(name = "mentors")
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class MentorProfile extends User {

    /**
     * Area of expertise or specialization for the mentor.
     * Example values could be "Software Development", "Data Science", etc.
     */
    @Column(nullable = false, length = 100)
    private String expertise;

    /**
     * Short bio or description about the mentor, summarizing qualifications and relevant experience.
     */
    @Column(nullable = false, length = 500)
    private String bio;

    /**
     * Total years of experience in the mentor's area of expertise.
     */
    @Column(nullable = false)
    private Integer experienceYears;

    /**
     * List of certifications or credentials the mentor has earned.
     */
    @ElementCollection
    @CollectionTable(name = "mentor_certifications", joinColumns = @JoinColumn(name = "mentor_id"))
    @Column(name = "certification")
    private List<String> certifications;

    /**
     * URL of the mentor's portfolio or professional profile, if available.
     */
    @Column(length = 255)
    private String portfolioUrl;

    /**
     * Availability status of the mentor.
     */
    @Column(nullable = false)
    private Boolean isAvailable;

    /**
     * City where the mentor is located.
     */
    @Column(length = 100)
    private String city;

    /**
     * Country where the mentor is located.
     */
    @Column(length = 100)
    private String country;

    /**
     * Hourly rate for mentoring sessions.
     */
    @Column(precision = 10, scale = 2)
    private BigDecimal hourlyRate;

    /**
     * Languages the mentor can communicate in.
     */
    @ElementCollection
    @CollectionTable(name = "mentor_languages", joinColumns = @JoinColumn(name = "mentor_id"))
    @Column(name = "language", length = 50)
    private List<String> languages;

    /**
     * Average rating of the mentor.
     */
    @Column(precision = 3, scale = 2)
    private BigDecimal rating;

    /**
     * Total number of reviews the mentor has received.
     */
    private Integer reviewCount;
}