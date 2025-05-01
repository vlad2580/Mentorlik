package com.mentorlik.mentorlik_backend.repository;

import com.mentorlik.mentorlik_backend.model.AdminProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository interface for {@link AdminProfile} entity.
 * <p>
 * Extends the {@link AbstractUserRepository} to provide CRUD operations
 * and any admin-specific queries.
 * </p>
 */
@Repository
public interface AdminProfileRepository extends JpaRepository<AdminProfile, Long> {
    Optional<AdminProfile> findByEmail(String email);
    
    /**
     * Проверяет существует ли администратор с указанным email.
     *
     * @param email email для проверки
     * @return true если администратор с указанным email существует, иначе false
     */
    boolean existsByEmail(String email);
}
