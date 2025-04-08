package com.mentorlik.mentorlik_backend.repository;

import com.mentorlik.mentorlik_backend.model.AdminProfile;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for {@link AdminProfile} entity.
 * <p>
 * Extends the {@link AbstractUserRepository} to provide CRUD operations
 * and any admin-specific queries.
 * </p>
 */
@Repository
public interface AdminProfileRepository extends AbstractUserRepository<AdminProfile> {
    // Additional admin-specific methods can be added here
    
    /**
     * Проверяет существует ли администратор с указанным email.
     *
     * @param email email для проверки
     * @return true если администратор с указанным email существует, иначе false
     */
    boolean existsByEmail(String email);
}
