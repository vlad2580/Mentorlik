package com.mentorlik.mentorlik_backend.dto.auth;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

/**
 * Represents the user information retrieved from LinkedIn OAuth2.
 */
@Getter
@ToString
@RequiredArgsConstructor
public class LinkedInUserInfo implements OAuth2UserInfo {

    private final String email;
    private final String firstName;
    private final String lastName;
    private final String profilePictureUrl;

    @Override
    public String getName() {
        return String.format("%s %s", firstName, lastName);
    }
}