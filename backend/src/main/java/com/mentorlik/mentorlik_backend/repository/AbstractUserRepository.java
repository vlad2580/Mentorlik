package com.mentorlik.mentorlik_backend.repository;

import com.mentorlik.mentorlik_backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.Optional;

/**
 * Abstract repository interface for common user operations.
 * <p>
 * This interface provides shared methods for different types of user profiles
 * (e.g., Mentor, Student, Admin) by extending the base JPA repository.
 * </p>
 * <p>
 * It is annotated with {@code @NoRepositoryBean} to ensure that Spring Data JPA
 * does not create an instance of this interface directly.
 * </p>
 */
@NoRepositoryBean
public interface AbstractUserRepository<T extends User> extends JpaRepository<T, Long> {

    /**
     * Finds a user by email. This is a common method for all user types.
     *
     * @param email the email to search for
     * @return an {@link Optional} containing the user, if found
     */
    Optional<T> findByEmail(String email);

    // Add any other common methods you want across User types here
}