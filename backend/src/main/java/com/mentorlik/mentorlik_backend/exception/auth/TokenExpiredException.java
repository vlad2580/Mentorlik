package com.mentorlik.mentorlik_backend.exception.auth;

/**
 * Exception thrown when a verification token has expired.
 */
public class TokenExpiredException extends RuntimeException {

    /**
     * Constructs a new TokenExpiredException with the specified message.
     *
     * @param message the detail message
     */
    public TokenExpiredException(String message) {
        super(message);
    }

    /**
     * Constructs a new TokenExpiredException with the specified message and cause.
     *
     * @param message the detail message
     * @param cause the cause of the exception
     */
    public TokenExpiredException(String message, Throwable cause) {
        super(message, cause);
    }
} 