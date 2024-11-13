package com.mentorlik.mentorlik_backend.exception.auth;

/**
 * Custom exception thrown when an OAuth2 token is invalid or cannot be verified.
 */
public class InvalidOAuthTokenException extends RuntimeException {

    public InvalidOAuthTokenException(String message) {
        super(message);
    }

    public InvalidOAuthTokenException(String message, Throwable cause) {
        super(message, cause);
    }
}
