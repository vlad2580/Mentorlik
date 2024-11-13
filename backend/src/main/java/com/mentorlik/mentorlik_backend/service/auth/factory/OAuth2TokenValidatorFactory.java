package com.mentorlik.mentorlik_backend.service.auth.factory;

import com.mentorlik.mentorlik_backend.service.auth.validator.OAuth2TokenValidator;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Factory for obtaining the appropriate OAuth2 token validator based on the provider.
 */
@Component
public class OAuth2TokenValidatorFactory {

    private final Map<String, OAuth2TokenValidator> validators;

    public OAuth2TokenValidatorFactory(Map<String, OAuth2TokenValidator> validators) {
        this.validators = validators;
    }

    public OAuth2TokenValidator getValidator(String provider) {
        OAuth2TokenValidator validator = validators.get(provider.toLowerCase());
        if (validator == null) {
            throw new IllegalArgumentException("Unsupported OAuth provider: " + provider);
        }
        return validator;
    }
}