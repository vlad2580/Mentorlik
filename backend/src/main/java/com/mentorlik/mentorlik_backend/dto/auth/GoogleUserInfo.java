package com.mentorlik.mentorlik_backend.dto.auth;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

/**
 * Represents the user information retrieved from Google OAuth2.
 */
@Getter
@ToString
@RequiredArgsConstructor
public class GoogleUserInfo implements OAuth2UserInfo {

    private final String email;
    private final String name;
    private final String profilePictureUrl;

    public GoogleUserInfo(String email) {
        this(email, null, null);
    }
}