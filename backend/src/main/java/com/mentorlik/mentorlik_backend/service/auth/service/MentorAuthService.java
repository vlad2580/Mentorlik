package com.mentorlik.mentorlik_backend.service.auth.service;

import com.mentorlik.mentorlik_backend.dto.auth.AuthRequestDto;
import com.mentorlik.mentorlik_backend.dto.profile.MentorProfileDto;
import com.mentorlik.mentorlik_backend.dto.profile.UserDto;
import com.mentorlik.mentorlik_backend.exception.ResourceNotFoundException;
import com.mentorlik.mentorlik_backend.exception.validation.EmailAlreadyExistsException;
import com.mentorlik.mentorlik_backend.model.MentorProfile;
import com.mentorlik.mentorlik_backend.repository.MentorProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Service for handling mentor authentication and profile management.
 * Implements the base {@link BaseAuthService} to provide mentor-specific logic for registration and login.
 */
@Service
public class MentorAuthService implements BaseAuthService<MentorProfile, MentorProfileDto> {

    private final MentorProfileRepository mentorRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * Constructs an instance of {@code MentorAuthService} with the required dependencies.
     *
     * @param mentorRepository The repository for managing mentor profiles.
     * @param passwordEncoder  The encoder for hashing passwords.
     */
    @Autowired
    public MentorAuthService(MentorProfileRepository mentorRepository, PasswordEncoder passwordEncoder) {
        this.mentorRepository = mentorRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Performs mentor login using email and password.
     *
     * @param authRequest Authentication request DTO containing email and password
     * @return Mentor DTO after successful authentication
     * @throws ResourceNotFoundException if mentor is not found or credentials are invalid
     */
    @Override
    public MentorProfileDto login(AuthRequestDto authRequest) {
        MentorProfile mentor = mentorRepository.findByEmail(authRequest.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("Mentor not found with email: " + authRequest.getEmail()));

        if (!passwordEncoder.matches(authRequest.getPassword(), mentor.getPassword())) {
            throw new ResourceNotFoundException("Invalid credentials provided");
        }

        return convertToDto(mentor);
    }

    /**
     * Performs login using OAuth2 token.
     *
     * @param token OAuth2 token
     * @param userType type of user
     * @return Mentor DTO after successful authentication
     */
    @Override
    public MentorProfileDto loginWithToken(String token, String userType) {
        // Implementation of OAuth2 for mentors should be here
        throw new UnsupportedOperationException("OAuth2 login for mentors not implemented yet");
    }

    /**
     * Registers a new mentor.
     *
     * @param userDto DTO with mentor data
     * @return DTO of the registered mentor
     * @throws EmailAlreadyExistsException if email is already in use
     */
    @Override
    public MentorProfileDto register(UserDto userDto) {
        if (mentorRepository.findByEmail(userDto.getEmail()).isPresent()) {
            throw new EmailAlreadyExistsException("Email is already in use");
        }

        MentorProfileDto mentorDto = (MentorProfileDto) userDto;
        MentorProfile mentor = createUserEntity(mentorDto);
        mentorRepository.save(mentor);

        return convertToDto(mentor);
    }

    /**
     * Creates a new {@link MentorProfile} entity from the provided {@link MentorProfileDto}.
     *
     * @param userDto The DTO containing mentor profile data.
     * @return A new {@link MentorProfile} entity with the data from the DTO.
     */
    protected MentorProfile createUserEntity(MentorProfileDto userDto) {
        MentorProfile mentor = new MentorProfile();
        mentor.setEmail(userDto.getEmail());
        mentor.setPassword(passwordEncoder.encode(userDto.getPassword()));
        mentor.setExpertise(userDto.getExpertise());
        mentor.setBio(userDto.getBio());
        mentor.setExperienceYears(userDto.getExperienceYears());
        mentor.setCertifications(userDto.getCertifications());
        mentor.setIsAvailable(userDto.getIsAvailable());
        mentor.setCity(userDto.getCity());
        mentor.setCountry(userDto.getCountry());
        mentor.setHourlyRate(userDto.getHourlyRate());
        mentor.setLanguages(userDto.getLanguages());
        mentor.setRating(userDto.getRating());
        mentor.setReviewCount(userDto.getReviewCount());
        return mentor;
    }

    /**
     * Converts a {@link MentorProfile} entity to a {@link MentorProfileDto}.
     *
     * @param mentor The mentor entity to convert.
     * @return A {@link MentorProfileDto} containing the data from the entity.
     */
    protected MentorProfileDto convertToDto(MentorProfile mentor) {
        return MentorProfileDto.builder()
                .id(mentor.getId())
                .email(mentor.getEmail())
                .expertise(mentor.getExpertise())
                .bio(mentor.getBio())
                .experienceYears(mentor.getExperienceYears())
                .certifications(mentor.getCertifications())
                .isAvailable(mentor.getIsAvailable())
                .city(mentor.getCity())
                .country(mentor.getCountry())
                .hourlyRate(mentor.getHourlyRate())
                .languages(mentor.getLanguages())
                .rating(mentor.getRating())
                .reviewCount(mentor.getReviewCount())
                .build();
    }
}