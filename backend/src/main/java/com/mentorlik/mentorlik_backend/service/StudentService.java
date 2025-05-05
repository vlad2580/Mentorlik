package com.mentorlik.mentorlik_backend.service;

import com.mentorlik.mentorlik_backend.dto.profile.StudentProfileDto;
import com.mentorlik.mentorlik_backend.exception.ResourceNotFoundException;
import com.mentorlik.mentorlik_backend.exception.validation.EmailAlreadyExistsException;
import com.mentorlik.mentorlik_backend.model.StudentProfile;
import com.mentorlik.mentorlik_backend.repository.StudentProfileRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service for managing student data.
 * <p>
 * Provides methods for working with student profiles,
 * including creation, search, update, and deletion.
 * </p>
 */
@Slf4j
@Service
public class StudentService {

    private final StudentProfileRepository studentProfileRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * Creates an instance of the student service.
     *
     * @param studentProfileRepository repository for accessing student data
     * @param passwordEncoder password encoder
     */
    public StudentService(StudentProfileRepository studentProfileRepository, PasswordEncoder passwordEncoder) {
        this.studentProfileRepository = studentProfileRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Gets a list of all students.
     *
     * @return list of student DTOs
     */
    @Transactional(readOnly = true)
    public List<StudentProfileDto> getAllStudents() {
        log.info("Getting all students");
        return studentProfileRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    /**
     * Gets a student by ID.
     *
     * @param id student identifier
     * @return student DTO
     * @throws ResourceNotFoundException if a student with the specified ID is not found
     */
    @Transactional(readOnly = true)
    public StudentProfileDto getStudentById(Long id) {
        log.info("Getting student with ID: {}", id);
        StudentProfile student = studentProfileRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with ID: " + id));
        return convertToDto(student);
    }

    /**
     * Gets a student by email.
     *
     * @param email student email
     * @return student DTO
     * @throws ResourceNotFoundException if a student with the specified email is not found
     */
    @Transactional(readOnly = true)
    public StudentProfileDto getStudentByEmail(String email) {
        log.info("Getting student with email: {}", email);
        StudentProfile student = studentProfileRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with email: " + email));
        return convertToDto(student);
    }

    /**
     * Creates a new student.
     *
     * @param studentDto data for the new student
     * @return DTO of the created student
     * @throws EmailAlreadyExistsException if the email is already in use
     */
    @Transactional
    public StudentProfileDto createStudent(StudentProfileDto studentDto) {
        log.info("Creating new student with email: {}", studentDto.getEmail());
        
        // Check email uniqueness
        if (studentProfileRepository.findByEmail(studentDto.getEmail()).isPresent()) {
            throw new EmailAlreadyExistsException("Email is already in use: " + studentDto.getEmail());
        }
        
        StudentProfile student = convertToEntity(studentDto);
        
        // Password hashing
        if (studentDto.getPassword() != null && !studentDto.getPassword().isEmpty()) {
            student.setPassword(passwordEncoder.encode(studentDto.getPassword()));
        }
        
        StudentProfile savedStudent = studentProfileRepository.save(student);
        log.info("Student successfully created with ID: {}", savedStudent.getId());
        return convertToDto(savedStudent);
    }

    /**
     * Updates student data.
     *
     * @param id student identifier
     * @param studentDto new student data
     * @return DTO of the updated student
     * @throws ResourceNotFoundException if a student with the specified ID is not found
     * @throws EmailAlreadyExistsException if the new email is already used by another user
     */
    @Transactional
    public StudentProfileDto updateStudent(Long id, StudentProfileDto studentDto) {
        log.info("Updating student with ID: {}", id);
        
        StudentProfile existingStudent = studentProfileRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with ID: " + id));
        
        // Check that the new email is not used by another user
        if (!existingStudent.getEmail().equals(studentDto.getEmail()) && 
                studentProfileRepository.findByEmail(studentDto.getEmail()).isPresent()) {
            throw new EmailAlreadyExistsException("Email is already in use: " + studentDto.getEmail());
        }
        
        // Update fields
        updateStudentFields(existingStudent, studentDto);
        
        StudentProfile updatedStudent = studentProfileRepository.save(existingStudent);
        log.info("Student successfully updated with ID: {}", updatedStudent.getId());
        return convertToDto(updatedStudent);
    }

    /**
     * Deletes a student by ID.
     *
     * @param id student identifier
     * @throws ResourceNotFoundException if a student with the specified ID is not found
     */
    @Transactional
    public void deleteStudent(Long id) {
        log.info("Deleting student with ID: {}", id);
        
        if (!studentProfileRepository.existsById(id)) {
            throw new ResourceNotFoundException("Student not found with ID: " + id);
        }
        
        studentProfileRepository.deleteById(id);
        log.info("Student successfully deleted with ID: {}", id);
    }

    /**
     * Searches for students by the given query.
     *
     * @param query search string
     * @return list of found student DTOs
     */
    @Transactional(readOnly = true)
    public List<StudentProfileDto> searchStudents(String query) {
        log.info("Searching students by query: {}", query);
        
        // A more complex search could be implemented here, for example using Specification
        List<StudentProfile> students = studentProfileRepository.findByNameContainingIgnoreCaseOrFieldOfStudyContainingIgnoreCase(
                query, query);
        
        return students.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    /**
     * Converts a student entity to DTO.
     *
     * @param student student entity
     * @return student DTO
     */
    private StudentProfileDto convertToDto(StudentProfile student) {
        StudentProfileDto dto = new StudentProfileDto();
        dto.setId(student.getId());
        dto.setEmail(student.getEmail());
        dto.setName(student.getName());
        dto.setFieldOfStudy(student.getFieldOfStudy());
        dto.setEducationLevel(student.getEducationLevel());
        // Removing non-existent methods
        // Not transferring password to DTO
        
        return dto;
    }

    /**
     * Converts a student DTO to entity.
     *
     * @param studentDto student DTO
     * @return student entity
     */
    private StudentProfile convertToEntity(StudentProfileDto studentDto) {
        StudentProfile student = new StudentProfile();
        student.setId(studentDto.getId()); // Null for new students
        student.setEmail(studentDto.getEmail());
        student.setName(studentDto.getName());
        student.setFieldOfStudy(studentDto.getFieldOfStudy());
        student.setEducationLevel(studentDto.getEducationLevel());
        student.setLearningGoals(studentDto.getLearningGoals());
        student.setSkills(studentDto.getSkills());
        
        // Гарантируем, что isAvailableForMentorship никогда не будет null
        student.setIsAvailableForMentorship(studentDto.getIsAvailableForMentorship() != null ? 
                                         studentDto.getIsAvailableForMentorship() : 
                                         false);
        
        // Установка emailVerified по умолчанию в false для новых студентов
        student.setEmailVerified(false);
        
        return student;
    }

    /**
     * Updates fields of an existing student based on DTO.
     *
     * @param existingStudent existing student entity
     * @param studentDto DTO with new data
     */
    private void updateStudentFields(StudentProfile existingStudent, StudentProfileDto studentDto) {
        // Only update email if it's not null in the DTO
        if (studentDto.getEmail() != null && !studentDto.getEmail().isEmpty()) {
            existingStudent.setEmail(studentDto.getEmail());
        }
        existingStudent.setName(studentDto.getName());
        existingStudent.setFieldOfStudy(studentDto.getFieldOfStudy());
        existingStudent.setEducationLevel(studentDto.getEducationLevel());
        existingStudent.setLearningGoals(studentDto.getLearningGoals());
        existingStudent.setSkills(studentDto.getSkills());
        existingStudent.setIsAvailableForMentorship(studentDto.getIsAvailableForMentorship());
        
        // Update password only if specified in DTO
        if (studentDto.getPassword() != null && !studentDto.getPassword().isEmpty()) {
            existingStudent.setPassword(passwordEncoder.encode(studentDto.getPassword()));
        }
    }
} 