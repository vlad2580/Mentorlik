package com.mentorlik.mentorlik_backend.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

/**
 * Service for sending emails.
 * <p>
 * Provides methods for sending different types of emails to users,
 * including verification emails, password reset emails, etc.
 * </p>
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;
    
    @Value("${app.url:http://localhost:4200}")
    private String appUrl;
    
    @Value("${spring.mail.username:noreply@mentorlik.com}")
    private String fromEmail;

    /**
     * Sends an email verification link to the given email address.
     *
     * @param email The email address to verify
     * @param token The verification token
     */
    public void sendVerificationEmail(String email, String token, String userType) {
        log.info("Sending verification email to {}", email);
        
        String verificationUrl = appUrl + "/verify-email?token=" + token + "&userType=" + userType;
        
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmail);
        message.setTo(email);
        message.setSubject("Mentorlik - Confirm your email address");
        message.setText("Hello,\n\n" +
                "Thank you for registering with Mentorlik!\n\n" +
                "Please click the link below to verify your email address:\n" +
                verificationUrl + "\n\n" +
                "This link will expire in 24 hours.\n\n" +
                "If you did not create an account, please ignore this email.\n\n" +
                "Best regards,\n" +
                "The Mentorlik Team");
        
        mailSender.send(message);
        log.info("Verification email sent to {}", email);
    }
    
    /**
     * Sends a notification that the email was successfully verified.
     *
     * @param email The verified email address
     */
    public void sendVerificationSuccessEmail(String email) {
        log.info("Sending verification success email to {}", email);
        
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmail);
        message.setTo(email);
        message.setSubject("Mentorlik - Your email has been verified");
        message.setText("Hello,\n\n" +
                "Your email address has been successfully verified!\n\n" +
                "You can now log in to your Mentorlik account.\n\n" +
                "Best regards,\n" +
                "The Mentorlik Team");
        
        mailSender.send(message);
        log.info("Verification success email sent to {}", email);
    }
} 