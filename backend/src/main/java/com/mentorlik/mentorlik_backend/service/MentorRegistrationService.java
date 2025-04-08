package com.mentorlik.mentorlik_backend.service;

import com.mentorlik.mentorlik_backend.dto.MentorRegistrationRequest;
import com.mentorlik.mentorlik_backend.model.MentorRegistration;
import com.mentorlik.mentorlik_backend.repository.MentorRegistrationRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

/**
 * Сервис для обработки регистрационных заявок менторов.
 * <p>
 * Отвечает за валидацию, сохранение и обработку регистрационных
 * заявок от менторов.
 * </p>
 */
@Slf4j
@Service
public class MentorRegistrationService {

    private final MentorRegistrationRepository mentorRegistrationRepository;
    private final UserService userService;

    private static final List<String> ALLOWED_CONTENT_TYPES = List.of(
            "image/jpeg",
            "image/png",
            "image/jpg"
    );

    /**
     * Создает экземпляр сервиса регистрации менторов.
     *
     * @param mentorRegistrationRepository репозиторий для сохранения заявок
     * @param userService сервис для работы с пользователями
     */
    public MentorRegistrationService(
            MentorRegistrationRepository mentorRegistrationRepository,
            UserService userService) {
        this.mentorRegistrationRepository = mentorRegistrationRepository;
        this.userService = userService;
    }

    /**
     * Обрабатывает и сохраняет заявку на регистрацию ментора.
     * <p>
     * Проверяет данные заявки, сохраняет их в базу данных и отправляет
     * уведомление администраторам о новой заявке.
     * </p>
     *
     * @param request данные заявки
     * @param photo фотография ментора
     * @return сохраненную заявку
     * @throws IllegalArgumentException если данные заявки некорректны
     */
    @Transactional
    public MentorRegistration registerMentor(MentorRegistrationRequest request, MultipartFile photo) {
        log.info("Обработка новой заявки на регистрацию ментора: {}", request.getEmail());

        // Проверка на существующего пользователя с таким email
        if (userService.existsByEmail(request.getEmail())) {
            log.warn("Пользователь с email {} уже существует", request.getEmail());
            throw new IllegalArgumentException("User with email " + request.getEmail() + " already exists");
        }

        // Проверка типа содержимого фото
        validatePhotoContentType(photo);

        // Создание и сохранение заявки
        MentorRegistration registration = createMentorRegistration(request, photo);
        MentorRegistration savedRegistration = mentorRegistrationRepository.save(registration);

        // Здесь можно добавить логику отправки уведомления администраторам
        // о новой заявке, например, через EmailService

        log.info("Заявка на регистрацию ментора успешно сохранена: {}", savedRegistration.getId());
        return savedRegistration;
    }

    /**
     * Создает объект заявки на регистрацию ментора из данных запроса.
     *
     * @param request данные запроса
     * @param photo фотография ментора
     * @return объект заявки
     */
    private MentorRegistration createMentorRegistration(MentorRegistrationRequest request, MultipartFile photo) {
        try {
            return MentorRegistration.builder()
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
                    .photo(photo.getBytes())
                    .photoContentType(photo.getContentType())
                    .applicationDate(LocalDateTime.now())
                    .status("PENDING")
                    .build();
        } catch (Exception e) {
            log.error("Error creating mentor registration", e);
            throw new RuntimeException("Error processing mentor registration", e);
        }
    }

    /**
     * Проверяет соответствие типа содержимого фото допустимым значениям.
     *
     * @param photo фотография ментора
     * @throws IllegalArgumentException если тип содержимого недопустим
     */
    private void validatePhotoContentType(MultipartFile photo) {
        String contentType = photo.getContentType();
        if (contentType == null || !ALLOWED_CONTENT_TYPES.contains(contentType)) {
            throw new IllegalArgumentException("Invalid photo format. Allowed formats: JPEG, PNG");
        }
    }
} 