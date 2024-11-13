package com.mentorlik.mentorlik_backend.service.auth.validator;

import com.mentorlik.mentorlik_backend.dto.auth.OAuth2UserInfo;
import com.mentorlik.mentorlik_backend.exception.auth.InvalidOAuthTokenException;

public interface OAuth2TokenValidator {
    OAuth2UserInfo validateAndGetUserInfo(String oauthToken) throws InvalidOAuthTokenException;
}
