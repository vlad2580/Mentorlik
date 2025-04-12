package com.mentorlik.mentorlik_backend.repository;

import com.mentorlik.mentorlik_backend.model.AdminProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Spring Data JPA repository for AdminProfile entities.
 */
@Repository
public interface AdminProfileJpaRepository extends JpaRepository<AdminProfile, Long> {
    Optional<AdminProfile> findByEmail(String email);
    boolean existsByEmail(String email);
} 