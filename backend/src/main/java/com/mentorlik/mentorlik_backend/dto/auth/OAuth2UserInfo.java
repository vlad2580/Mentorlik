package com.mentorlik.mentorlik_backend.dto.auth;

public interface OAuth2UserInfo {
    String getEmail();
    String getName();
    String getProfilePictureUrl();
}