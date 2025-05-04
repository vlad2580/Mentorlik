package com.mentorlik.mentorlik_backend.controller;

import com.mentorlik.mentorlik_backend.dto.ApiResponse;
import com.mentorlik.mentorlik_backend.dto.profile.StudentProfileDto;
import com.mentorlik.mentorlik_backend.dto.registration.CreateStudentRequest;
import com.mentorlik.mentorlik_backend.exception.ResourceNotFoundException;
import com.mentorlik.mentorlik_backend.exception.auth.TokenExpiredException;
import com.mentorlik.mentorlik_backend.exception.validation.EmailAlreadyExistsException;
import com.mentorlik.mentorlik_backend.model.StudentProfile;
import com.mentorlik.mentorlik_backend.repository.StudentProfileRepository;
import com.mentorlik.mentorlik_backend.service.StudentService;
import com.mentorlik.mentorlik_backend.service.VerificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

/**
 * REST controller for student operations, including registration, verification, and management.
 */
@Slf4j
@RestController
@RequestMapping("/api/public/students")
@RequiredArgsConstructor
public class StudentController {

    private final StudentService studentService;
    private final VerificationService verificationService;
    private final StudentProfileRepository studentProfileRepository;

    // Public endpoints
    @PostMapping("/register")
    public ResponseEntity<ApiResponse<StudentProfileDto>> registerStudent(
            @Valid @RequestBody CreateStudentRequest request) {
        log.info("Registering student with email={}", request.getEmail());
        try {
            StudentProfileDto created = studentService.createStudent(toDto(request));
            verificationService.createVerificationTokenAndSendEmail(
                    created.getEmail(), created.getId(), "student"
            );
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(ApiResponse.success(
                            created,
                            "Registration successful. Check your email to verify your account."));
        } catch (EmailAlreadyExistsException e) {
            log.warn("Email already registered: {}", request.getEmail());
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body(ApiResponse.error("Email is already registered."));
        } catch (Exception e) {
            log.error("Failed to register student: {}", e.getMessage(), e);
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Registration failed. Please try again later."));
        }
    }

    @GetMapping("/verify")
    public ResponseEntity<ApiResponse<Void>> verifyEmail(@RequestParam String token) {
        log.info("Verifying email with token={}", token);
        try {
            verificationService.verifyToken(token);
            return ResponseEntity.ok(ApiResponse.success(
                    null,
                    "Email verified successfully. You can now log in."));
        } catch (ResourceNotFoundException e) {
            log.warn("Token not found: {}", token);
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error("Verification token not found. Request a new one."));
        } catch (TokenExpiredException e) {
            log.warn("Token expired: {}", token);
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error("Verification link expired. Request a new one."));
        } catch (Exception e) {
            log.error("Error during verification: {}", e.getMessage(), e);
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error("Verification failed. Please contact support."));
        }
    }

    @PostMapping("/resend-verification")
    public ResponseEntity<ApiResponse<Void>> resendVerification(
            @RequestBody Map<String, String> payload) {
        String email = payload.get("email");
        log.info("Resend verification for email={}", email);

        if (email == null || email.isBlank()) {
            return ResponseEntity
                    .badRequest()
                    .body(ApiResponse.error("Email must be provided."));
        }

        try {
            StudentProfile student = studentProfileRepository
                    .findByEmail(email)
                    .orElseThrow(() ->
                        new ResourceNotFoundException("Student not found: " + email)
                    );

            if (student.getEmailVerified()) {
                return ResponseEntity
                        .badRequest()
                        .body(ApiResponse.error("Email is already verified."));
            }

            verificationService.createVerificationTokenAndSendEmail(
                    email, student.getId(), "student"
            );
            return ResponseEntity.ok(
                    ApiResponse.success(null, "Verification email sent. Check your inbox."));

        } catch (ResourceNotFoundException e) {
            log.warn("Email not in system: {}", email);
            return ResponseEntity.ok(
                    ApiResponse.success(null, "If the email is registered, a link was sent."));
        } catch (Exception e) {
            log.error("Failed to resend verification email: {}", e.getMessage(), e);
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Unable to send email. Try again later."));
        }
    }

    // Admin endpoints
    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<List<StudentProfileDto>>> getAll() {
        log.info("Fetching all students");
        List<StudentProfileDto> list = studentService.getAllStudents();
        return ResponseEntity.ok(ApiResponse.success(list));
    }

    @GetMapping("/admin/{id}")
    @PreAuthorize("hasRole('ADMIN') or @studentSecurityService.isStudentOwner(authentication, #id)")
    public ResponseEntity<ApiResponse<StudentProfileDto>> getById(@PathVariable Long id) {
        log.info("Fetching student id={}", id);
        StudentProfileDto dto = studentService.getStudentById(id);
        return ResponseEntity.ok(ApiResponse.success(dto));
    }

    @GetMapping("/admin/search")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MENTOR')")
    public ResponseEntity<ApiResponse<List<StudentProfileDto>>> search(@RequestParam String query) {
        log.info("Searching students with query='{}'", query);
        List<StudentProfileDto> results = studentService.searchStudents(query);
        return ResponseEntity.ok(ApiResponse.success(results));
    }

    @PostMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<StudentProfileDto>> create(
            @Valid @RequestBody StudentProfileDto dto) {
        log.info("Creating student email={}", dto.getEmail());
        StudentProfileDto created = studentService.createStudent(dto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success(created, "Student created"));
    }

    @PutMapping("/admin/{id}")
    @PreAuthorize("hasRole('STUDENT') and @studentSecurityService.isStudentOwner(authentication, #id)")
    public ResponseEntity<ApiResponse<StudentProfileDto>> update(
            @PathVariable Long id,
            @Valid @RequestBody StudentProfileDto dto) {
        log.info("Updating student id={}", id);
        StudentProfileDto updated = studentService.updateStudent(id, dto);
        return ResponseEntity.ok(ApiResponse.success(updated, "Student data updated"));
    }

    @DeleteMapping("/admin/{id}")
    @PreAuthorize("hasRole('ADMIN') or (hasRole('STUDENT') and @studentSecurityService.isStudentOwner(authentication, #id))")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        log.info("Deleting student id={}", id);
        studentService.deleteStudent(id);
        return ResponseEntity.ok(ApiResponse.success(null, "Student deleted"));
    }

    // New endpoint for getting current student profile
    @GetMapping("/current")
    public ResponseEntity<ApiResponse<StudentProfileDto>> getCurrentStudentProfile(@RequestHeader("Authorization") String authHeader) {
        // Extract email from token (or use SecurityContext)
        // Here we assume that JwtAuthenticationFilter has already set up authentication
        String email = org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication().getName();
        StudentProfileDto student = studentService.getStudentByEmail(email);
        return ResponseEntity.ok(ApiResponse.success(student));
    }

    private StudentProfileDto toDto(CreateStudentRequest req) {
        StudentProfileDto dto = new StudentProfileDto();
        dto.setName(req.getName());
        dto.setEmail(req.getEmail());
        dto.setPassword(req.getPassword());
        dto.setFieldOfStudy(req.getFieldOfStudy());
        dto.setEducationLevel(req.getEducationLevel());
        dto.setLearningGoals(req.getLearningGoals());
        dto.setSkills(req.getSkills());
        dto.setIsAvailableForMentorship(
                Boolean.TRUE.equals(req.getIsAvailableForMentorship())
        );
        return dto;
    }
}