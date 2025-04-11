package com.mentorlik.mentorlik_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * Data Transfer Object для запроса на создание ментора.
 * <p>
 * Содержит все данные, необходимые для создания нового ментора,
 * включая персональную информацию и профильное фото.
 * </p>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MentorCreationRequest {

    /**
     * Полное имя ментора.
     */
    @NotBlank(message = "Jméno je povinné")
    private String fullname;
    
    /**
     * Для совместимости с передачей данных с FE
     */
    private String name;

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
     * Годы опыта работы ментора (из фронтенда)
     */
    private String experienceYears;

    /**
     * Специализация ментора.
     */
    @NotBlank(message = "Specializace je povinná")
    private String specialization;
    
    /**
     * Специализация ментора (из фронтенда)
     */
    private String expertise;

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
     * Краткая биография ментора (из фронтенда - about)
     */
    private String about;

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
     * Почасовая ставка ментора (из фронтенда)
     */
    private String hourlyRate;

    /**
     * Ссылка на календарь ментора (опционально).
     */
    private String calendar;
    
    /**
     * Доступность ментора
     */
    private Boolean isAvailable;
    
    /**
     * Пароль ментора
     */
    private String password;

    /**
     * Содержимое профильного фото в виде массива байтов.
     */
    @NotNull(message = "Profilová fotografie je povinná")
    private byte[] photo;
    
    /**
     * Содержимое профильного фото в виде строки Base64.
     * Используется при получении фото как часть JSON.
     */
    private String photoBase64;

    /**
     * MIME-тип фотографии (например, "image/jpeg").
     */
    @NotBlank(message = "Typ obsahu fotografie je povinný")
    private String photoContentType;
    
    /**
     * Согласие с условиями
     */
    private Boolean termsConsent;
    
    /**
     * Получает правильное имя из доступных полей
     */
    public String getFullname() {
        if (fullname != null && !fullname.isEmpty()) {
            return fullname;
        }
        return name;
    }
    
    /**
     * Получает правильный опыт из доступных полей
     */
    public String getExperience() {
        if (experience != null && !experience.isEmpty()) {
            return experience;
        }
        return experienceYears;
    }
    
    /**
     * Получает правильную специализацию из доступных полей
     */
    public String getSpecialization() {
        if (specialization != null && !specialization.isEmpty()) {
            return specialization;
        }
        return expertise;
    }
    
    /**
     * Получает правильную биографию из доступных полей
     */
    public String getBio() {
        if (bio != null && !bio.isEmpty()) {
            return bio;
        }
        return about;
    }
    
    /**
     * Получает правильную ставку из доступных полей
     */
    public String getRate() {
        if (rate != null && !rate.isEmpty()) {
            return rate;
        }
        return hourlyRate;
    }
} 