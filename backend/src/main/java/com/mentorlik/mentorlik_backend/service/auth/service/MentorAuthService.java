package com.mentorlik.mentorlik_backend.service.auth.service;

import com.mentorlik.mentorlik_backend.dto.auth.AuthRequestDto;
import com.mentorlik.mentorlik_backend.dto.profile.MentorProfileDto;
import com.mentorlik.mentorlik_backend.dto.profile.UserDto;
import com.mentorlik.mentorlik_backend.exception.ResourceNotFoundException;
import com.mentorlik.mentorlik_backend.exception.validation.EmailAlreadyExistsException;
import com.mentorlik.mentorlik_backend.model.MentorProfile;
import com.mentorlik.mentorlik_backend.repository.MentorProfileRepository;
import com.mentorlik.mentorlik_backend.service.auth.JwtTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Service for handling mentor authentication and profile management.
 * Implements the base {@link BaseAuthService} to provide mentor-specific logic for registration and login.
 */
@Service("mentor")
public class MentorAuthService implements BaseAuthService<MentorProfile, MentorProfileDto> {

    private final MentorProfileRepository mentorRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenService jwtTokenService;
    private static final Logger log = LoggerFactory.getLogger(MentorAuthService.class);

    /**
     * Constructs an instance of {@code MentorAuthService} with the required dependencies.
     *
     * @param mentorRepository The repository for managing mentor profiles.
     * @param passwordEncoder  The encoder for hashing passwords.
     * @param jwtTokenService  The service for generating JWT tokens.
     */
    @Autowired
    public MentorAuthService(
            MentorProfileRepository mentorRepository, 
            PasswordEncoder passwordEncoder,
            JwtTokenService jwtTokenService) {
        this.mentorRepository = mentorRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenService = jwtTokenService;
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
        log.info("Login attempt for mentor with email: {}", authRequest.getEmail());
        
        MentorProfile mentor = mentorRepository.findByEmail(authRequest.getEmail())
                .orElseThrow(() -> {
                    log.warn("Login failed: Mentor not found with email: {}", authRequest.getEmail());
                    return new ResourceNotFoundException("Mentor not found with email: " + authRequest.getEmail());
                });

        if (!passwordEncoder.matches(authRequest.getPassword(), mentor.getPassword())) {
            log.warn("Login failed: Invalid password for mentor: {}", authRequest.getEmail());
            throw new ResourceNotFoundException("Invalid credentials provided");
        }
        
        // Check if email is verified
        if (!mentor.getEmailVerified()) {
            log.warn("Login failed: Email not verified for mentor: {}", authRequest.getEmail());
            throw new IllegalStateException("Email not verified. Please check your email and verify your account before logging in.");
        }

        // Convert entity to DTO
        MentorProfileDto mentorDto = convertToDto(mentor);
        
        // Generate JWT token
        String accessToken = jwtTokenService.generateAccessToken(mentorDto);
        mentorDto.setToken(accessToken);
        
        log.info("Login successful for mentor: {}, token generated", authRequest.getEmail());
        return mentorDto;
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
        // Implementation for OAuth2 login
        throw new UnsupportedOperationException("OAuth2 login for mentors not implemented yet");
    }

    /**
     * Registers a new mentor in the system.
     *
     * @param userDto Mentor DTO containing registration data
     * @return Registered mentor DTO
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

        // Convert saved entity to DTO
        MentorProfileDto createdMentorDto = convertToDto(mentor);
        
        // Generate JWT token for new user
        String accessToken = jwtTokenService.generateAccessToken(createdMentorDto);
        createdMentorDto.setToken(accessToken);
        
        log.info("Mentor registered successfully with email: {}, token generated", userDto.getEmail());
        return createdMentorDto;
    }

    /**
     * Creates a new MentorProfile entity from DTO.
     *
     * @param userDto Mentor DTO
     * @return Mentor entity
     */
    protected MentorProfile createUserEntity(MentorProfileDto userDto) {
        MentorProfile mentor = new MentorProfile();
        mentor.setEmail(userDto.getEmail());
        mentor.setPassword(passwordEncoder.encode(userDto.getPassword()));
        mentor.setName(userDto.getName());
        mentor.setExpertise(userDto.getExpertise());
        mentor.setBio(userDto.getBio());
        mentor.setExperienceYears(userDto.getExperienceYears());
        mentor.setIsAvailable(userDto.getIsAvailable());
        // Set email as verified for testing purposes
        mentor.setEmailVerified(true);
        return mentor;
    }

    /**
     * Converts MentorProfile entity to DTO.
     *
     * @param mentor Mentor entity
     * @return Mentor DTO
     */
    protected MentorProfileDto convertToDto(MentorProfile mentor) {
        return MentorProfileDto.builder()
                .id(mentor.getId())
                .name(mentor.getName())
                .email(mentor.getEmail())
                .expertise(mentor.getExpertise())
                .bio(mentor.getBio())
                .experienceYears(mentor.getExperienceYears())
                .isAvailable(mentor.getIsAvailable())
                .build();
    }
}