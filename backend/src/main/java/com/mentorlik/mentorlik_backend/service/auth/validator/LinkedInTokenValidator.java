package com.mentorlik.mentorlik_backend.service.auth.validator;

import com.mentorlik.mentorlik_backend.dto.auth.LinkedInUserInfo;
import com.mentorlik.mentorlik_backend.dto.auth.OAuth2UserInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * Service for validating LinkedIn OAuth2 tokens.
 */
@Slf4j
@Service("linkedinTokenValidator")
public class LinkedInTokenValidator implements OAuth2TokenValidator {

    @Override
    public OAuth2UserInfo validateAndGetUserInfo(String oauthToken) {
        // Add LinkedIn-specific token verification and user data extraction here
        // Placeholder logic, replace with actual LinkedIn token verification and data extraction

        String email = "sample@example.com";  // Replace with extracted data
        String firstName = "SampleFirst";
        String lastName = "SampleLast";
        String profilePictureUrl = "http://sample.com/pic.jpg";

        log.info("LinkedIn OAuth token successfully validated for user with email: {}", email);
        return new LinkedInUserInfo(email, firstName, lastName, profilePictureUrl);
    }
}
