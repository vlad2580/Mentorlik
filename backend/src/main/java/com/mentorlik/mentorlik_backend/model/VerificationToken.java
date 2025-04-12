package com.mentorlik.mentorlik_backend.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Entity class representing an email verification token in the system.
 * <p>
 * Used to verify user email addresses during registration by providing
 * a unique token that can be sent via email and validated later.
 * </p>
 */
@Entity
@Table(name = "verification_tokens")
@Data
@NoArgsConstructor
public class VerificationToken {

    /**
     * The unique identifier for each verification token.
     * Generated automatically by the database.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * The unique token value used for verification.
     */
    @Column(nullable = false, unique = true)
    private String token;

    /**
     * The email address associated with this verification token.
     */
    @Column(nullable = false)
    private String email;

    /**
     * The user ID associated with this verification token.
     */
    @Column(nullable = false)
    private Long userId;

    /**
     * The type of user this token is for.
     */
    @Column(nullable = false)
    private String userType;

    /**
     * The date and time when this token was created.
     */
    @Column(nullable = false)
    private LocalDateTime createdAt;

    /**
     * The date and time when this token expires.
     */
    @Column(nullable = false)
    private LocalDateTime expiresAt;

    /**
     * Flag indicating whether this token has been used.
     */
    @Column(nullable = false)
    private boolean used;

    /**
     * Creates a new verification token with default values.
     *
     * @param email The email address to verify
     * @param userId The ID of the user this token is for
     * @param userType The type of user (e.g., "student", "mentor")
     * @return A new verification token
     */
    public static VerificationToken create(String email, Long userId, String userType) {
        VerificationToken token = new VerificationToken();
        token.setToken(UUID.randomUUID().toString());
        token.setEmail(email);
        token.setUserId(userId);
        token.setUserType(userType);
        token.setCreatedAt(LocalDateTime.now());
        token.setExpiresAt(LocalDateTime.now().plusHours(24)); // Token valid for 24 hours
        token.setUsed(false);
        return token;
    }

    /**
     * Checks if this token has expired.
     *
     * @return true if the token has expired, false otherwise
     */
    public boolean isExpired() {
        return LocalDateTime.now().isAfter(this.expiresAt);
    }

    /**
     * Marks this token as used.
     */
    public void markAsUsed() {
        this.used = true;
    }
} 