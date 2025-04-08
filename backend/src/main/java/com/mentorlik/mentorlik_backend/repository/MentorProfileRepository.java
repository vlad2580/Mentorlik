package com.mentorlik.mentorlik_backend.repository;

import com.mentorlik.mentorlik_backend.model.MentorProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for managing {@link MentorProfile} entities.
 * <p>
 * This interface extends the {@link AbstractUserRepository} to inherit
 * common user operations and provides a layer of abstraction for accessing
 * and managing Mentor-specific data in the database.
 * </p>
 * <p>
 * This repository is a part of the persistence layer and is responsible for
 * querying and managing Mentor profile data, enabling integration with the
 * data access layer and the application business logic.
 * </p>
 */
@Repository
public interface MentorProfileRepository extends JpaRepository<MentorProfile, Long> {

    // Define any Mentor-specific data access methods here if needed

    /**
     * Ищет менторов по имени или области экспертизы, используя поиск без учета регистра.
     *
     * @param name часть имени для поиска
     * @param expertise часть области экспертизы для поиска
     * @return список найденных менторов
     */
    List<MentorProfile> findByNameContainingIgnoreCaseOrExpertiseContainingIgnoreCase(String name, String expertise);

    /**
     * Находит ментора по email.
     *
     * @param email email ментора
     * @return Optional с ментором, если найден
     */
    Optional<MentorProfile> findByEmail(String email);

    /**
     * Проверяет существует ли ментор с указанным email.
     *
     * @param email email для проверки
     * @return true если ментор с указанным email существует, иначе false
     */
    boolean existsByEmail(String email);

}