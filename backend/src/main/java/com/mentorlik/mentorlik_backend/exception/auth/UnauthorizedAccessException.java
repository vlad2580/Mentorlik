package com.mentorlik.mentorlik_backend.exception.auth;

/**
 * Custom exception to be thrown when an unauthorized access attempt is detected.
 * <p>
 * This exception extends {@code RuntimeException} and is used to indicate
 * that a specific action or resource access was denied due to insufficient permissions.
 * </p>
 */
public class UnauthorizedAccessException extends RuntimeException {

    /**
     * Constructs a new {@code UnauthorizedAccessException} with a default detail message.
     */
    public UnauthorizedAccessException() {
        super("Unauthorized access attempt detected.");
    }

    /**
     * Constructs a new {@code UnauthorizedAccessException} with the specified detail message.
     *
     * @param message the detail message, providing additional context about the unauthorized access
     */
    public UnauthorizedAccessException(String message) {
        super(message);
    }

    /**
     * Constructs a new {@code UnauthorizedAccessException} with the specified detail message and cause.
     *
     * @param message the detail message
     * @param cause   the cause of the exception, allowing further tracing back to the root problem
     */
    public UnauthorizedAccessException(String message, Throwable cause) {
        super(message, cause);
    }
}
