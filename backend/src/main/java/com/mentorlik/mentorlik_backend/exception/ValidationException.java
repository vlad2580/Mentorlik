package com.mentorlik.mentorlik_backend.exception;

/**
 * Custom exception to be thrown when validation fails for a particular entity or data field.
 * <p>
 * This exception extends {@code RuntimeException} and is used to signal that
 * a validation rule has been violated.
 * </p>
 */
public class ValidationException extends RuntimeException {

    /**
     * Constructs a new {@code ValidationException} with a default message indicating validation failure.
     */
    public ValidationException() {
        super("Validation failed for the provided data.");
    }

    /**
     * Constructs a new {@code ValidationException} with the specified detail message.
     *
     * @param message the detail message providing additional information about the validation failure
     */
    public ValidationException(String message) {
        super(message);
    }

    /**
     * Constructs a new {@code ValidationException} with the specified detail message and cause.
     *
     * @param message the detail message
     * @param cause   the cause of the exception, allowing further tracing back to the underlying issue
     */
    public ValidationException(String message, Throwable cause) {
        super(message, cause);
    }
}
