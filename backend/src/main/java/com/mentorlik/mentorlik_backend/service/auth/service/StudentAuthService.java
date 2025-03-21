package com.mentorlik.mentorlik_backend.service.auth.service;

import com.mentorlik.mentorlik_backend.dto.auth.AuthRequestDto;
import com.mentorlik.mentorlik_backend.dto.profile.StudentProfileDto;
import com.mentorlik.mentorlik_backend.dto.profile.UserDto;
import com.mentorlik.mentorlik_backend.exception.ResourceNotFoundException;
import com.mentorlik.mentorlik_backend.exception.validation.EmailAlreadyExistsException;
import com.mentorlik.mentorlik_backend.model.StudentProfile;
import com.mentorlik.mentorlik_backend.repository.StudentProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Service for handling student authentication and profile management.
 * Implements the base {@link BaseAuthService} to provide student-specific logic for registration and login.
 */
@Service
public class StudentAuthService implements BaseAuthService<StudentProfile, StudentProfileDto> {

    private final StudentProfileRepository studentRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * Constructs an instance of {@code StudentAuthService} with the required dependencies.
     *
     * @param studentRepository The repository for managing student profiles.
     * @param passwordEncoder   The encoder for hashing passwords.
     */
    @Autowired
    public StudentAuthService(StudentProfileRepository studentRepository, PasswordEncoder passwordEncoder) {
        this.studentRepository = studentRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public StudentProfileDto login(AuthRequestDto authRequest) {
        StudentProfile student = studentRepository.findByEmail(authRequest.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with email: " + authRequest.getEmail()));

        if (!passwordEncoder.matches(authRequest.getPassword(), student.getPassword())) {
            throw new ResourceNotFoundException("Invalid credentials provided");
        }

        return convertToDto(student);
    }

    @Override
    public StudentProfileDto loginWithToken(String token, String userType) {
        // Implementation of OAuth login for students
        throw new UnsupportedOperationException("OAuth2 login for students not implemented yet");
    }

    @Override
    public StudentProfileDto register(UserDto userDto) {
        if (studentRepository.findByEmail(userDto.getEmail()).isPresent()) {
            throw new EmailAlreadyExistsException("Email is already in use");
        }

        StudentProfileDto studentDto = (StudentProfileDto) userDto;
        StudentProfile student = createUserEntity(studentDto);
        studentRepository.save(student);

        return convertToDto(student);
    }

    /**
     * Creates a new {@link StudentProfile} entity from the provided {@link StudentProfileDto}.
     *
     * @param userDto The DTO containing student profile data.
     * @return A new {@link StudentProfile} entity with the data from the DTO.
     */
    protected StudentProfile createUserEntity(StudentProfileDto userDto) {
        StudentProfile student = new StudentProfile();
        student.setEmail(userDto.getEmail());
        student.setPassword(passwordEncoder.encode(userDto.getPassword()));
        student.setFieldOfStudy(userDto.getFieldOfStudy());
        student.setEducationLevel(userDto.getEducationLevel());
        student.setLearningGoals(userDto.getLearningGoals());
        student.setSkills(userDto.getSkills());
        student.setIsAvailableForMentorship(userDto.getIsAvailableForMentorship());
        return student;
    }

    /**
     * Converts a {@link StudentProfile} entity to a {@link StudentProfileDto}.
     *
     * @param student The student entity to convert.
     * @return A {@link StudentProfileDto} containing the data from the entity.
     */
    protected StudentProfileDto convertToDto(StudentProfile student) {
        return StudentProfileDto.builder()
                .id(student.getId())
                .email(student.getEmail())
                .fieldOfStudy(student.getFieldOfStudy())
                .educationLevel(student.getEducationLevel())
                .learningGoals(student.getLearningGoals())
                .skills(student.getSkills())
                .isAvailableForMentorship(student.getIsAvailableForMentorship())
                .build();
    }
}