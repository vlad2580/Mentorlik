package com.mentorlik.mentorlik_backend.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Entity class representing a mentor in the system.
 * Extends the base user class to inherit common fields.
 * <p>
 * This class includes fields specific to mentors, such as expertise, bio,
 * years of experience, certifications, portfolio URL, and availability status.
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
     * Example values could be "Certified Scrum Master", "AWS Certified Solutions Architect".
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
     * Indicates whether the mentor is actively accepting mentees.
     */
    @Column(nullable = false)
    private Boolean isAvailable;
}