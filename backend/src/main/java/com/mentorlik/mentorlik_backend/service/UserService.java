package com.mentorlik.mentorlik_backend.service;

import com.mentorlik.mentorlik_backend.dto.UserDto;
import com.mentorlik.mentorlik_backend.model.User;
import com.mentorlik.mentorlik_backend.repository.UserRepository;
import org.springframework.stereotype.Service;

/**
 * Service class for managing user-related operations.
 * <p>
 * Provides methods for retrieving user information and
 * converting entities to data transfer objects (DTOs).
 * </p>
 */
@Service
public class UserService {

    private final UserRepository userRepository;

    /**
     * Constructs a new {@code UserService} with the specified {@code UserRepository}.
     *
     * @param userRepository the repository used for user data access
     */
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Retrieves a user by their ID.
     * <p>
     * If the user is not found, this method throws a {@code RuntimeException}.
     * </p>
     *
     * @param id the ID of the user to retrieve
     * @return a {@link UserDto} containing the user's data
     * @throws RuntimeException if the user with the specified ID is not found
     */
    public UserDto getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return new UserDto(user.getId(), user.getEmail());
    }
}
