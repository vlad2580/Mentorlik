package com.mentorlik.mentorlik_backend.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Entity class representing a student in the system.
 * <p>
 * Extends the base {@link User} class to inherit common fields
 * like email and password, and adds fields specific to students.
 * </p>
 */
@Entity
@Table(name = "students")
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class StudentProfile extends User {

    /**
     * The primary field or subject that the student is focusing on.
     */
    @Column(nullable = false, length = 100)
    private String fieldOfStudy;

    /**
     * The current educational level of the student.
     */
    @Column(nullable = false, length = 50)
    private String educationLevel;

    /**
     * Short description of the student's learning goals or objectives.
     */
    @Column(nullable = false, length = 500)
    private String learningGoals;

    /**
     * Brief description about the student, including background and interests.
     */
    @Column(length = 1000)
    private String about;

    /**
     * List of skills or subjects the student is proficient in or currently studying.
     */
    @ElementCollection
    @CollectionTable(name = "student_skills", joinColumns = @JoinColumn(name = "student_id"))
    @Column(name = "skill")
    private List<String> skills;

    /**
     * Availability status of the student.
     * Indicates whether the student is actively looking for mentors.
     */
    @Column(nullable = false)
    private Boolean isAvailableForMentorship = false;
    
    /**
     * Flag indicating whether the student's email has been verified.
     */
    @Column(nullable = false)
    private Boolean emailVerified = false;
}