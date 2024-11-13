package com.mentorlik.mentorlik_backend.exception;

import com.mentorlik.mentorlik_backend.exception.validation.EmailAlreadyExistsException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.security.access.AccessDeniedException;

import java.util.Map;
import java.util.stream.Collectors;

/**
 * Global exception handler for the application.
 * <p>
 * This class centralizes exception handling using Spring's {@code @ControllerAdvice},
 * providing a unified way to handle exceptions across all controllers.
 * </p>
 */
@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handles {@link ResourceNotFoundException} exceptions thrown within the application.
     * Returns a {@code NOT_FOUND} (404) response with the exception's message.
     *
     * @param ex the exception thrown when a resource is not found
     * @return a {@code ResponseEntity} containing the error message and {@code HttpStatus.NOT_FOUND}
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<String> handleResourceNotFound(ResourceNotFoundException ex) {
        log.warn("Resource not found: {}", ex.getMessage());
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    /**
     * Handles validation errors, such as {@link MethodArgumentNotValidException}.
     * <p>
     * Returns a {@code BAD_REQUEST} (400) response with detailed validation error messages.
     * </p>
     *
     * @param ex the exception thrown when a validation error occurs
     * @return a {@code ResponseEntity} containing validation error messages and {@code HttpStatus.BAD_REQUEST}
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationException(MethodArgumentNotValidException ex) {
        Map<String, String> errors = ex.getBindingResult().getFieldErrors()
                .stream()
                .collect(Collectors.toMap(
                        error -> error.getField(),
                        error -> error.getDefaultMessage()
                ));
        log.warn("Validation failed: {}", errors);
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles unauthorized access errors, such as {@link AccessDeniedException}.
     * <p>
     * Returns a {@code FORBIDDEN} (403) response with a message indicating access is denied.
     * </p>
     *
     * @param ex the exception thrown when unauthorized access is attempted
     * @return a {@code ResponseEntity} containing an error message and {@code HttpStatus.FORBIDDEN}
     */
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<String> handleUnauthorizedException(AccessDeniedException ex) {
        log.warn("Access denied: Unauthorized access");
        return new ResponseEntity<>("Access denied: Unauthorized access", HttpStatus.FORBIDDEN);
    }

    /**
     * Handles {@link EmailAlreadyExistsException} thrown when a duplicate email is encountered during registration.
     * Returns a {@code CONFLICT} (409) response with the exception's message.
     *
     * @param ex the exception thrown when an email conflict is detected
     * @return a {@code ResponseEntity} containing the error message and {@code HttpStatus.CONFLICT}
     */
    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<String> handleEmailAlreadyExists(EmailAlreadyExistsException ex) {
        log.warn("Email conflict: {}", ex.getMessage());
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.CONFLICT);
    }

    /**
     * Handles any uncaught exceptions that were not handled by specific handlers.
     * <p>
     * Returns an {@code INTERNAL_SERVER_ERROR} (500) response with a generic error message.
     * </p>
     *
     * @param ex the unexpected exception
     * @return a {@code ResponseEntity} containing a generic error message and {@code HttpStatus.INTERNAL_SERVER_ERROR}
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGenericException(Exception ex) {
        log.error("An unexpected error occurred", ex);
        return new ResponseEntity<>("An unexpected error occurred. Please try again later.", HttpStatus.INTERNAL_SERVER_ERROR);
    }
}