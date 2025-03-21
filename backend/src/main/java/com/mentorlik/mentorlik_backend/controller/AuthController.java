package com.mentorlik.mentorlik_backend.controller;

import com.mentorlik.mentorlik_backend.dto.ApiResponse;
import com.mentorlik.mentorlik_backend.dto.auth.AuthRequestDto;
import com.mentorlik.mentorlik_backend.dto.auth.JwtResponseDto;
import com.mentorlik.mentorlik_backend.dto.profile.UserDto;
import com.mentorlik.mentorlik_backend.service.auth.factory.AuthServiceFactory;
import com.mentorlik.mentorlik_backend.service.auth.service.BaseAuthService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * Контроллер для управления эндпоинтами аутентификации и регистрации.
 * <p>
 * Предоставляет эндпоинты для входа и регистрации пользователей
 * разных типов (Admin, Mentor, Student), включая OAuth2-аутентификацию через Google и LinkedIn.
 * </p>
 */
@Slf4j
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthServiceFactory authServiceFactory;

    /**
     * Создает экземпляр {@code AuthController} с указанным {@code AuthServiceFactory}.
     *
     * @param authServiceFactory фабрика для получения соответствующего сервиса аутентификации
     *                           в зависимости от типа пользователя.
     */
    public AuthController(AuthServiceFactory authServiceFactory) {
        this.authServiceFactory = authServiceFactory;
    }

    /**
     * Эндпоинт для традиционного входа пользователя по типу пользователя.
     *
     * @param userType тип пользователя (например, "admin", "mentor", "student")
     * @param authRequest DTO запроса аутентификации, содержащий email и пароль
     * @return {@code ResponseEntity} с ответом на вход, либо данные пользователя при успехе, либо сообщение об ошибке
     */
    @PostMapping("/login/{userType}")
    public ResponseEntity<ApiResponse<UserDto>> login(
            @PathVariable String userType, 
            @Valid @RequestBody AuthRequestDto authRequest) {
        
        log.info("Попытка входа для {} с email: {}", userType, authRequest.getEmail());

        try {
            UserDto user = authServiceFactory.getAuthService(userType).login(authRequest);
            log.info("Успешный вход для {} с email: {}", userType, authRequest.getEmail());
            
            JwtResponseDto jwtResponse = JwtResponseDto.builder()
                    .token("jwt_token_placeholder") // Здесь должен быть реальный JWT-токен
                    .tokenType("Bearer")
                    .build();
            
            ApiResponse<UserDto> response = ApiResponse.<UserDto>builder()
                    .status("success")
                    .data(user)
                    .message("Успешная аутентификация")
                    .build();
            
            return ResponseEntity.ok(response);
        } catch (Exception ex) {
            log.error("Ошибка входа для {} с email {}: {}", userType, authRequest.getEmail(), ex.getMessage());
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.error("Ошибка аутентификации"));
        }
    }

    /**
     * Эндпоинт для OAuth2-входа через Google.
     *
     * @param token токен аутентификации от клиента
     * @param userType тип пользователя (например, "mentor", "student")
     * @return {@code ResponseEntity} с ответом на вход, либо данные пользователя при успехе, либо сообщение об ошибке
     */
    @PostMapping("/oauth2/google")
    public ResponseEntity<ApiResponse<UserDto>> loginWithGoogle(
            @RequestParam String token,
            @RequestParam String userType) {
        
        log.info("Попытка OAuth2-входа через Google для типа: {}", userType);

        try {
            UserDto user = authServiceFactory.getAuthService("google").loginWithToken(token, userType);
            log.info("Успешный OAuth2-вход через Google");
            
            JwtResponseDto jwtResponse = JwtResponseDto.builder()
                    .token("jwt_token_placeholder") // Здесь должен быть реальный JWT-токен
                    .tokenType("Bearer")
                    .build();
            
            ApiResponse<UserDto> response = ApiResponse.<UserDto>builder()
                    .status("success")
                    .data(user)
                    .message("Успешная аутентификация через Google")
                    .build();
            
            return ResponseEntity.ok(response);
        } catch (Exception ex) {
            log.error("Ошибка OAuth2-входа через Google: {}", ex.getMessage());
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.error("Ошибка аутентификации через Google"));
        }
    }

    /**
     * Эндпоинт для OAuth2-входа через LinkedIn.
     *
     * @param token токен аутентификации от клиента
     * @param userType тип пользователя (например, "mentor", "student")
     * @return {@code ResponseEntity} с ответом на вход, либо данные пользователя при успехе, либо сообщение об ошибке
     */
    @PostMapping("/oauth2/linkedin")
    public ResponseEntity<ApiResponse<UserDto>> loginWithLinkedIn(
            @RequestParam String token,
            @RequestParam String userType) {
        
        log.info("Попытка OAuth2-входа через LinkedIn для типа: {}", userType);

        try {
            UserDto user = authServiceFactory.getAuthService("linkedin").loginWithToken(token, userType);
            log.info("Успешный OAuth2-вход через LinkedIn");
            
            JwtResponseDto jwtResponse = JwtResponseDto.builder()
                    .token("jwt_token_placeholder") // Здесь должен быть реальный JWT-токен
                    .tokenType("Bearer")
                    .build();
            
            ApiResponse<UserDto> response = ApiResponse.<UserDto>builder()
                    .status("success")
                    .data(user)
                    .message("Успешная аутентификация через LinkedIn")
                    .build();
            
            return ResponseEntity.ok(response);
        } catch (Exception ex) {
            log.error("Ошибка OAuth2-входа через LinkedIn: {}", ex.getMessage());
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.error("Ошибка аутентификации через LinkedIn"));
        }
    }

    /**
     * Эндпоинт для регистрации пользователя по типу пользователя.
     *
     * @param userType тип пользователя (например, "admin", "mentor", "student")
     * @param userDto DTO данных пользователя, содержащий данные регистрации, такие как email и пароль
     * @return {@code ResponseEntity} с ответом на регистрацию, либо данные созданного пользователя при успехе, либо сообщение об ошибке
     */
    @PostMapping("/register/{userType}")
    public ResponseEntity<ApiResponse<UserDto>> register(
            @PathVariable String userType, 
            @Valid @RequestBody UserDto userDto) {
        
        log.info("Попытка регистрации для {} с email: {}", userType, userDto.getEmail());

        try {
            BaseAuthService<?, ? extends UserDto> authService = authServiceFactory.getAuthService(userType);
            UserDto registeredUser = authService.register(userDto);
            log.info("Успешная регистрация для {} с email: {}", userType, userDto.getEmail());
            
            ApiResponse<UserDto> response = ApiResponse.<UserDto>builder()
                    .status("success")
                    .data(registeredUser)
                    .message("Регистрация прошла успешно")
                    .build();
            
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (IllegalArgumentException ex) {
            log.warn("Ошибка регистрации для {} с email {}: {}", userType, userDto.getEmail(), ex.getMessage());
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error("Неверные данные регистрации: " + ex.getMessage()));
        } catch (Exception ex) {
            log.error("Непредвиденная ошибка при регистрации для {} с email {}: {}", 
                    userType, userDto.getEmail(), ex.getMessage());
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Произошла непредвиденная ошибка при регистрации"));
        }
    }
}
