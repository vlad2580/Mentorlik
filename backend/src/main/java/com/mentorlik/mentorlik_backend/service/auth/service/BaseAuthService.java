package com.mentorlik.mentorlik_backend.service.auth.service;

import com.mentorlik.mentorlik_backend.dto.auth.AuthRequestDto;
import com.mentorlik.mentorlik_backend.dto.profile.UserDto;
import com.mentorlik.mentorlik_backend.model.User;

/**
 * Базовый интерфейс для сервисов аутентификации.
 * <p>
 * Определяет общие методы, которые должны реализовать все сервисы аутентификации,
 * включая традиционный вход по паролю и OAuth2-аутентификацию.
 * </p>
 *
 * @param <T> тип пользовательской модели (например, AdminProfile, MentorProfile, StudentProfile)
 * @param <U> тип пользовательского DTO (например, AdminProfileDto, MentorProfileDto, StudentProfileDto)
 */
public interface BaseAuthService<T extends User, U extends UserDto> {

    /**
     * Выполняет вход пользователя с использованием email и пароля.
     *
     * @param authRequest DTO запроса аутентификации, содержащий email и пароль
     * @return DTO пользователя после успешной аутентификации
     * @throws IllegalArgumentException если данные аутентификации неверны
     */
    U login(AuthRequestDto authRequest);

    /**
     * Выполняет вход с использованием токена OAuth2.
     *
     * @param token токен для OAuth2-аутентификации 
     * @param userType тип пользователя (например, "mentor", "student")
     * @return DTO пользователя после успешной аутентификации
     */
    U loginWithToken(String token, String userType);

    /**
     * Регистрирует нового пользователя в системе.
     *
     * @param userDto DTO пользователя, содержащий данные регистрации
     * @return DTO зарегистрированного пользователя
     * @throws IllegalArgumentException если данные регистрации неверны
     */
    U register(UserDto userDto);
}