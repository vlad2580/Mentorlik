package com.mentorlik.mentorlik_backend.domain.service;

import com.mentorlik.mentorlik_backend.domain.entity.AdminProfile;
import com.mentorlik.mentorlik_backend.domain.entity.MentorProfile;
import com.mentorlik.mentorlik_backend.domain.entity.StudentProfile;
import com.mentorlik.mentorlik_backend.domain.repository.AdminProfileRepository;
import com.mentorlik.mentorlik_backend.domain.repository.MentorProfileRepository;
import com.mentorlik.mentorlik_backend.domain.repository.StudentProfileRepository;

/**
 * Domain service for User-related business logic.
 * <p>
 * This service contains core domain logic for working with different user types.
 * It is independent of application-specific concerns and focuses purely on 
 * business rules and domain operations.
 * </p>
 */
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
     * Retrieves a mentor by their ID.
     *
     * @param id the ID of the mentor to retrieve
     * @return a mentor entity
     * @throws RuntimeException if no mentor with the specified ID is found
     */
    public MentorProfile getMentorById(Long id) {
        return mentorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Mentor not found with ID: " + id));
    }

    /**
     * Retrieves a student by their ID.
     *
     * @param id the ID of the student to retrieve
     * @return a student entity
     * @throws RuntimeException if no student with the specified ID is found
     */
    public StudentProfile getStudentById(Long id) {
        return studentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Student not found with ID: " + id));
    }

    /**
     * Retrieves an admin by their ID.
     *
     * @param id the ID of the admin to retrieve
     * @return an admin entity
     * @throws RuntimeException if no admin with the specified ID is found
     */
    public AdminProfile getAdminById(Long id) {
        return adminRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Admin not found with ID: " + id));
    }

    /**
     * Checks if a user exists with the given email address.
     *
     * @param email the email address to check
     * @return true if a user exists with the given email, false otherwise
     */
    public boolean existsByEmail(String email) {
        return adminRepository.existsByEmail(email) ||
               mentorRepository.existsByEmail(email) ||
               studentRepository.existsByEmail(email);
    }
} 