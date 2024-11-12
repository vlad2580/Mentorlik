package com.mentorlik.mentorlik_backend.exception;

/**
 * Custom exception to be thrown when a requested resource is not found.
 * <p>
 * This exception extends {@code RuntimeException} and is typically used to indicate
 * that a specific resource (e.g., user, record, or file) does not exist in the system.
 * </p>
 */
public class ResourceNotFoundException extends RuntimeException {

    /**
     * Constructs a new {@code ResourceNotFoundException} with the specified detail message.
     *
     * @param message the detail message, which can be used to provide additional context
     *                about the missing resource
     */
    public ResourceNotFoundException(String message) {
        super(message);
    }

    /**
     * Constructs a new {@code ResourceNotFoundException} with the specified detail message and cause.
     *
     * @param message the detail message
     * @param cause   the cause of the exception, which can be used to trace back to the root problem
     */
    public ResourceNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructs a new {@code ResourceNotFoundException} with a message indicating
     * the type and ID of the missing resource.
     *
     * @param resourceName the name of the missing resource type (e.g., "User")
     * @param resourceId   the ID of the missing resource
     */
    public ResourceNotFoundException(String resourceName, Object resourceId) {
        super(String.format("%s with ID %s not found", resourceName, resourceId));
    }
}