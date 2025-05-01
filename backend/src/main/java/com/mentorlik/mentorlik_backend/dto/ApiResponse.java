package com.mentorlik.mentorlik_backend.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Standardized API response wrapper for all endpoints.
 *
 * <p>This class provides a consistent structure for API responses,
 * including operation status, returned data, user-facing messages,
 * and optional metadata such as timestamp and path.</p>
 *
 * @param <T> the type of the response payload
 */
@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {

    /**
     * Operation status: "success" or "error".
     */
    private String status;

    /**
     * Payload of the response; only present on success.
     */
    private T data;

    /**
     * Descriptive message explaining the result of the operation.
     */
    private String message;

    /**
     * Timestamp when the response was generated.
     */
    private LocalDateTime timestamp;

    /**
     * The request path that produced this response.
     */
    private String path;

    /**
     * List of validation errors, if applicable.
     */
    private List<ValidationError> errors;

    /**
     * Virtual boolean flag mapped in JSON as "ok": true if status is "success".
     *
     * @return true if this response indicates success
     */
    @JsonProperty("ok")
    public boolean isOk() {
        return "success".equalsIgnoreCase(this.status);
    }

    /**
     * Creates a successful response containing data.
     *
     * @param data the payload to include
     * @param <T>  payload type
     * @return ApiResponse instance with status="success" and provided data
     */
    public static <T> ApiResponse<T> success(T data) {
        return ApiResponse.<T>builder()
                .status("success")
                .data(data)
                .timestamp(LocalDateTime.now())
                .build();
    }

    /**
     * Creates a successful response containing data and a message.
     *
     * @param data    the payload to include
     * @param message descriptive message
     * @param <T>     payload type
     * @return ApiResponse with status="success", data, and message
     */
    public static <T> ApiResponse<T> success(T data, String message) {
        return ApiResponse.<T>builder()
                .status("success")
                .data(data)
                .message(message)
                .timestamp(LocalDateTime.now())
                .build();
    }

    /**
     * Creates an error response with a message.
     *
     * @param message error description
     * @param <T>     payload type
     * @return ApiResponse with status="error" and provided message
     */
    public static <T> ApiResponse<T> error(String message) {
        return ApiResponse.<T>builder()
                .status("error")
                .message(message)
                .timestamp(LocalDateTime.now())
                .build();
    }

    /**
     * Creates an error response with a message and request path.
     *
     * @param message error description
     * @param path    request path
     * @param <T>     payload type
     * @return ApiResponse with status="error", message, and path
     */
    public static <T> ApiResponse<T> error(String message, String path) {
        return ApiResponse.<T>builder()
                .status("error")
                .message(message)
                .path(path)
                .timestamp(LocalDateTime.now())
                .build();
    }

    /**
     * Creates an error response with a message and validation errors.
     *
     * @param message error description
     * @param errors  list of validation errors
     * @param <T>     payload type
     * @return ApiResponse with status="error", message, and validation errors
     */
    public static <T> ApiResponse<T> error(String message, List<ValidationError> errors) {
        return ApiResponse.<T>builder()
                .status("error")
                .message(message)
                .errors(errors)
                .timestamp(LocalDateTime.now())
                .build();
    }

    /**
     * Inner class representing a validation error on a specific field.
     */
    @Data
    @Builder
    public static class ValidationError {
        /**
         * Name of the field with the validation error.
         */
        private String field;

        /**
         * Description of the validation error.
         */
        private String message;
    }
}