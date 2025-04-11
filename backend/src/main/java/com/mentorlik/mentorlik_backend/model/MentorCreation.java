package com.mentorlik.mentorlik_backend.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Builder;
import lombok.AllArgsConstructor;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * Entity class representing a mentor creation request.
 * <p>
 * Stores all information provided during the mentor creation process,
 * including personal information, professional details, and profile photo.
 * </p>
 */
@Entity
@Table(name = "mentor_creations")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MentorCreation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String fullname;

    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @Column(length = 255)
    private String linkedin;

    @Column(nullable = false, length = 100)
    private String position;

    @Column(nullable = false, length = 100)
    private String company;

    @Column(nullable = false, length = 50)
    private String experience;

    @Column(nullable = false, length = 100)
    private String specialization;

    @Column(nullable = false, length = 255)
    private String skills;

    @Column(nullable = false, length = 1000)
    private String bio;

    @Column(nullable = false, length = 1000)
    private String help;

    @Column(nullable = false, length = 50)
    private String rate;

    @Column(length = 255)
    private String calendar;

    @Lob
    @Column(nullable = false)
    private byte[] photo;

    @Column(name = "photo_content_type", nullable = false, length = 50)
    private String photoContentType;

    @Column(name = "creation_date", nullable = false)
    private LocalDateTime applicationDate;

    @Column(nullable = false, length = 20)
    private String status;

    @Column(name = "review_date")
    private LocalDateTime reviewDate;

    @Column(length = 1000)
    private String notes;
} 