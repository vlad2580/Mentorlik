package com.mentorlik.mentorlik_backend.service;

import com.mentorlik.mentorlik_backend.dto.AuthRequestDto;
import com.mentorlik.mentorlik_backend.dto.UserDto;
import com.mentorlik.mentorlik_backend.exception.EmailAlreadyExistsException;
import com.mentorlik.mentorlik_backend.exception.ResourceNotFoundException;
import com.mentorlik.mentorlik_backend.model.User;
import com.mentorlik.mentorlik_backend.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service class for handling authentication and registration logic.
 * Provides methods to login and register users. Contains business logic
 * for validating credentials and managing user persistence.
 */
@Slf4j
@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * Constructs an instance of {@code AuthService} with the required {@code UserRepository} and {@code PasswordEncoder}.
     *
     * @param userRepository the repository used to manage {@code User} data
     * @param passwordEncoder the encoder used for hashing passwords
     */
    @Autowired
    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Logs in a user by validating the provided credentials.
     *
     * @param authRequest the authentication request containing the email and password
     * @return a {@link UserDto} containing user information if login is successful
     * @throws ResourceNotFoundException if the user is not found or credentials are invalid
     */
    @Transactional(readOnly = true)
    public UserDto login(AuthRequestDto authRequest) {
        log.info("Attempting login for user with email: {}", authRequest.getEmail());

        final User user = userRepository.findByEmail(authRequest.getEmail())
                .orElseThrow(() -> {
                    log.warn("Login failed for user with email {}: User not found", authRequest.getEmail());
                    return new ResourceNotFoundException("User not found with email: " + authRequest.getEmail());
                });

        if (!passwordEncoder.matches(authRequest.getPassword(), user.getPassword())) {
            log.warn("Login failed for user with email {}: Incorrect password", authRequest.getEmail());
            throw new ResourceNotFoundException("Invalid credentials provided");
        }

        log.info("User with email {} successfully logged in", authRequest.getEmail());
        return convertToDto(user);
    }

    /**
     * Registers a new user by saving the user information in the database.
     *
     * @param userDto the {@code UserDto} containing registration information
     * @return a {@link UserDto} containing the newly registered user's information
     * @throws IllegalArgumentException if a user with the same email already exists
     */
    @Transactional
    public UserDto register(UserDto userDto) {
        log.info("Attempting to register new user with email: {}", userDto.getEmail());

        validateUserUniqueness(userDto.getEmail());

        final User user = new User();
        user.setEmail(userDto.getEmail());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));

        userRepository.save(user);

        log.info("User with email {} successfully registered", userDto.getEmail());
        return convertToDto(user);
    }

    /**
     * Checks if a user with the given email already exists in the database.
     *
     * @param email the email to check for uniqueness
     * @throws IllegalArgumentException if a user with the same email already exists
     */
    private void validateUserUniqueness(String email) {
        if (userRepository.findByEmail(email).isPresent()) {
            log.warn("Registration failed: Email {} is already in use", email);
            throw new EmailAlreadyExistsException("Email is already in use");
        }
    }

    /**
     * Converts a {@link User} entity to a {@link UserDto}.
     *
     * @param user the {@code User} entity to convert
     * @return the converted {@code UserDto}
     */
    private UserDto convertToDto(User user) {
        return new UserDto(user.getId(), user.getEmail());
    }
}