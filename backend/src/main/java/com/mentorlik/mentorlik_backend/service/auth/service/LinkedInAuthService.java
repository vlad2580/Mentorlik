package com.mentorlik.mentorlik_backend.service.auth.service;

import com.mentorlik.mentorlik_backend.dto.auth.LinkedInUserInfo;
import com.mentorlik.mentorlik_backend.dto.profile.MentorProfileDto;
import com.mentorlik.mentorlik_backend.dto.profile.StudentProfileDto;
import com.mentorlik.mentorlik_backend.dto.profile.UserDto;
import com.mentorlik.mentorlik_backend.model.MentorProfile;
import com.mentorlik.mentorlik_backend.model.StudentProfile;
import com.mentorlik.mentorlik_backend.repository.MentorProfileRepository;
import com.mentorlik.mentorlik_backend.repository.StudentProfileRepository;
import com.mentorlik.mentorlik_backend.service.auth.validator.OAuth2TokenValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Service for processing login with LinkedIn OAuth2 token and user type.
 */
@Slf4j
@Service("linkedin")
public class LinkedInAuthService {

    private final MentorProfileRepository mentorProfileRepository;
    private final StudentProfileRepository studentProfileRepository;
    private final OAuth2TokenValidator tokenValidator;

    @Autowired
    public LinkedInAuthService(MentorProfileRepository mentorProfileRepository,
                               StudentProfileRepository studentProfileRepository,
                               OAuth2TokenValidator tokenValidator) {
        this.mentorProfileRepository = mentorProfileRepository;
        this.studentProfileRepository = studentProfileRepository;
        this.tokenValidator = tokenValidator;
    }

    /**
     * Processes login with LinkedIn OAuth2 token and user type.
     *
     * @param userType   the type of user ("mentor" or "student")
     * @param oauthToken the OAuth2 token provided by LinkedIn
     * @return a DTO representing the authenticated user, either {@link MentorProfileDto} or {@link StudentProfileDto}
     */
    public UserDto loginWithOAuth(String userType, String oauthToken) {
        LinkedInUserInfo userInfo = (LinkedInUserInfo) tokenValidator.validateAndGetUserInfo(oauthToken);

        return switch (userType.toLowerCase()) {
            case "mentor" -> {
                MentorProfile mentor = findOrCreateMentorProfile(userInfo);
                yield new MentorProfileDto(
                        mentor.getId(),
                        mentor.getEmail(),
                        mentor.getName(),
                        mentor.getExpertise(),
                        mentor.getBio(),
                        mentor.getExperienceYears(),
                        mentor.getCertifications(),
                        mentor.getIsAvailable(),
                        mentor.getCity(),
                        mentor.getCountry(),
                        mentor.getHourlyRate(),
                        mentor.getLanguages(),
                        mentor.getRating(),
                        mentor.getReviewCount()
                );
            }
            case "student" -> {
                StudentProfile student = findOrCreateStudentProfile(userInfo);
                yield new StudentProfileDto(
                        student.getId(),
                        student.getEmail(),
                        student.getFieldOfStudy(),
                        student.getEducationLevel(),
                        student.getLearningGoals(),
                        student.getSkills(),
                        student.getIsAvailableForMentorship()
                );
            }
            default -> {
                log.error("Invalid user type for LinkedIn OAuth login: {}", userType);
                throw new IllegalArgumentException("Invalid user type for LinkedIn login: " + userType);
            }
        };
    }

    /**
     * Finds an existing MentorProfile by email or creates a new one based on LinkedIn user info.
     *
     * @param userInfo the LinkedIn user info retrieved from the OAuth2 token
     * @return the MentorProfile of the authenticated user
     */
    private MentorProfile findOrCreateMentorProfile(LinkedInUserInfo userInfo) {
        return mentorProfileRepository.findByEmail(userInfo.getEmail())
                .orElseGet(() -> createMentorProfile(userInfo));
    }

    /**
     * Creates a new MentorProfile based on LinkedIn user info.
     *
     * @param userInfo the LinkedIn user info retrieved from the OAuth2 token
     * @return the newly created MentorProfile
     */
    private MentorProfile createMentorProfile(LinkedInUserInfo userInfo) {
        MentorProfile mentor = new MentorProfile();
        mentor.setEmail(userInfo.getEmail());
        mentor.setExpertise("Unknown"); // Placeholder or default value
        mentor.setBio("New LinkedIn user");
        mentorProfileRepository.save(mentor);
        return mentor;
    }

    /**
     * Finds an existing StudentProfile by email or creates a new one based on LinkedIn user info.
     *
     * @param userInfo the LinkedIn user info retrieved from the OAuth2 token
     * @return the StudentProfile of the authenticated user
     */
    private StudentProfile findOrCreateStudentProfile(LinkedInUserInfo userInfo) {
        return studentProfileRepository.findByEmail(userInfo.getEmail())
                .orElseGet(() -> createStudentProfile(userInfo));
    }

    /**
     * Creates a new StudentProfile based on LinkedIn user info.
     *
     * @param userInfo the LinkedIn user info retrieved from the OAuth2 token
     * @return the newly created StudentProfile
     */
    private StudentProfile createStudentProfile(LinkedInUserInfo userInfo) {
        StudentProfile student = new StudentProfile();
        student.setEmail(userInfo.getEmail());
        student.setFieldOfStudy("Unknown");
        student.setEducationLevel("Unknown");
        studentProfileRepository.save(student);
        return student;
    }
}
