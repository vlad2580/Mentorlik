package com.mentorlik.mentorlik_backend.service.auth.service;

import com.mentorlik.mentorlik_backend.dto.profile.UserDto;

/**
 * Interface for services that support OAuth2-based authentication.
 */
public interface OAuth2AuthService {

    /**
     * Logs in a user using an OAuth2 token.
     *
     * @param oauthToken the OAuth2 token used for authentication
     * @return a {@link UserDto} containing user information if login is successful
     */
    UserDto loginWithOAuth(String userType, String oauthToken);
}