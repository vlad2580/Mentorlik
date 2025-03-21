package com.mentorlik.mentorlik_backend.controller;

import com.mentorlik.mentorlik_backend.dto.ApiResponse;
import com.mentorlik.mentorlik_backend.dto.profile.StudentProfileDto;
import com.mentorlik.mentorlik_backend.service.StudentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * Контроллер для управления данными студентов.
 * <p>
 * Предоставляет эндпоинты для получения, создания, обновления и удаления
 * информации о студентах в системе.
 * </p>
 */
@Slf4j
@RestController
@RequestMapping("/api/students")
public class StudentController {

    private final StudentService studentService;

    /**
     * Создает экземпляр контроллера студентов.
     *
     * @param studentService сервис для работы с данными студентов
     */
    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    /**
     * Получает список всех студентов.
     *
     * @return ответ со списком всех студентов
     */
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<List<StudentProfileDto>>> getAllStudents() {
        log.info("Запрос на получение всех студентов");
        List<StudentProfileDto> students = studentService.getAllStudents();
        return ResponseEntity.ok(ApiResponse.success(students));
    }

    /**
     * Получает студента по ID.
     *
     * @param id идентификатор студента
     * @return ответ с информацией о студенте
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or @studentSecurityService.isStudentOwner(authentication, #id)")
    public ResponseEntity<ApiResponse<StudentProfileDto>> getStudentById(@PathVariable Long id) {
        log.info("Запрос на получение студента с ID: {}", id);
        StudentProfileDto student = studentService.getStudentById(id);
        return ResponseEntity.ok(ApiResponse.success(student));
    }

    /**
     * Обновляет данные студента.
     *
     * @param id идентификатор студента
     * @param studentDto данные студента для обновления
     * @return ответ с обновленной информацией о студенте
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('STUDENT') and @studentSecurityService.isStudentOwner(authentication, #id)")
    public ResponseEntity<ApiResponse<StudentProfileDto>> updateStudent(
            @PathVariable Long id,
            @Valid @RequestBody StudentProfileDto studentDto) {
        log.info("Запрос на обновление студента с ID: {}", id);
        StudentProfileDto updatedStudent = studentService.updateStudent(id, studentDto);
        return ResponseEntity.ok(ApiResponse.success(updatedStudent, "Данные студента успешно обновлены"));
    }

    /**
     * Удаляет студента из системы.
     *
     * @param id идентификатор студента
     * @return ответ с подтверждением удаления
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or (hasRole('STUDENT') and @studentSecurityService.isStudentOwner(authentication, #id))")
    public ResponseEntity<ApiResponse<Void>> deleteStudent(@PathVariable Long id) {
        log.info("Запрос на удаление студента с ID: {}", id);
        studentService.deleteStudent(id);
        return ResponseEntity.ok(ApiResponse.success(null, "Студент успешно удален"));
    }

    /**
     * Ищет студентов по заданным критериям.
     *
     * @param query строка поиска (имя, учебное направление и т.д.)
     * @return ответ со списком найденных студентов
     */
    @GetMapping("/search")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MENTOR')")
    public ResponseEntity<ApiResponse<List<StudentProfileDto>>> searchStudents(@RequestParam String query) {
        log.info("Запрос на поиск студентов по запросу: {}", query);
        List<StudentProfileDto> students = studentService.searchStudents(query);
        return ResponseEntity.ok(ApiResponse.success(students));
    }

    /**
     * Создает нового студента.
     *
     * @param studentDto данные нового студента
     * @return ответ с информацией о созданном студенте
     */
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<StudentProfileDto>> createStudent(@Valid @RequestBody StudentProfileDto studentDto) {
        log.info("Запрос на создание нового студента");
        StudentProfileDto createdStudent = studentService.createStudent(studentDto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success(createdStudent, "Студент успешно создан"));
    }
} 