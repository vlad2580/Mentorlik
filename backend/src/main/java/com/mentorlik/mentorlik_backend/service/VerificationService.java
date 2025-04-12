package com.mentorlik.mentorlik_backend.service;

import com.mentorlik.mentorlik_backend.exception.ResourceNotFoundException;
import com.mentorlik.mentorlik_backend.exception.auth.TokenExpiredException;
import com.mentorlik.mentorlik_backend.model.StudentProfile;
import com.mentorlik.mentorlik_backend.model.VerificationToken;
import com.mentorlik.mentorlik_backend.repository.StudentProfileRepository;
import com.mentorlik.mentorlik_backend.repository.VerificationTokenRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service for handling email verification.
 * <p>
 * Provides methods for creating and validating verification tokens,
 * and for updating user status based on token verification.
 * </p>
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class VerificationService {

    private final VerificationTokenRepository tokenRepository;
    private final StudentProfileRepository studentRepository;
    private final EmailService emailService;

    /**
     * Creates a verification token for the given user and sends a verification email.
     *
     * @param email The email address to verify
     * @param userId The ID of the user this token is for
     * @param userType The type of user (e.g., "student", "mentor")
     */
    @Transactional
    public void createVerificationTokenAndSendEmail(String email, Long userId, String userType) {
        log.info("Creating verification token for {} with ID: {}", userType, userId);
        
        // Create and save token
        VerificationToken token = VerificationToken.create(email, userId, userType);
        tokenRepository.save(token);
        
        // Send verification email
        emailService.sendVerificationEmail(email, token.getToken(), userType);
    }

    /**
     * Verifies a token and activates the associated account.
     *
     * @param token The token to verify
     * @return The email associated with the token
     */
    @Transactional
    public String verifyToken(String token) {
        log.info("Verifying token: {}", token);
        
        VerificationToken verificationToken = tokenRepository.findByToken(token)
                .orElseThrow(() -> new ResourceNotFoundException("Token not found"));
        
        if (verificationToken.isUsed()) {
            log.warn("Token already used: {}", token);
            throw new IllegalStateException("Token has already been used");
        }
        
        if (verificationToken.isExpired()) {
            log.warn("Token expired: {}", token);
            throw new TokenExpiredException("Token has expired");
        }
        
        String userType = verificationToken.getUserType();
        Long userId = verificationToken.getUserId();
        
        log.info("Token is valid for {} with ID: {}", userType, userId);
        
        // Activate the account based on user type
        if ("student".equalsIgnoreCase(userType)) {
            activateStudentAccount(userId);
        } else {
            log.warn("Unsupported user type: {}", userType);
            throw new UnsupportedOperationException("Verification for " + userType + " not implemented");
        }
        
        // Mark token as used
        verificationToken.markAsUsed();
        tokenRepository.save(verificationToken);
        
        // Send confirmation email
        String email = verificationToken.getEmail();
        emailService.sendVerificationSuccessEmail(email);
        
        return email;
    }
    
    /**
     * Activates a student account.
     *
     * @param studentId The ID of the student to activate
     */
    private void activateStudentAccount(Long studentId) {
        log.info("Activating student account with ID: {}", studentId);
        
        StudentProfile student = studentRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with ID: " + studentId));
        
        // Set the email verification flag
        student.setEmailVerified(true);
        
        studentRepository.save(student);
        log.info("Student account activated: {}", studentId);
    }
} 