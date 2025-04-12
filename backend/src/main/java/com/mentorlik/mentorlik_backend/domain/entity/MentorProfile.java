package com.mentorlik.mentorlik_backend.domain.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.List;

/**
 * Entity representing a mentor user profile in the system.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MentorProfile extends User {

    /**
     * The expertise/specialization areas of the mentor.
     */
    private String expertise;
    
    /**
     * Professional biography of the mentor.
     */
    private String bio;
    
    /**
     * Number of years of professional experience.
     */
    private Integer experienceYears;
    
    /**
     * List of relevant certifications.
     */
    private List<String> certifications;
    
    /**
     * Flag indicating if the mentor is available for new mentees.
     */
    private Boolean isAvailable;
} 