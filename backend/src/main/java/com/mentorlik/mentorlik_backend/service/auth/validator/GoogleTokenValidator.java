package com.mentorlik.mentorlik_backend.service.auth.validator;

import com.mentorlik.mentorlik_backend.dto.auth.GoogleUserInfo;
import com.mentorlik.mentorlik_backend.dto.auth.OAuth2UserInfo;
import com.mentorlik.mentorlik_backend.exception.auth.InvalidOAuthTokenException;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Optional;

/**
 * Service for validating Google OAuth2 tokens.
 */
@Primary
@Slf4j
@Service("googleTokenValidator")
@RequiredArgsConstructor
public class GoogleTokenValidator implements OAuth2TokenValidator {

    private final GoogleIdTokenVerifier tokenVerifier;

    @Override
    public OAuth2UserInfo validateAndGetUserInfo(String oauthToken) {
        try {
            GoogleIdToken idToken = tokenVerifier.verify(oauthToken);
            if (idToken == null) {
                log.warn("Invalid Google OAuth token: verification failed.");
                throw new InvalidOAuthTokenException("Invalid Google OAuth token.");
            }

            GoogleIdToken.Payload payload = idToken.getPayload();
            String email = Optional.ofNullable((String) payload.get("email"))
                    .orElseThrow(() -> new InvalidOAuthTokenException("Email not available in OAuth token payload."));
            String name = (String) payload.get("name");
            String profilePictureUrl = (String) payload.get("picture");

            log.info("Google OAuth token successfully validated for user with email: {}", email);
            return new GoogleUserInfo(email, name, profilePictureUrl);

        } catch (GeneralSecurityException | IOException e) {
            log.error("Error validating Google OAuth token", e);
            throw new InvalidOAuthTokenException("Error validating Google OAuth token: " + e.getMessage(), e);
        }
    }
}
