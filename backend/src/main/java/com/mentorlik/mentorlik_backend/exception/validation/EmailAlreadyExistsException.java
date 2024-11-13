package com.mentorlik.mentorlik_backend.exception.validation;

/**
 * Custom exception thrown when an email is already registered in the system.
 */
public class EmailAlreadyExistsException extends RuntimeException {

    /**
     * Constructs a new {@code EmailAlreadyExistsException} with the specified detail message.
     *
     * @param message the detail message, providing information about the email conflict
     */
    public EmailAlreadyExistsException(String message) {
        super(message);
    }
}