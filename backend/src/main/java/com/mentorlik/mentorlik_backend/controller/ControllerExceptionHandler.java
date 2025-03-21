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
 * Обработчик исключений для всех контроллеров API.
 * <p>
 * Этот класс перехватывает исключения, возникающие в контроллерах,
 * и преобразует их в унифицированные ответы API в формате {@link ApiResponse}.
 * </p>
 */
@Slf4j
@RestControllerAdvice
public class ControllerExceptionHandler {

    /**
     * Обрабатывает исключения типа {@link ResourceNotFoundException}.
     *
     * @param ex исключение о ненайденном ресурсе
     * @param request HTTP-запрос
     * @return ответ API с ошибкой и статусом 404 NOT_FOUND
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
     * Обрабатывает исключения валидации запроса.
     *
     * @param ex исключение валидации аргументов метода
     * @param request HTTP-запрос
     * @return ответ API с ошибками валидации и статусом 400 BAD_REQUEST
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
                .message("Ошибка валидации данных")
                .path(path)
                .errors(validationErrors)
                .build();
    }

    /**
     * Обрабатывает исключения неавторизованного доступа.
     *
     * @param ex исключение доступа
     * @param request HTTP-запрос
     * @return ответ API с ошибкой и статусом 403 FORBIDDEN
     */
    @ExceptionHandler({AccessDeniedException.class, UnauthorizedAccessException.class})
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ApiResponse<Void> handleAccessDeniedException(
            Exception ex, WebRequest request) {
        String path = ((ServletWebRequest) request).getRequest().getRequestURI();
        log.warn("Access denied: {}", ex.getMessage());
        return ApiResponse.error("Доступ запрещен: необходима авторизация", path);
    }

    /**
     * Обрабатывает исключения, связанные с неверным OAuth-токеном.
     *
     * @param ex исключение OAuth-токена
     * @param request HTTP-запрос
     * @return ответ API с ошибкой и статусом 401 UNAUTHORIZED
     */
    @ExceptionHandler(InvalidOAuthTokenException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ApiResponse<Void> handleInvalidOAuthTokenException(
            InvalidOAuthTokenException ex, WebRequest request) {
        String path = ((ServletWebRequest) request).getRequest().getRequestURI();
        log.warn("Invalid OAuth token: {}", ex.getMessage());
        return ApiResponse.error("Недействительный токен аутентификации", path);
    }

    /**
     * Обрабатывает исключения, связанные с повторяющимся email.
     *
     * @param ex исключение существующего email
     * @param request HTTP-запрос
     * @return ответ API с ошибкой и статусом 409 CONFLICT
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
     * Обрабатывает исключения пользовательской валидации.
     *
     * @param ex исключение валидации
     * @param request HTTP-запрос
     * @return ответ API с ошибкой и статусом 400 BAD_REQUEST
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
     * Обрабатывает все остальные непредвиденные исключения.
     *
     * @param ex исключение
     * @param request HTTP-запрос
     * @return ответ API с ошибкой и статусом 500 INTERNAL_SERVER_ERROR
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiResponse<Void> handleGlobalException(
            Exception ex, WebRequest request) {
        String path = ((ServletWebRequest) request).getRequest().getRequestURI();
        log.error("An unexpected error occurred", ex);
        return ApiResponse.error("Произошла внутренняя ошибка сервера. Пожалуйста, попробуйте позже.", path);
    }
} 