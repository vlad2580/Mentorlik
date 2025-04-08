package com.mentorlik.mentorlik_backend.controller;

import com.mentorlik.mentorlik_backend.dto.ApiResponse;
import com.mentorlik.mentorlik_backend.dto.MentorRegistrationRequest;
import com.mentorlik.mentorlik_backend.service.MentorRegistrationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.Valid;
import java.io.IOException;

/**
 * Контроллер для обработки регистрации новых менторов.
 * <p>
 * Предоставляет эндпоинт для регистрации менторов, включая загрузку
 * их профильных фотографий.
 * </p>
 */
@Slf4j
@RestController
@RequestMapping("/api/mentor-registration")
public class MentorRegistrationController {

    private final MentorRegistrationService mentorRegistrationService;

    /**
     * Создает экземпляр контроллера регистрации менторов.
     *
     * @param mentorRegistrationService сервис для регистрации менторов
     */
    public MentorRegistrationController(MentorRegistrationService mentorRegistrationService) {
        this.mentorRegistrationService = mentorRegistrationService;
    }

    /**
     * Регистрирует нового ментора в системе.
     * <p>
     * Принимает данные ментора и его профильную фотографию.
     * </p>
     *
     * @param fullname имя и фамилия ментора
     * @param email адрес электронной почты ментора
     * @param linkedin ссылка на профиль LinkedIn (опционально)
     * @param position должность ментора
     * @param company компания, в которой работает ментор
     * @param experience опыт работы ментора
     * @param specialization специализация ментора
     * @param skills навыки ментора
     * @param bio краткая биография ментора
     * @param help в чем ментор может помочь
     * @param rate почасовая ставка ментора
     * @param photo профильная фотография ментора
     * @param termsConsent согласие с условиями
     * @return ответ с информацией о зарегистрированном менторе
     */
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<Void>> registerMentor(
            @RequestParam("fullname") String fullname,
            @RequestParam("email") String email,
            @RequestParam(value = "linkedin", required = false) String linkedin,
            @RequestParam("position") String position,
            @RequestParam("company") String company,
            @RequestParam("experience") String experience,
            @RequestParam("specialization") String specialization,
            @RequestParam("skills") String skills,
            @RequestParam("bio") String bio,
            @RequestParam("help") String help,
            @RequestParam("rate") String rate,
            @RequestParam(value = "calendar", required = false) String calendar,
            @RequestParam("photo") MultipartFile photo,
            @RequestParam("termsConsent") boolean termsConsent) {
        
        log.info("Получен запрос на регистрацию нового ментора: {}", email);
        
        if (!termsConsent) {
            return ResponseEntity
                    .badRequest()
                    .body(ApiResponse.error("Musíte souhlasit s podmínkami služby"));
        }
        
        try {
            MentorRegistrationRequest request = MentorRegistrationRequest.builder()
                    .fullname(fullname)
                    .email(email)
                    .linkedin(linkedin)
                    .position(position)
                    .company(company)
                    .experience(experience)
                    .specialization(specialization)
                    .skills(skills)
                    .bio(bio)
                    .help(help)
                    .rate(rate)
                    .calendar(calendar)
                    .photo(photo.getBytes())
                    .photoContentType(photo.getContentType())
                    .build();
            
            mentorRegistrationService.registerMentor(request, photo);
            
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(ApiResponse.success(null, "Žádost o mentorství byla úspěšně odeslána"));
            
        } catch (IOException e) {
            log.error("Ошибка при обработке фотографии ментора", e);
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Chyba při zpracování fotografie. Prosím, zkuste to znovu."));
        } catch (Exception e) {
            log.error("Ошибка при регистрации ментора", e);
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Došlo k neočekávané chybě. Prosím, zkuste to znovu později."));
        }
    }
} 