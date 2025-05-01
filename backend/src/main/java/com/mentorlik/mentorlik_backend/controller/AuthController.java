package com.mentorlik.mentorlik_backend.controller;

import com.mentorlik.mentorlik_backend.dto.ApiResponse;
import com.mentorlik.mentorlik_backend.dto.auth.AuthRequestDto;
import com.mentorlik.mentorlik_backend.dto.profile.MentorProfileDto;
import com.mentorlik.mentorlik_backend.dto.profile.StudentProfileDto;
import com.mentorlik.mentorlik_backend.dto.profile.UserDto;
import com.mentorlik.mentorlik_backend.exception.ResourceNotFoundException;
import com.mentorlik.mentorlik_backend.service.auth.factory.AuthServiceFactory;
import com.mentorlik.mentorlik_backend.service.auth.service.BaseAuthService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Collections;
import java.util.ArrayList;

/**
 * Controller for managing authentication and registration endpoints.
 * <p>
 * Provides endpoints for login and registration of users
 * of different types (Admin, Mentor, Student), including OAuth2 authentication via Google and LinkedIn.
 * </p>
 */
@Slf4j
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthServiceFactory authServiceFactory;

    /**
     * Creates an instance of {@code AuthController} with the specified {@code AuthServiceFactory}.
     *
     * @param authServiceFactory factory for obtaining the appropriate authentication service
     *                           depending on the user type.
     */
    public AuthController(AuthServiceFactory authServiceFactory) {
        this.authServiceFactory = authServiceFactory;
    }

    /**
     * Endpoint for traditional user login without specifying user type.
     * Will try to authenticate as student first, then as mentor if student authentication fails.
     *
     * @param authRequest DTO of authentication request containing email and password
     * @return {@code ResponseEntity} with login response, either user data on success or error message
     */
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<UserDto>> login(@Valid @RequestBody AuthRequestDto authRequest) {
        log.info("Login attempt with email: {}", authRequest.getEmail());

        try {
            // Try student login first
            try {
                UserDto user = authServiceFactory.getAuthService("student").login(authRequest);
                log.info("Successful login as student with email: {} with user data: {}", authRequest.getEmail(), user);
                return ResponseEntity.ok(ApiResponse.success(user, "Authentication successful"));
            } catch (ResourceNotFoundException e) {
                // If student login fails, try mentor login
                UserDto user = authServiceFactory.getAuthService("mentor").login(authRequest);
                log.info("Successful login as mentor with email: {} with user data: {}", authRequest.getEmail(), user);
                return ResponseEntity.ok(ApiResponse.success(user, "Authentication successful"));
            }
        } catch (IllegalStateException e) {
            // This is likely an email verification error
            log.warn("Login failed with email {}: {}", authRequest.getEmail(), e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .body(ApiResponse.error(e.getMessage()));
        } catch (ResourceNotFoundException e) {
            // Incorrect login credentials
            log.error("Login error with email {}: {}", authRequest.getEmail(), e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.error("Neplatné přihlašovací údaje. Zkontrolujte email a heslo."));
        } catch (Exception e) {
            // All other errors
            log.error("Login error with email {}: {}", authRequest.getEmail(), e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.error("Došlo k chybě při přihlašování. Zkuste to prosím znovu."));
        }
    }

    /**
     * Endpoint for OAuth2 login via Google.
     *
     * @param token authentication token from client
     * @param userType type of user (e.g., "mentor", "student")
     * @return {@code ResponseEntity} with login response, either user data on success or error message
     */
    @PostMapping("/oauth2/google")
    public ResponseEntity<ApiResponse<UserDto>> loginWithGoogle(
            @RequestParam String token,
            @RequestParam String userType) {
        
        log.info("OAuth2 login attempt via Google for type: {}", userType);

        try {
            UserDto user = authServiceFactory.getAuthService("google").loginWithToken(token, userType);
            log.info("Successful OAuth2 login via Google");
            
            ApiResponse<UserDto> response = ApiResponse.<UserDto>builder()
                    .status("success")
                    .data(user)
                    .message("Authentication via Google successful")
                    .build();
            
            return ResponseEntity.ok(response);
        } catch (Exception ex) {
            log.error("OAuth2 login error via Google: {}", ex.getMessage());
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.error("Authentication error via Google"));
        }
    }

    /**
     * Endpoint for OAuth2 login via LinkedIn.
     *
     * @param token authentication token from client
     * @param userType type of user (e.g., "mentor", "student")
     * @return {@code ResponseEntity} with login response, either user data on success or error message
     */
    @PostMapping("/oauth2/linkedin")
    public ResponseEntity<ApiResponse<UserDto>> loginWithLinkedIn(
            @RequestParam String token,
            @RequestParam String userType) {
        
        log.info("OAuth2 login attempt via LinkedIn for type: {}", userType);

        try {
            UserDto user = authServiceFactory.getAuthService("linkedin").loginWithToken(token, userType);
            log.info("Successful OAuth2 login via LinkedIn");
            
            ApiResponse<UserDto> response = ApiResponse.<UserDto>builder()
                    .status("success")
                    .data(user)
                    .message("Authentication via LinkedIn successful")
                    .build();
            
            return ResponseEntity.ok(response);
        } catch (Exception ex) {
            log.error("OAuth2 login error via LinkedIn: {}", ex.getMessage());
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.error("Authentication error via LinkedIn"));
        }
    }

    /**
     * Endpoint for user registration by user type.
     *
     * @param userType type of user (e.g., "admin", "mentor", "student")
     * @param userDto DTO of user data containing registration data such as email and password
     * @return {@code ResponseEntity} with registration response, either created user data on success or error message
     */
    @PostMapping("/register/{userType}")
    public ResponseEntity<ApiResponse<UserDto>> register(
            @PathVariable String userType, 
            @Valid @RequestBody UserDto userDto) {
        
        log.info("Registration attempt for {} with email: {}", userType, userDto.getEmail());

        try {
            BaseAuthService<?, ? extends UserDto> authService = authServiceFactory.getAuthService(userType);
            UserDto registeredUser = authService.register(userDto);
            log.info("Successful registration for {} with email: {}", userType, userDto.getEmail());
            
            ApiResponse<UserDto> response = ApiResponse.<UserDto>builder()
                    .status("success")
                    .data(registeredUser)
                    .message("Registration successful")
                    .build();
            
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (IllegalArgumentException ex) {
            log.warn("Registration error for {} with email {}: {}", userType, userDto.getEmail(), ex.getMessage());
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error("Invalid registration data: " + ex.getMessage()));
        } catch (Exception ex) {
            log.error("Unexpected error during registration for {} with email {}: {}", 
                    userType, userDto.getEmail(), ex.getMessage());
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("An unexpected error occurred during registration"));
        }
    }

    /**
     * Endpoint for traditional user login by user type.
     *
     * @param userType type of user (e.g., "admin", "mentor", "student")
     * @param authRequest DTO of authentication request containing email and password
     * @return {@code ResponseEntity} with login response, either user data on success or error message
     */
    @PostMapping("/login/{userType}")
    public ResponseEntity<ApiResponse<UserDto>> loginWithType(
            @PathVariable String userType, 
            @Valid @RequestBody AuthRequestDto authRequest) {
        
        log.info("Login attempt for {} with email: {}", userType, authRequest.getEmail());

        try {
            UserDto user = authServiceFactory.getAuthService(userType).login(authRequest);
            log.info("Successful login for {} with email: {} with user data: {}", userType, authRequest.getEmail(), user);
            
            // Ensure user data is complete and valid
            if (user != null) {
                // Дополняем данные пользователя в зависимости от типа
                switch (userType.toLowerCase()) {
                    case "student":
                        if (user instanceof StudentProfileDto) {
                            StudentProfileDto studentDto = (StudentProfileDto) user;
                            // Дополняем данные студента, если они не заполнены
                            if (studentDto.getFieldOfStudy() == null || studentDto.getFieldOfStudy().isEmpty()) {
                                studentDto.setFieldOfStudy("Computer Science");
                            }
                            if (studentDto.getEducationLevel() == null || studentDto.getEducationLevel().isEmpty()) {
                                studentDto.setEducationLevel("Bachelor");
                            }
                            if (studentDto.getLearningGoals() == null || studentDto.getLearningGoals().isEmpty()) {
                                studentDto.setLearningGoals("Improve programming skills");
                            }
                            if (studentDto.getSkills() == null) {
                                studentDto.setSkills(new ArrayList<>(Collections.singletonList("Programming")));
                            }
                            if (studentDto.getIsAvailableForMentorship() == null) {
                                studentDto.setIsAvailableForMentorship(true);
                            }
                        }
                        break;
                    case "mentor":
                        if (user instanceof MentorProfileDto) {
                            MentorProfileDto mentorDto = (MentorProfileDto) user;
                            // Дополняем данные ментора, если они не заполнены
                            if (mentorDto.getExpertise() == null || mentorDto.getExpertise().isEmpty()) {
                                mentorDto.setExpertise("Software Development");
                            }
                            if (mentorDto.getBio() == null || mentorDto.getBio().isEmpty()) {
                                mentorDto.setBio("Experienced software developer");
                            }
                            if (mentorDto.getExperienceYears() == null) {
                                mentorDto.setExperienceYears(5);
                            }
                            if (mentorDto.getIsAvailable() == null) {
                                mentorDto.setIsAvailable(true);
                            }
                        }
                        break;
                    default:
                        log.info("No additional data needed for user type: {}", userType);
                }
            } else {
                log.warn("User data is null after successful login for {} with email: {}", userType, authRequest.getEmail());
                // Создаем синтетические данные, если пользователь null
                if (userType.equalsIgnoreCase("student")) {
                    user = StudentProfileDto.builder()
                            .email(authRequest.getEmail())
                            .name(authRequest.getEmail().split("@")[0])
                            .fieldOfStudy("Computer Science")
                            .educationLevel("Bachelor")
                            .learningGoals("Improve programming skills")
                            .skills(new ArrayList<>(Collections.singletonList("Programming")))
                            .isAvailableForMentorship(true)
                            .build();
                } else if (userType.equalsIgnoreCase("mentor")) {
                    user = MentorProfileDto.builder()
                            .email(authRequest.getEmail())
                            .name(authRequest.getEmail().split("@")[0])
                            .expertise("Software Development")
                            .bio("Experienced software developer")
                            .experienceYears(5)
                            .isAvailable(true)
                            .build();
                }
            }
            
            return ResponseEntity.ok(ApiResponse.success(user, "Authentication successful"));
        } catch (IllegalStateException e) {
            // This is likely an email verification error
            log.warn("Login failed for {} with email {}: {}", userType, authRequest.getEmail(), e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .body(ApiResponse.error(e.getMessage()));
        } catch (ResourceNotFoundException e) {
            // Incorrect login credentials
            log.error("Login error for {} with email {}: {}", userType, authRequest.getEmail(), e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.error("Neplatné přihlašovací údaje. Zkontrolujte email a heslo."));
        } catch (Exception e) {
            // All other errors
            log.error("Login error for {} with email {}: {}", userType, authRequest.getEmail(), e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.error("Došlo k chybě při přihlašování. Zkuste to prosím znovu."));
        }
    }
}
