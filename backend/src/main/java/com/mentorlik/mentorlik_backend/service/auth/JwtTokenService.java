package com.mentorlik.mentorlik_backend.service.auth;

import com.mentorlik.mentorlik_backend.config.JwtConfig;
import com.mentorlik.mentorlik_backend.dto.profile.UserDto;
import com.mentorlik.mentorlik_backend.dto.profile.StudentProfileDto;
import com.mentorlik.mentorlik_backend.exception.ResourceNotFoundException;
import com.mentorlik.mentorlik_backend.repository.StudentProfileRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Сервис для управления JWT токенами.
 * <p>
 * Обеспечивает генерацию, валидацию и обработку JWT токенов для аутентификации пользователей.
 * </p>
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class JwtTokenService {

    private final SecretKey jwtSecretKey;
    private final JwtConfig jwtConfig;
    private final StudentProfileRepository studentRepository;
    
    /**
     * Генерирует JWT токен доступа для пользователя.
     *
     * @param user Пользователь, для которого создается токен
     * @return JWT токен
     */
    public String generateAccessToken(UserDto user) {
        return generateToken(user, jwtConfig.getExpirationTime());
    }
    
    /**
     * Генерирует JWT токен обновления для пользователя.
     *
     * @param user Пользователь, для которого создается токен
     * @return JWT токен обновления
     */
    public String generateRefreshToken(UserDto user) {
        return generateToken(user, jwtConfig.getRefreshExpirationTime());
    }
    
    /**
     * Валидирует токен обновления и возвращает пользователя.
     *
     * @param token Токен обновления для валидации
     * @return DTO пользователя, если токен валиден
     * @throws JwtException если токен невалиден или истек
     */
    public UserDto validateRefreshTokenAndGetUser(String token) {
        Claims claims = validateTokenAndGetClaims(token);
        
        Long userId = Long.valueOf(claims.getSubject());
        String userType = claims.get("userType", String.class);
        
        if ("student".equals(userType)) {
            return studentRepository.findById(userId)
                    .map(student -> {
                        // Преобразуем в конкретную реализацию StudentProfileDto
                        return StudentProfileDto.builder()
                            .id(student.getId())
                            .name(student.getName())
                            .email(student.getEmail())
                            .fieldOfStudy(student.getFieldOfStudy())
                            .educationLevel(student.getEducationLevel())
                            .learningGoals(student.getLearningGoals())
                            .skills(student.getSkills())
                            .isAvailableForMentorship(student.getIsAvailableForMentorship())
                            .build();
                    })
                    .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + userId));
        }
        
        // Здесь можно добавить поддержку других типов пользователей
        
        throw new JwtException("Unsupported user type: " + userType);
    }
    
    /**
     * Получает срок действия токена доступа в миллисекундах.
     *
     * @return срок действия в мс
     */
    public long getAccessTokenExpiration() {
        return jwtConfig.getExpirationTime();
    }
    
    /**
     * Получает срок действия токена обновления в миллисекундах.
     *
     * @return срок действия в мс
     */
    public long getRefreshTokenExpiration() {
        return jwtConfig.getRefreshExpirationTime();
    }
    
    /**
     * Генерирует JWT токен с указанным сроком действия.
     *
     * @param user Пользователь, для которого создается токен
     * @param expirationMs Срок действия в миллисекундах
     * @return JWT токен
     */
    private String generateToken(UserDto user, long expirationMs) {
        Map<String, Object> claims = new HashMap<>();
        
        // Устанавливаем роль пользователя и другие необходимые данные
        claims.put("name", user.getName());
        claims.put("email", user.getEmail());
        
        // Определяем тип пользователя
        String userType = determineUserType(user);
        claims.put("userType", userType);
        
        // Добавляем ID в токен
        String subject = user.getId().toString();
        
        // Генерируем токен
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationMs))
                .signWith(jwtSecretKey, SignatureAlgorithm.HS512)
                .compact();
    }
    
    /**
     * Определяет тип пользователя на основе его DTO.
     *
     * @param user Пользователь
     * @return Тип пользователя (student, mentor и т.д.)
     */
    private String determineUserType(UserDto user) {
        // Логика определения типа пользователя на основе класса или полей
        // Пример: можно проверить наличие специфичных полей
        if (user.getClass().getSimpleName().contains("Student")) {
            return "student";
        } else if (user.getClass().getSimpleName().contains("Mentor")) {
            return "mentor";
        }
        
        // По умолчанию
        return "user";
    }
    
    /**
     * Валидирует токен и извлекает из него данные.
     *
     * @param token JWT токен для валидации
     * @return Claims из токена
     * @throws JwtException если токен невалиден или истек
     */
    private Claims validateTokenAndGetClaims(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(jwtSecretKey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (JwtException e) {
            log.error("JWT token validation failed: {}", e.getMessage());
            throw new JwtException("Invalid JWT token");
        }
    }
} 