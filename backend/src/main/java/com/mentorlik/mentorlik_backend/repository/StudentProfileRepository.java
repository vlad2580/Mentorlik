package com.mentorlik.mentorlik_backend.repository;

import com.mentorlik.mentorlik_backend.model.StudentProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Репозиторий для работы с данными студентов.
 * <p>
 * Предоставляет методы доступа к базе данных для сущностей StudentProfile.
 * </p>
 */
@Repository
public interface StudentProfileRepository extends JpaRepository<StudentProfile, Long> {
    
    /**
     * Находит студента по email.
     *
     * @param email email студента
     * @return Optional со студентом, если найден
     */
    Optional<StudentProfile> findByEmail(String email);
    
    /**
     * Ищет студентов по имени или области обучения, используя поиск без учета регистра.
     *
     * @param name часть имени для поиска
     * @param fieldOfStudy часть области обучения для поиска
     * @return список найденных студентов
     */
    List<StudentProfile> findByNameContainingIgnoreCaseOrFieldOfStudyContainingIgnoreCase(String name, String fieldOfStudy);
    
    /**
     * Находит студентов по уровню образования.
     *
     * @param educationLevel уровень образования
     * @return список найденных студентов
     */
    List<StudentProfile> findByEducationLevel(String educationLevel);
    
    /**
     * Ищет студентов по нескольким критериям с помощью JPQL-запроса.
     *
     * @param searchTerm общий поисковый термин
     * @param educationLevel уровень образования (опционально)
     * @return список найденных студентов
     */
    @Query("SELECT s FROM StudentProfile s WHERE " +
           "(LOWER(s.name) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(s.fieldOfStudy) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(s.about) LIKE LOWER(CONCAT('%', :searchTerm, '%'))) " +
           "AND (:educationLevel IS NULL OR s.educationLevel = :educationLevel)")
    List<StudentProfile> searchStudents(
            @Param("searchTerm") String searchTerm, 
            @Param("educationLevel") String educationLevel);
}