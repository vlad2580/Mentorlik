package com.mentorlik.mentorlik_backend.repository;

import com.mentorlik.mentorlik_backend.model.VerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository interface for {@link VerificationToken} entity.
 * <p>
 * Provides CRUD operations and custom queries for working with verification tokens.
 * </p>
 */
@Repository
public interface VerificationTokenRepository extends JpaRepository<VerificationToken, Long> {
    
    /**
     * Finds a verification token by its token value.
     *
     * @param token The token value to search for
     * @return An optional containing the found token, or empty if not found
     */
    Optional<VerificationToken> findByToken(String token);
    
    /**
     * Finds verification tokens by email address.
     *
     * @param email The email address to search for
     * @return An optional containing the found token, or empty if not found
     */
    Optional<VerificationToken> findByEmailAndUsed(String email, boolean used);
} 