package com.mentorlik.mentorlik_backend.domain.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * Entity representing a student user profile in the system.
 */
@Data
@EqualsAndHashCode(callSuper=true)
@NoArgsConstructor
@AllArgsConstructor
public class StudentProfile extends User {

    /**
     * The student's field of study.
     */
    private String fieldOfStudy;
    
    /**
     * The student's education level (e.g., "Bachelor", "Master", "PhD").
     */
    private String educationLevel;
    
    /**
     * The student's learning goals/objectives.
     */
    private List<String> learningGoals;
    
    /**
     * The student's current skills.
     */
    private List<String> skills;
    
    /**
     * Flag indicating if the student is available/looking for mentorship.
     */
    private Boolean isAvailableForMentorship;
} 