package com.mentorlik.mentorlik_backend.repository;

import com.mentorlik.mentorlik_backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository interface for {@link User} entity.
 * <p>
 * This interface extends {@code JpaRepository} to provide standard CRUD operations.
 * Additional methods can be defined here for specific queries.
 * </p>
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Finds a user by their email.
     *
     * @param email the email to search for
     * @return an {@link Optional} containing the {@link User} if found, or {@link Optional#empty()} if not found
     */
    Optional<User> findByEmail(String email);
}