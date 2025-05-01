package com.mentorlik.mentorlik_backend.service.auth.service;

import com.mentorlik.mentorlik_backend.dto.auth.AuthRequestDto;
import com.mentorlik.mentorlik_backend.dto.profile.StudentProfileDto;
import com.mentorlik.mentorlik_backend.dto.profile.UserDto;
import com.mentorlik.mentorlik_backend.model.StudentProfile;
import com.mentorlik.mentorlik_backend.model.User;
import com.mentorlik.mentorlik_backend.repository.StudentProfileRepository;
import com.mentorlik.mentorlik_backend.exception.ResourceNotFoundException;
import com.mentorlik.mentorlik_backend.exception.validation.EmailAlreadyExistsException;
import com.mentorlik.mentorlik_backend.service.auth.JwtTokenService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service for handling student authentication and profile management.
 * Implements the base {@link BaseAuthService} to provide student-specific logic for registration and login.
 */
@Service("student")
public class StudentAuthService implements BaseAuthService<StudentProfile, StudentProfileDto> {

    private final StudentProfileRepository studentRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenService jwtTokenService;
    private static final Logger log = LoggerFactory.getLogger(StudentAuthService.class);

    /**
     * Constructs an instance of {@code StudentAuthService} with the required dependencies.
     *
     * @param studentRepository The repository for managing student profiles.
     * @param passwordEncoder   The encoder for hashing passwords.
     * @param jwtTokenService   The service for JWT token generation.
     */
    @Autowired
    public StudentAuthService(
            StudentProfileRepository studentRepository, 
            PasswordEncoder passwordEncoder,
            JwtTokenService jwtTokenService) {
        this.studentRepository = studentRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenService = jwtTokenService;
    }

    @Override
    public StudentProfileDto login(AuthRequestDto authRequest) {
        log.info("Login attempt for student with email: {}", authRequest.getEmail());
        
        // Check if student exists with the given email
        StudentProfile student = studentRepository.findByEmail(authRequest.getEmail())
                .orElseThrow(() -> {
                    log.warn("Login failed: Student not found with email: {}", authRequest.getEmail());
                    return new ResourceNotFoundException("Student not found with email: " + authRequest.getEmail());
                });
        
        log.debug("Student found with ID: {}, verification status: {}", student.getId(), student.getEmailVerified());

        // Validate password
        if (!passwordEncoder.matches(authRequest.getPassword(), student.getPassword())) {
            log.warn("Login failed: Invalid password for student: {}", authRequest.getEmail());
            throw new ResourceNotFoundException("Invalid credentials provided");
        }
        
        // Check if email is verified
        if (!student.getEmailVerified()) {
            log.warn("Login failed: Email not verified for student: {}", authRequest.getEmail());
            throw new IllegalStateException("Email not verified. Please check your email and verify your account before logging in.");
        }
        
        // Create DTO from entity
        StudentProfileDto studentDto = convertToDto(student);
        
        // Generate JWT token
        String accessToken = jwtTokenService.generateAccessToken(studentDto);
        studentDto.setToken(accessToken);
        
        log.info("Login successful for student: {}, token generated", authRequest.getEmail());
        return studentDto;
    }

    @Override
    public StudentProfileDto loginWithToken(String token, String userType) {
        // Implementation of OAuth login for students
        throw new UnsupportedOperationException("OAuth2 login for students not implemented yet");
    }

    @Override
    public StudentProfileDto register(UserDto userDto) {
        if (studentRepository.existsByEmail(userDto.getEmail())) {
            throw new EmailAlreadyExistsException("Email is already in use");
        }

        StudentProfileDto studentDto = (StudentProfileDto) userDto;
        StudentProfile student = createUserEntity(studentDto);
        studentRepository.save(student);

        return convertToDto(student);
    }

    private StudentProfile createUserEntity(StudentProfileDto userDto) {
        StudentProfile student = new StudentProfile();
        student.setEmail(userDto.getEmail());
        student.setPassword(passwordEncoder.encode(userDto.getPassword()));
        student.setName(userDto.getName());
        student.setFieldOfStudy(userDto.getFieldOfStudy());
        student.setEducationLevel(userDto.getEducationLevel());
        student.setLearningGoals(userDto.getLearningGoals());
        student.setSkills(userDto.getSkills());
        student.setIsAvailableForMentorship(userDto.getIsAvailableForMentorship());
        student.setEmailVerified(false);
        return student;
    }

    private StudentProfileDto convertToDto(StudentProfile student) {
        return StudentProfileDto.builder()
                .id(student.getId())
                .email(student.getEmail())
                .name(student.getName())
                .fieldOfStudy(student.getFieldOfStudy())
                .educationLevel(student.getEducationLevel())
                .learningGoals(student.getLearningGoals())
                .skills(student.getSkills())
                .isAvailableForMentorship(student.getIsAvailableForMentorship())
                .emailVerified(student.getEmailVerified())
                .build();
    }
}