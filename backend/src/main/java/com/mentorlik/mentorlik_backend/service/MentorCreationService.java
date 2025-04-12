package com.mentorlik.mentorlik_backend.service;

import com.mentorlik.mentorlik_backend.dto.MentorCreationRequest;
import com.mentorlik.mentorlik_backend.model.MentorCreation;
import com.mentorlik.mentorlik_backend.repository.MentorCreationRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Service for processing mentor creation.
 * <p>
 * Responsible for validation, saving, and processing
 * mentor creation requests.
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
     * Creates an instance of the mentor creation service.
     *
     * @param mentorCreationRepository repository for saving requests
     * @param userService service for working with users
     */
    public MentorCreationService(
            MentorCreationRepository mentorCreationRepository,
            UserService userService) {
        this.mentorCreationRepository = mentorCreationRepository;
        this.userService = userService;
    }

    /**
     * Returns all mentor creation requests.
     *
     * @return list of all mentor creation requests
     */
    @Transactional(readOnly = true)
    public List<MentorCreation> getAllMentorCreations() {
        log.info("Retrieving all mentor creation requests");
        return mentorCreationRepository.findAll();
    }

    /**
     * Returns a mentor creation request by ID.
     *
     * @param id request identifier
     * @return mentor creation request
     * @throws RuntimeException if the request is not found
     */
    @Transactional(readOnly = true)
    public MentorCreation getMentorCreationById(Long id) {
        log.info("Retrieving mentor creation request with ID: {}", id);
        return mentorCreationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Mentor creation request not found with ID: " + id));
    }

    /**
     * Processes and saves a mentor creation request.
     * <p>
     * Checks the request data, saves it to the database, and sends
     * a notification to administrators about the new mentor.
     * </p>
     *
     * @param request request data
     * @return saved request
     * @throws IllegalArgumentException if the request data is invalid
     */
    @Transactional
    public MentorCreation createMentor(MentorCreationRequest request) {
        log.info("Processing new mentor creation request: {}", request.getEmail());

        // Check for existing user with this email
        if (userService.existsByEmail(request.getEmail())) {
            log.warn("User with email {} already exists", request.getEmail());
            throw new IllegalArgumentException("User with email " + request.getEmail() + " already exists");
        }

        // Check photo content type
        validatePhotoContentType(request.getPhotoContentType());

        // Create and save request
        MentorCreation creation = createMentorCreation(request);
        MentorCreation savedCreation = mentorCreationRepository.save(creation);

        // Here you can add logic to send notifications to administrators
        // about the new request, for example, through EmailService

        log.info("Mentor creation request successfully saved: {}", savedCreation.getId());
        return savedCreation;
    }

    /**
     * Creates a mentor creation request object from request data.
     *
     * @param request request data
     * @return request object
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
     * Checks if the photo content type matches allowed values.
     *
     * @param contentType photo content type
     * @throws IllegalArgumentException if the content type is not allowed
     */
    private void validatePhotoContentType(String contentType) {
        if (contentType == null || !ALLOWED_CONTENT_TYPES.contains(contentType)) {
            throw new IllegalArgumentException("Invalid photo format. Allowed formats: JPEG, PNG");
        }
    }
} 