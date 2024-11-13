package com.mentorlik.mentorlik_backend.service.auth.service;

import com.mentorlik.mentorlik_backend.dto.profile.MentorProfileDto;
import com.mentorlik.mentorlik_backend.model.MentorProfile;
import com.mentorlik.mentorlik_backend.repository.MentorProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Service for handling mentor authentication and profile management.
 * Extends the base {@link BaseAuthService} to provide mentor-specific logic for registration and login.
 */
@Service
public class MentorAuthService extends BaseAuthService<MentorProfile, MentorProfileDto> {

    /**
     * Constructs an instance of {@code MentorAuthService} with the required dependencies.
     *
     * @param mentorRepository The repository for managing mentor profiles.
     * @param passwordEncoder  The encoder for hashing passwords.
     */
    @Autowired
    public MentorAuthService(MentorProfileRepository mentorRepository, PasswordEncoder passwordEncoder) {
        super(mentorRepository, passwordEncoder);
    }

    /**
     * Creates a new {@link MentorProfile} entity from the provided {@link MentorProfileDto}.
     *
     * @param userDto The DTO containing mentor profile data.
     * @return A new {@link MentorProfile} entity with the data from the DTO.
     */
    @Override
    protected MentorProfile createUserEntity(MentorProfileDto userDto) {
        MentorProfile mentor = new MentorProfile();
        mentor.setEmail(userDto.getEmail());
        mentor.setPassword(passwordEncoder.encode(userDto.getPassword()));
        mentor.setExpertise(userDto.getExpertise());
        mentor.setBio(userDto.getBio());
        mentor.setExperienceYears(userDto.getExperienceYears());
        mentor.setCertifications(userDto.getCertifications());
        mentor.setIsAvailable(userDto.getIsAvailable());
        return mentor;
    }

    /**
     * Converts a {@link MentorProfile} entity to a {@link MentorProfileDto}.
     *
     * @param mentor The mentor entity to convert.
     * @return A {@link MentorProfileDto} containing the data from the entity.
     */
    @Override
    protected MentorProfileDto convertToDto(MentorProfile mentor) {
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
}