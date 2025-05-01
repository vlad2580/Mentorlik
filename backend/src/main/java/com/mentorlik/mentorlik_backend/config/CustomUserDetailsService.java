package com.mentorlik.mentorlik_backend.config;

import com.mentorlik.mentorlik_backend.model.AdminProfile;
import com.mentorlik.mentorlik_backend.model.MentorProfile;
import com.mentorlik.mentorlik_backend.model.StudentProfile;
import com.mentorlik.mentorlik_backend.repository.AdminProfileRepository;
import com.mentorlik.mentorlik_backend.repository.MentorProfileRepository;
import com.mentorlik.mentorlik_backend.repository.StudentProfileRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Custom implementation of UserDetailsService for loading user details from the database.
 * <p>
 * This service is responsible for loading user details during authentication.
 * It checks all user repositories (Admin, Mentor, Student) to find the user
 * and returns appropriate UserDetails with the correct role.
 * </p>
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final AdminProfileRepository adminProfileRepository;
    private final MentorProfileRepository mentorProfileRepository;
    private final StudentProfileRepository studentProfileRepository;

    /**
     * Constructs a new CustomUserDetailsService with the specified repositories.
     *
     * @param adminProfileRepository repository for admin data access
     * @param mentorProfileRepository repository for mentor data access
     * @param studentProfileRepository repository for student data access
     */
    public CustomUserDetailsService(
            AdminProfileRepository adminProfileRepository,
            MentorProfileRepository mentorProfileRepository,
            StudentProfileRepository studentProfileRepository) {
        this.adminProfileRepository = adminProfileRepository;
        this.mentorProfileRepository = mentorProfileRepository;
        this.studentProfileRepository = studentProfileRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // Try to find user in each repository
        return adminProfileRepository.findByEmail(email)
                .map(admin -> new CustomUserDetails(admin))
                .orElseGet(() -> mentorProfileRepository.findByEmail(email)
                        .map(mentor -> new CustomUserDetails(mentor))
                        .orElseGet(() -> studentProfileRepository.findByEmail(email)
                                .map(student -> new CustomUserDetails(student))
                                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email))));
    }
} 