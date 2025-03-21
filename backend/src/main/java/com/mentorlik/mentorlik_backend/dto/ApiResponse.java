package com.mentorlik.mentorlik_backend.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Унифицированный формат ответа API для всех эндпоинтов.
 * <p>
 * Этот класс обеспечивает консистентную структуру ответов API,
 * включая статус операции, данные, сообщения об ошибках и метаданные.
 * </p>
 *
 * @param <T> тип данных, возвращаемых в ответе
 */
@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {
    
    /**
     * Статус операции: success или error
     */
    private String status;
    
    /**
     * Данные ответа. Присутствуют только при успешном выполнении запроса.
     */
    private T data;
    
    /**
     * Сообщение, описывающее результат операции
     */
    private String message;
    
    /**
     * Временная метка ответа
     */
    private LocalDateTime timestamp;
    
    /**
     * Путь к ресурсу, который обрабатывал запрос
     */
    private String path;
    
    /**
     * Список ошибок валидации, если они есть
     */
    private List<ValidationError> errors;
    
    /**
     * Создает успешный ответ с данными
     *
     * @param data данные для включения в ответ
     * @param <T> тип данных
     * @return объект ApiResponse с статусом success и данными
     */
    public static <T> ApiResponse<T> success(T data) {
        return ApiResponse.<T>builder()
                .status("success")
                .data(data)
                .timestamp(LocalDateTime.now())
                .build();
    }
    
    /**
     * Создает успешный ответ с данными и сообщением
     *
     * @param data данные для включения в ответ
     * @param message сообщение о результате операции
     * @param <T> тип данных
     * @return объект ApiResponse с статусом success, данными и сообщением
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
     * Создает ответ об ошибке с сообщением
     *
     * @param message сообщение об ошибке
     * @param <T> тип данных
     * @return объект ApiResponse с статусом error и сообщением об ошибке
     */
    public static <T> ApiResponse<T> error(String message) {
        return ApiResponse.<T>builder()
                .status("error")
                .message(message)
                .timestamp(LocalDateTime.now())
                .build();
    }
    
    /**
     * Создает ответ об ошибке с сообщением и статусом HTTP
     *
     * @param message сообщение об ошибке
     * @param path путь к ресурсу
     * @param <T> тип данных
     * @return объект ApiResponse с статусом error, сообщением об ошибке и путем
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
     * Создает ответ об ошибке с сообщением и списком ошибок валидации
     *
     * @param message сообщение об ошибке
     * @param errors список ошибок валидации
     * @param <T> тип данных
     * @return объект ApiResponse с статусом error, сообщением и ошибками валидации
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
     * Класс для представления ошибок валидации
     */
    @Data
    @Builder
    public static class ValidationError {
        /**
         * Поле, в котором произошла ошибка
         */
        private String field;
        
        /**
         * Сообщение об ошибке
         */
        private String message;
    }
} 