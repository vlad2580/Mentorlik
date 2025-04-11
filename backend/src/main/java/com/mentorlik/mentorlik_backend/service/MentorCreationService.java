package com.mentorlik.mentorlik_backend.service;

import com.mentorlik.mentorlik_backend.dto.MentorCreationRequest;
import com.mentorlik.mentorlik_backend.model.MentorCreation;
import com.mentorlik.mentorlik_backend.repository.MentorCreationRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Base64;

/**
 * Сервис для обработки создания менторов.
 * <p>
 * Отвечает за валидацию, сохранение и обработку заявок
 * на создание менторов.
 * </p>
 */
@Slf4j
@Service
public class MentorCreationService {

    private final MentorCreationRepository mentorCreationRepository;
    private final UserService userService;

    private static final List<String> ALLOWED_CONTENT_TYPES = List.of(
            "image/jpeg",
            "image/png",
            "image/jpg"
    );

    /**
     * Создает экземпляр сервиса создания менторов.
     *
     * @param mentorCreationRepository репозиторий для сохранения заявок
     * @param userService сервис для работы с пользователями
     */
    public MentorCreationService(
            MentorCreationRepository mentorCreationRepository,
            UserService userService) {
        this.mentorCreationRepository = mentorCreationRepository;
        this.userService = userService;
    }

    /**
     * Возвращает все заявки на создание менторов.
     *
     * @return список всех заявок на создание менторов
     */
    @Transactional(readOnly = true)
    public List<MentorCreation> getAllMentorCreations() {
        log.info("Retrieving all mentor creation requests");
        return mentorCreationRepository.findAll();
    }

    /**
     * Возвращает заявку на создание ментора по ID.
     *
     * @param id идентификатор заявки
     * @return заявка на создание ментора
     * @throws RuntimeException если заявка не найдена
     */
    @Transactional(readOnly = true)
    public MentorCreation getMentorCreationById(Long id) {
        log.info("Retrieving mentor creation request with ID: {}", id);
        return mentorCreationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Mentor creation request not found with ID: " + id));
    }

    /**
     * Обрабатывает и сохраняет заявку на создание ментора.
     * <p>
     * Проверяет данные заявки, сохраняет их в базу данных и отправляет
     * уведомление администраторам о новом менторе.
     * </p>
     *
     * @param request данные заявки
     * @return сохраненную заявку
     * @throws IllegalArgumentException если данные заявки некорректны
     */
    @Transactional
    public MentorCreation createMentor(MentorCreationRequest request) {
        log.info("Обработка новой заявки на создание ментора: {}", request.getEmail());

        // Проверка на существующего пользователя с таким email
        if (userService.existsByEmail(request.getEmail())) {
            log.warn("Пользователь с email {} уже существует", request.getEmail());
            throw new IllegalArgumentException("User with email " + request.getEmail() + " already exists");
        }

        // Проверка типа содержимого фото
        validatePhotoContentType(request.getPhotoContentType());

        // Создание и сохранение заявки
        MentorCreation creation = createMentorCreation(request);
        MentorCreation savedCreation = mentorCreationRepository.save(creation);

        // Здесь можно добавить логику отправки уведомления администраторам
        // о новой заявке, например, через EmailService

        log.info("Заявка на создание ментора успешно сохранена: {}", savedCreation.getId());
        return savedCreation;
    }

    /**
     * Создает объект заявки на создание ментора из данных запроса.
     *
     * @param request данные запроса
     * @return объект заявки
     */
    private MentorCreation createMentorCreation(MentorCreationRequest request) {
        try {
            return MentorCreation.builder()
                    .fullname(request.getFullname())
                    .email(request.getEmail())
                    .linkedin(request.getLinkedin())
                    .position(request.getPosition())
                    .company(request.getCompany())
                    .experience(request.getExperience())
                    .specialization(request.getSpecialization())
                    .skills(request.getSkills())
                    .bio(request.getBio())
                    .help(request.getHelp())
                    .rate(request.getRate())
                    .calendar(request.getCalendar())
                    .photo(request.getPhoto())
                    .photoContentType(request.getPhotoContentType())
                    .applicationDate(LocalDateTime.now())
                    .status("PENDING")
                    .build();
        } catch (Exception e) {
            log.error("Error creating mentor", e);
            throw new RuntimeException("Error processing mentor creation", e);
        }
    }

    /**
     * Проверяет соответствие типа содержимого фото допустимым значениям.
     *
     * @param contentType тип содержимого фото
     * @throws IllegalArgumentException если тип содержимого недопустим
     */
    private void validatePhotoContentType(String contentType) {
        if (contentType == null || !ALLOWED_CONTENT_TYPES.contains(contentType)) {
            throw new IllegalArgumentException("Invalid photo format. Allowed formats: JPEG, PNG");
        }
    }
} 