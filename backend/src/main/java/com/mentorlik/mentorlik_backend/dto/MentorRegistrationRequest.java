package com.mentorlik.mentorlik_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * Data Transfer Object для запроса на регистрацию ментора.
 * <p>
 * Содержит все данные, необходимые для регистрации нового ментора,
 * включая персональную информацию и профильное фото.
 * </p>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MentorRegistrationRequest {

    /**
     * Полное имя ментора.
     */
    @NotBlank(message = "Jméno je povinné")
    private String fullname;

    /**
     * Адрес электронной почты ментора.
     */
    @NotBlank(message = "E-mail je povinný")
    @Email(message = "Neplatný formát e-mailu")
    private String email;

    /**
     * Ссылка на профиль LinkedIn (опционально).
     */
    private String linkedin;

    /**
     * Должность ментора.
     */
    @NotBlank(message = "Pozice je povinná")
    private String position;

    /**
     * Компания, в которой работает ментор.
     */
    @NotBlank(message = "Společnost je povinná")
    private String company;

    /**
     * Опыт работы ментора.
     */
    @NotBlank(message = "Zkušenosti jsou povinné")
    private String experience;

    /**
     * Специализация ментора.
     */
    @NotBlank(message = "Specializace je povinná")
    private String specialization;

    /**
     * Ключевые навыки ментора.
     */
    @NotBlank(message = "Dovednosti jsou povinné")
    private String skills;

    /**
     * Краткая биография ментора.
     */
    @NotBlank(message = "Bio je povinné")
    private String bio;

    /**
     * В чем ментор может помочь.
     */
    @NotBlank(message = "Pomoc je povinná")
    private String help;

    /**
     * Почасовая ставка ментора.
     */
    @NotBlank(message = "Sazba je povinná")
    private String rate;

    /**
     * Ссылка на календарь ментора (опционально).
     */
    private String calendar;

    /**
     * Содержимое профильного фото в виде массива байтов.
     */
    @NotNull(message = "Profilová fotografie je povinná")
    private byte[] photo;

    /**
     * MIME-тип фотографии (например, "image/jpeg").
     */
    @NotBlank(message = "Typ obsahu fotografie je povinný")
    private String photoContentType;
} 