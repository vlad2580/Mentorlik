package com.mentorlik.mentorlik_backend.controller;

import com.mentorlik.mentorlik_backend.dto.ApiResponse;
import com.mentorlik.mentorlik_backend.dto.profile.StudentProfileDto;
import com.mentorlik.mentorlik_backend.dto.registration.CreateStudentRequest;
import com.mentorlik.mentorlik_backend.exception.validation.EmailAlreadyExistsException;
import com.mentorlik.mentorlik_backend.service.StudentService;
import com.mentorlik.mentorlik_backend.service.VerificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * Controller for handling student creation with email verification.
 */
@Slf4j
@RestController
@RequestMapping("/api/students")
@RequiredArgsConstructor
public class CreateStudentController {

    private final StudentService studentService;
    private final VerificationService verificationService;

    /**
     * Endpoint for creating a student.
     * Creates an unverified student account and sends a verification email.
     *
     * @param request Student creation data
     * @return A response containing the created student or an error message
     */
    @PostMapping("/create-student")
    public ResponseEntity<ApiResponse<StudentProfileDto>> createStudent(@Valid @RequestBody CreateStudentRequest request) {
        log.info("Student creation request received for email: {}", request.getEmail());
        
        try {
            // Convert CreateStudentRequest to StudentProfileDto
            StudentProfileDto studentDto = convertToProfileDto(request);
            
            StudentProfileDto student = studentService.createStudent(studentDto);
            
            // Send verification email
            verificationService.createVerificationTokenAndSendEmail(
                student.getEmail(), 
                student.getId(), 
                "student"
            );
            
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(ApiResponse.success(student, "Registration successful. Please check your email to verify your account."));
        } catch (EmailAlreadyExistsException e) {
            log.warn("Registration failed - email already exists: {}", request.getEmail());
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body(ApiResponse.error("Email is already registered: " + e.getMessage()));
        } catch (Exception e) {
            log.error("Error during student registration: {}", e.getMessage(), e);
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Registration failed: " + e.getMessage()));
        }
    }

    /**
     * Endpoint for verifying a student's email address.
     *
     * @param token The verification token sent to the student's email
     * @return A response indicating the success or failure of verification
     */
    @GetMapping("/create-student/verify")
    public ResponseEntity<ApiResponse<Void>> verifyEmail(@RequestParam String token) {
        log.info("Email verification request received with token: {}", token);
        
        try {
            verificationService.verifyToken(token);
            return ResponseEntity.ok(ApiResponse.success(null, "Email verified successfully. You can now log in."));
        } catch (Exception e) {
            log.error("Error during email verification: {}", e.getMessage(), e);
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error("Verification failed: " + e.getMessage()));
        }
    }
    
    /**
     * Converts a CreateStudentRequest to StudentProfileDto.
     * 
     * @param request The request to convert
     * @return A StudentProfileDto with data from the request
     */
    private StudentProfileDto convertToProfileDto(CreateStudentRequest request) {
        StudentProfileDto dto = new StudentProfileDto();
        dto.setName(request.getName());
        dto.setEmail(request.getEmail());
        dto.setPassword(request.getPassword());
        dto.setFieldOfStudy(request.getFieldOfStudy());
        dto.setEducationLevel(request.getEducationLevel());
        dto.setLearningGoals(request.getLearningGoals());
        dto.setSkills(request.getSkills());
        dto.setIsAvailableForMentorship(request.getIsAvailableForMentorship() != null ? 
                                       request.getIsAvailableForMentorship() : 
                                       false);
        return dto;
    }
} 