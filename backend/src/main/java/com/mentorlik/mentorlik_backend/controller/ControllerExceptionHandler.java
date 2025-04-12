package com.mentorlik.mentorlik_backend.controller;

import com.mentorlik.mentorlik_backend.dto.ApiResponse;
import com.mentorlik.mentorlik_backend.exception.ResourceNotFoundException;
import com.mentorlik.mentorlik_backend.exception.auth.InvalidOAuthTokenException;
import com.mentorlik.mentorlik_backend.exception.auth.UnauthorizedAccessException;
import com.mentorlik.mentorlik_backend.exception.validation.EmailAlreadyExistsException;
import com.mentorlik.mentorlik_backend.exception.validation.ValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Exception handler for all API controllers.
 * <p>
 * This class intercepts exceptions that occur in controllers
 * and transforms them into unified API responses in the {@link ApiResponse} format.
 * </p>
 */
@Slf4j
@RestControllerAdvice
public class ControllerExceptionHandler {

    /**
     * Handles exceptions of type {@link ResourceNotFoundException}.
     *
     * @param ex the resource not found exception
     * @param request the HTTP request
     * @return an API response with error and status 404 NOT_FOUND
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiResponse<Void> handleResourceNotFoundException(
            ResourceNotFoundException ex, WebRequest request) {
        String path = ((ServletWebRequest) request).getRequest().getRequestURI();
        log.warn("Resource not found: {}", ex.getMessage());
        return ApiResponse.error(ex.getMessage(), path);
    }

    /**
     * Handles request validation exceptions.
     *
     * @param ex the method argument validation exception
     * @param request the HTTP request
     * @return an API response with validation errors and status 400 BAD_REQUEST
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResponse<Void> handleValidationExceptions(
            MethodArgumentNotValidException ex, WebRequest request) {
        String path = ((ServletWebRequest) request).getRequest().getRequestURI();
        
        List<ApiResponse.ValidationError> validationErrors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(fieldError -> ApiResponse.ValidationError.builder()
                        .field(fieldError.getField())
                        .message(fieldError.getDefaultMessage())
                        .build())
                .collect(Collectors.toList());
        
        log.warn("Validation failed: {}", validationErrors);
        return ApiResponse.<Void>builder()
                .status("error")
                .message("Data validation error")
                .path(path)
                .errors(validationErrors)
                .build();
    }

    /**
     * Handles unauthorized access exceptions.
     *
     * @param ex the access exception
     * @param request the HTTP request
     * @return an API response with error and status 403 FORBIDDEN
     */
    @ExceptionHandler({AccessDeniedException.class, UnauthorizedAccessException.class})
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ApiResponse<Void> handleAccessDeniedException(
            Exception ex, WebRequest request) {
        String path = ((ServletWebRequest) request).getRequest().getRequestURI();
        log.warn("Access denied: {}", ex.getMessage());
        return ApiResponse.error("Access denied: Authorization required", path);
    }

    /**
     * Handles exceptions related to invalid OAuth tokens.
     *
     * @param ex the OAuth token exception
     * @param request the HTTP request
     * @return an API response with error and status 401 UNAUTHORIZED
     */
    @ExceptionHandler(InvalidOAuthTokenException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ApiResponse<Void> handleInvalidOAuthTokenException(
            InvalidOAuthTokenException ex, WebRequest request) {
        String path = ((ServletWebRequest) request).getRequest().getRequestURI();
        log.warn("Invalid OAuth token: {}", ex.getMessage());
        return ApiResponse.error("Invalid authentication token", path);
    }

    /**
     * Handles exceptions related to duplicate emails.
     *
     * @param ex the existing email exception
     * @param request the HTTP request
     * @return an API response with error and status 409 CONFLICT
     */
    @ExceptionHandler(EmailAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiResponse<Void> handleEmailAlreadyExistsException(
            EmailAlreadyExistsException ex, WebRequest request) {
        String path = ((ServletWebRequest) request).getRequest().getRequestURI();
        log.warn("Email conflict: {}", ex.getMessage());
        return ApiResponse.error(ex.getMessage(), path);
    }

    /**
     * Handles custom validation exceptions.
     *
     * @param ex the validation exception
     * @param request the HTTP request
     * @return an API response with error and status 400 BAD_REQUEST
     */
    @ExceptionHandler(ValidationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResponse<Void> handleValidationException(
            ValidationException ex, WebRequest request) {
        String path = ((ServletWebRequest) request).getRequest().getRequestURI();
        log.warn("Validation error: {}", ex.getMessage());
        return ApiResponse.error(ex.getMessage(), path);
    }

    /**
     * Handles all other unexpected exceptions.
     *
     * @param ex the exception
     * @param request the HTTP request
     * @return an API response with error and status 500 INTERNAL_SERVER_ERROR
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiResponse<Void> handleGlobalException(
            Exception ex, WebRequest request) {
        String path = ((ServletWebRequest) request).getRequest().getRequestURI();
        log.error("An unexpected error occurred", ex);
        return ApiResponse.error("An internal server error occurred. Please try again later.", path);
    }
} 