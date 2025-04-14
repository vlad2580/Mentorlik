package com.mentorlik.mentorlik_backend.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

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
    
    @Value("${app.url:http://localhost:80}")
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
        
        String verificationUrl = "/verify-email?token=" + token + "&userType=" + userType;
        String verificationLink = appUrl + verificationUrl;
        
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
            
            helper.setFrom(fromEmail);
            helper.setTo(email);
            helper.setSubject("Mentorlik - Confirm your email address");
            
            String htmlContent = 
                "<!DOCTYPE html>" +
                "<html>" +
                "<head>" +
                "    <meta charset=\"UTF-8\">" +
                "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">" +
                "    <title>Email Verification</title>" +
                "    <style>" +
                "        @import url('https://fonts.googleapis.com/css2?family=Roboto:wght@300;400;500;700&display=swap');" +
                "        body {" +
                "            font-family: 'Roboto', Arial, sans-serif;" +
                "            line-height: 1.6;" +
                "            color: #333333;" +
                "            margin: 0;" +
                "            padding: 0;" +
                "            background-color: #f4f4f4;" +
                "        }" +
                "        .container {" +
                "            max-width: 600px;" +
                "            margin: 0 auto;" +
                "            padding: 20px;" +
                "            background-color: #ffffff;" +
                "            border-radius: 8px;" +
                "            box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);" +
                "        }" +
                "        .header {" +
                "            text-align: center;" +
                "            padding: 20px 0;" +
                "            border-bottom: 1px solid #eeeeee;" +
                "        }" +
                "        .logo {" +
                "            font-size: 28px;" +
                "            font-weight: 700;" +
                "            color: #1e88e5;" +
                "            text-decoration: none;" +
                "        }" +
                "        .content {" +
                "            padding: 30px 20px;" +
                "            text-align: center;" +
                "        }" +
                "        .title {" +
                "            font-size: 24px;" +
                "            color: #333333;" +
                "            margin-bottom: 20px;" +
                "        }" +
                "        .message {" +
                "            font-size: 16px;" +
                "            margin-bottom: 30px;" +
                "            color: #666666;" +
                "        }" +
                "        .button {" +
                "            display: inline-block;" +
                "            background-color: #1e88e5;" +
                "            color: #ffffff !important;" +
                "            text-decoration: none;" +
                "            padding: 12px 30px;" +
                "            border-radius: 4px;" +
                "            font-weight: 500;" +
                "            margin: 20px 0;" +
                "            transition: background-color 0.3s;" +
                "        }" +
                "        .button:hover {" +
                "            background-color: #1976d2;" +
                "        }" +
                "        .note {" +
                "            font-size: 14px;" +
                "            color: #999999;" +
                "            margin-top: 30px;" +
                "        }" +
                "        .footer {" +
                "            text-align: center;" +
                "            padding: 20px;" +
                "            color: #999999;" +
                "            font-size: 14px;" +
                "            border-top: 1px solid #eeeeee;" +
                "        }" +
                "    </style>" +
                "</head>" +
                "<body>" +
                "    <div class=\"container\">" +
                "        <div class=\"header\">" +
                "            <div class=\"logo\">Mentorlik</div>" +
                "        </div>" +
                "        <div class=\"content\">" +
                "            <h1 class=\"title\">Verify Your Email Address</h1>" +
                "            <p class=\"message\">Thank you for registering with Mentorlik! Please click the button below to verify your email address and activate your account.</p>" +
                "            <a href=\"" + verificationLink + "\" class=\"button\">Verify Email</a>" +
                "            <p class=\"message\">If the button above doesn't work, you can copy and paste the following link into your browser:</p>" +
                "            <p style=\"word-break: break-all;\"><a href=\"" + verificationLink + "\">" + verificationLink + "</a></p>" +
                "            <p class=\"note\">This link will expire in 24 hours. If you did not create an account with Mentorlik, please ignore this email.</p>" +
                "        </div>" +
                "        <div class=\"footer\">" +
                "            <p>&copy; " + java.time.Year.now().getValue() + " Mentorlik. All rights reserved.</p>" +
                "        </div>" +
                "    </div>" +
                "</body>" +
                "</html>";
            
            helper.setText(htmlContent, true);
            
            mailSender.send(mimeMessage);
            log.info("Verification email sent to {}", email);
        } catch (MessagingException e) {
            log.error("Failed to send verification email to {}: {}", email, e.getMessage(), e);
        }
    }
    
    /**
     * Sends a notification that the email was successfully verified.
     *
     * @param email The verified email address
     */
    public void sendVerificationSuccessEmail(String email) {
        log.info("Sending verification success email to {}", email);
        
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
            
            helper.setFrom(fromEmail);
            helper.setTo(email);
            helper.setSubject("Mentorlik - Your email has been verified");
            
            String htmlContent = 
                "<!DOCTYPE html>" +
                "<html>" +
                "<head>" +
                "    <meta charset=\"UTF-8\">" +
                "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">" +
                "    <title>Email Verified Successfully</title>" +
                "    <style>" +
                "        @import url('https://fonts.googleapis.com/css2?family=Roboto:wght@300;400;500;700&display=swap');" +
                "        body {" +
                "            font-family: 'Roboto', Arial, sans-serif;" +
                "            line-height: 1.6;" +
                "            color: #333333;" +
                "            margin: 0;" +
                "            padding: 0;" +
                "            background-color: #f4f4f4;" +
                "        }" +
                "        .container {" +
                "            max-width: 600px;" +
                "            margin: 0 auto;" +
                "            padding: 20px;" +
                "            background-color: #ffffff;" +
                "            border-radius: 8px;" +
                "            box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);" +
                "        }" +
                "        .header {" +
                "            text-align: center;" +
                "            padding: 20px 0;" +
                "            border-bottom: 1px solid #eeeeee;" +
                "        }" +
                "        .logo {" +
                "            font-size: 28px;" +
                "            font-weight: 700;" +
                "            color: #1e88e5;" +
                "            text-decoration: none;" +
                "        }" +
                "        .content {" +
                "            padding: 30px 20px;" +
                "            text-align: center;" +
                "        }" +
                "        .title {" +
                "            font-size: 24px;" +
                "            color: #333333;" +
                "            margin-bottom: 20px;" +
                "        }" +
                "        .message {" +
                "            font-size: 16px;" +
                "            margin-bottom: 30px;" +
                "            color: #666666;" +
                "        }" +
                "        .success-icon {" +
                "            font-size: 64px;" +
                "            color: #4caf50;" +
                "            margin-bottom: 20px;" +
                "        }" +
                "        .button {" +
                "            display: inline-block;" +
                "            background-color: #1e88e5;" +
                "            color: #ffffff !important;" +
                "            text-decoration: none;" +
                "            padding: 12px 30px;" +
                "            border-radius: 4px;" +
                "            font-weight: 500;" +
                "            margin: 20px 0;" +
                "            transition: background-color 0.3s;" +
                "        }" +
                "        .button:hover {" +
                "            background-color: #1976d2;" +
                "        }" +
                "        .footer {" +
                "            text-align: center;" +
                "            padding: 20px;" +
                "            color: #999999;" +
                "            font-size: 14px;" +
                "            border-top: 1px solid #eeeeee;" +
                "        }" +
                "    </style>" +
                "</head>" +
                "<body>" +
                "    <div class=\"container\">" +
                "        <div class=\"header\">" +
                "            <div class=\"logo\">Mentorlik</div>" +
                "        </div>" +
                "        <div class=\"content\">" +
                "            <div class=\"success-icon\">✓</div>" +
                "            <h1 class=\"title\">Email Successfully Verified!</h1>" +
                "            <p class=\"message\">Your email address has been successfully verified. Your Mentorlik account is now active.</p>" +
                "            <p class=\"message\">You can now log in and start using all the features of the platform.</p>" +
                "            <a href=\"" + appUrl + "/login\" class=\"button\">Log In Now</a>" +
                "        </div>" +
                "        <div class=\"footer\">" +
                "            <p>&copy; " + java.time.Year.now().getValue() + " Mentorlik. All rights reserved.</p>" +
                "        </div>" +
                "    </div>" +
                "</body>" +
                "</html>";
            
            helper.setText(htmlContent, true);
            
            mailSender.send(mimeMessage);
            log.info("Verification success email sent to {}", email);
        } catch (MessagingException e) {
            log.error("Failed to send verification success email to {}: {}", email, e.getMessage(), e);
        }
    }
} 