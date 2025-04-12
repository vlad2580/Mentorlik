package com.mentorlik.mentorlik_backend.infrastructure.persistence;

import com.mentorlik.mentorlik_backend.domain.entity.AdminProfile;
import com.mentorlik.mentorlik_backend.domain.mapper.AdminProfileMapper;
import com.mentorlik.mentorlik_backend.domain.repository.AdminProfileRepository;
import com.mentorlik.mentorlik_backend.repository.AdminProfileJpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Implementation of the AdminProfileRepository interface.
 * <p>
 * This class adapts the Spring Data JPA repository to the domain repository interface,
 * implementing the dependency inversion principle by having infrastructure depend on domain.
 * </p>
 */
@Repository
public class AdminProfileRepositoryImpl implements AdminProfileRepository {

    private final AdminProfileJpaRepository jpaRepository;
    private final AdminProfileMapper mapper;

    /**
     * Constructs a new AdminProfileRepositoryImpl with the specified JPA repository and mapper.
     *
     * @param jpaRepository the JPA repository to delegate operations to
     * @param mapper the mapper for converting between domain and JPA entities
     */
    public AdminProfileRepositoryImpl(AdminProfileJpaRepository jpaRepository, AdminProfileMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public Optional<AdminProfile> findById(Long id) {
        return jpaRepository.findById(id)
                .map(mapper::toDomainEntity);
    }

    @Override
    public AdminProfile save(AdminProfile adminProfile) {
        // Convert domain entity to JPA entity
        com.mentorlik.mentorlik_backend.model.AdminProfile jpaEntity = mapper.toJpaEntity(adminProfile);
        // Save and convert back to domain entity
        return mapper.toDomainEntity(jpaRepository.save(jpaEntity));
    }

    @Override
    public boolean existsByEmail(String email) {
        return jpaRepository.existsByEmail(email);
    }

    @Override
    public Optional<AdminProfile> findByEmail(String email) {
        return jpaRepository.findByEmail(email)
                .map(mapper::toDomainEntity);
    }
} 