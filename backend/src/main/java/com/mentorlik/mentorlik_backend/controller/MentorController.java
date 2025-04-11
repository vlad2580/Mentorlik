package com.mentorlik.mentorlik_backend.controller;

import com.mentorlik.mentorlik_backend.dto.ApiResponse;
import com.mentorlik.mentorlik_backend.dto.profile.MentorProfileDto;
import com.mentorlik.mentorlik_backend.service.MentorService;
import com.mentorlik.mentorlik_backend.service.MentorSearchService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * Контроллер для управления данными менторов.
 * <p>
 * Предоставляет эндпоинты для получения, создания, обновления и удаления
 * информации о менторах в системе.
 * </p>
 */
@Slf4j
@RestController
@RequestMapping("/api/mentors")
public class MentorController {

    private final MentorService mentorService;
    private final MentorSearchService mentorSearchService;

    /**
     * Создает экземпляр контроллера менторов.
     *
     * @param mentorService сервис для работы с данными менторов
     * @param mentorSearchService сервис для расширенного поиска менторов
     */
    public MentorController(MentorService mentorService, MentorSearchService mentorSearchService) {
        this.mentorService = mentorService;
        this.mentorSearchService = mentorSearchService;
    }

    /**
     * Получает список всех менторов.
     *
     * @return ответ со списком всех менторов
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<MentorProfileDto>>> getAllMentors() {
        log.info("Запрос на получение всех менторов");
        List<MentorProfileDto> mentors = mentorService.getAllMentors();
        return ResponseEntity.ok(ApiResponse.success(mentors));
    }

    /**
     * Получает ментора по ID.
     *
     * @param id идентификатор ментора
     * @return ответ с информацией о менторе
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<MentorProfileDto>> getMentorById(@PathVariable Long id) {
        log.info("Запрос на получение ментора с ID: {}", id);
        MentorProfileDto mentor = mentorService.getMentorById(id);
        return ResponseEntity.ok(ApiResponse.success(mentor));
    }

    /**
     * Обновляет данные ментора.
     *
     * @param id идентификатор ментора
     * @param mentorDto данные ментора для обновления
     * @return ответ с обновленной информацией о менторе
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('MENTOR') and @mentorSecurityService.isMentorOwner(authentication, #id)")
    public ResponseEntity<ApiResponse<MentorProfileDto>> updateMentor(
            @PathVariable Long id,
            @Valid @RequestBody MentorProfileDto mentorDto) {
        log.info("Запрос на обновление ментора с ID: {}", id);
        MentorProfileDto updatedMentor = mentorService.updateMentor(id, mentorDto);
        return ResponseEntity.ok(ApiResponse.success(updatedMentor, "Данные ментора успешно обновлены"));
    }

    /**
     * Удаляет ментора из системы.
     *
     * @param id идентификатор ментора
     * @return ответ с подтверждением удаления
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or (hasRole('MENTOR') and @mentorSecurityService.isMentorOwner(authentication, #id))")
    public ResponseEntity<ApiResponse<Void>> deleteMentor(@PathVariable Long id) {
        log.info("Запрос на удаление ментора с ID: {}", id);
        mentorService.deleteMentor(id);
        return ResponseEntity.ok(ApiResponse.success(null, "Ментор успешно удален"));
    }

    /**
     * Ищет менторов по заданным критериям (базовый поиск).
     *
     * @param query строка поиска (имя, навыки и т.д.)
     * @return ответ со списком найденных менторов
     */
    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<MentorProfileDto>>> searchMentors(@RequestParam String query) {
        log.info("Запрос на поиск менторов по запросу: {}", query);
        List<MentorProfileDto> mentors = mentorService.searchMentors(query);
        return ResponseEntity.ok(ApiResponse.success(mentors));
    }

    /**
     * Создает нового ментора.
     *
     * @param mentorDto данные нового ментора
     * @return ответ с информацией о созданном менторе
     */
    @PostMapping
    public ResponseEntity<ApiResponse<MentorProfileDto>> createMentor(@Valid @RequestBody MentorProfileDto mentorDto) {
        log.info("Запрос на создание нового ментора");
        MentorProfileDto createdMentor = mentorService.createMentor(mentorDto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success(createdMentor, "Ментор успешно создан"));
    }
} 