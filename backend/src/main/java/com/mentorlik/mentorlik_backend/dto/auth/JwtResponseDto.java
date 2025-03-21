package com.mentorlik.mentorlik_backend.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO для передачи JWT-токена клиенту после успешной аутентификации.
 * <p>
 * Содержит информацию о токене, включая сам токен и его тип (обычно "Bearer").
 * </p>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JwtResponseDto {
    
    /**
     * JWT-токен доступа.
     */
    private String token;
    
    /**
     * Тип токена, обычно "Bearer".
     */
    private String tokenType;
} 