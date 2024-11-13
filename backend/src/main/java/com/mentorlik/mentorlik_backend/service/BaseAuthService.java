package com.mentorlik.mentorlik_backend.service;

import com.mentorlik.mentorlik_backend.dto.AuthRequestDto;
import com.mentorlik.mentorlik_backend.dto.UserDto;
import com.mentorlik.mentorlik_backend.exception.EmailAlreadyExistsException;
import com.mentorlik.mentorlik_backend.exception.ResourceNotFoundException;
import com.mentorlik.mentorlik_backend.model.User;
import com.mentorlik.mentorlik_backend.repository.AbstractUserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

/**
 * Abstract service providing common authentication and registration functionality.
 *
 * @param <T> The type of User entity.
 * @param <D> The type of User DTO.
 */
@Slf4j
public abstract class BaseAuthService<T extends User, D extends UserDto> {

    protected final AbstractUserRepository<T> userRepository;
    protected final PasswordEncoder passwordEncoder;

    protected BaseAuthService(AbstractUserRepository<T> userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Logs in a user by validating the provided credentials.
     *
     * @param authRequest The authentication request containing email and password.
     * @return A {@link UserDto} containing user information if login is successful.
     * @throws ResourceNotFoundException if the user is not found or credentials are invalid.
     */
    @Transactional(readOnly = true)
    public D login(AuthRequestDto authRequest) {
        log.info("Attempting login for user with email: {}", authRequest.getEmail());
        T user = userRepository.findByEmail(authRequest.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + authRequest.getEmail()));

        if (!passwordEncoder.matches(authRequest.getPassword(), user.getPassword())) {
            throw new ResourceNotFoundException("Invalid credentials provided");
        }

        return convertToDto(user);
    }

    /**
     * Registers a new user by saving the user information in the database.
     *
     * @param userDto The DTO containing user registration data.
     * @return A {@link UserDto} representing the registered user.
     * @throws EmailAlreadyExistsException if a user with the same email already exists.
     */
    @SuppressWarnings("unchecked")
    @Transactional
    public D register(UserDto userDto) {
        log.info("Registering new user with email: {}", userDto.getEmail());
        validateUserUniqueness(userDto.getEmail());

        T user = createUserEntity((D) userDto);
        userRepository.save(user);

        return convertToDto(user);
    }

    private void validateUserUniqueness(String email) {
        if (userRepository.findByEmail(email).isPresent()) {
            throw new EmailAlreadyExistsException("Email is already in use");
        }
    }

    /**
     * Creates a new User entity from the provided UserDto.
     *
     * @param userDto The DTO containing user data.
     * @return A new User entity.
     */
    protected abstract T createUserEntity(D userDto);

    /**
     * Converts a User entity to a UserDto.
     *
     * @param user The User entity to convert.
     * @return The converted UserDto.
     */
    protected abstract D convertToDto(T user);
}