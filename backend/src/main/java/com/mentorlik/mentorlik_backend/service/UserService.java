package com.mentorlik.mentorlik_backend.service;

import com.mentorlik.mentorlik_backend.dto.profile.AdminProfileDto;
import com.mentorlik.mentorlik_backend.dto.profile.MentorProfileDto;
import com.mentorlik.mentorlik_backend.dto.profile.StudentProfileDto;
import com.mentorlik.mentorlik_backend.exception.ResourceNotFoundException;
import com.mentorlik.mentorlik_backend.model.AdminProfile;
import com.mentorlik.mentorlik_backend.model.MentorProfile;
import com.mentorlik.mentorlik_backend.model.StudentProfile;
import com.mentorlik.mentorlik_backend.repository.AdminProfileRepository;
import com.mentorlik.mentorlik_backend.repository.MentorProfileRepository;
import com.mentorlik.mentorlik_backend.repository.StudentProfileRepository;
import org.springframework.stereotype.Service;

/**
 * Service class for managing user-related operations.
 * <p>
 * Provides methods for retrieving information for different user profiles
 * (Admin, Mentor, Student) and converting entities to appropriate DTOs.
 * </p>
 */
@Service
public class UserService {

    private final AdminProfileRepository adminRepository;
    private final MentorProfileRepository mentorRepository;
    private final StudentProfileRepository studentRepository;

    /**
     * Constructs a new {@code UserService} with the specified repositories.
     *
     * @param adminRepository  repository for admin data access
     * @param mentorRepository repository for mentor data access
     * @param studentRepository repository for student data access
     */
    public UserService(AdminProfileRepository adminRepository, MentorProfileRepository mentorRepository,
                       StudentProfileRepository studentRepository) {
        this.adminRepository = adminRepository;
        this.mentorRepository = mentorRepository;
        this.studentRepository = studentRepository;
    }

    /**
     * Retrieves a mentor by their ID and converts it to a {@link MentorProfileDto}.
     *
     * @param id the ID of the mentor to retrieve
     * @return a {@link MentorProfileDto} containing the mentor's data
     * @throws ResourceNotFoundException if no mentor with the specified ID is found
     */
    public MentorProfileDto getMentorById(Long id) {
        MentorProfile mentor = mentorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Mentor not found with ID: " + id));
        return MentorProfileDto.builder()
                .id(mentor.getId())
                .email(mentor.getEmail())
                .expertise(mentor.getExpertise())
                .bio(mentor.getBio())
                .experienceYears(mentor.getExperienceYears())
                .certifications(mentor.getCertifications())
                .isAvailable(mentor.getIsAvailable())
                .build();
    }

    /**
     * Retrieves a student by their ID and converts it to a {@link StudentProfileDto}.
     *
     * @param id the ID of the student to retrieve
     * @return a {@link StudentProfileDto} containing the student's data
     * @throws ResourceNotFoundException if no student with the specified ID is found
     */
    public StudentProfileDto getStudentById(Long id) {
        StudentProfile student = studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with ID: " + id));
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

    /**
     * Retrieves an admin by their ID and converts it to an {@link AdminProfileDto}.
     *
     * @param id the ID of the admin to retrieve
     * @return an {@link AdminProfileDto} containing the admin's data
     * @throws ResourceNotFoundException if no admin with the specified ID is found
     */
    public AdminProfileDto getAdminById(Long id) {
        AdminProfile admin = adminRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Admin not found with ID: " + id));
        return AdminProfileDto.builder()
                .id(admin.getId())
                .email(admin.getEmail())
                .accessLevel(admin.getAccessLevel())
                .role(admin.getRole())
                .build();
    }
}