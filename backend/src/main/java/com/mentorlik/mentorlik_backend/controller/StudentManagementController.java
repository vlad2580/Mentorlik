package com.mentorlik.mentorlik_backend.controller;

import com.mentorlik.mentorlik_backend.dto.ApiResponse;
import com.mentorlik.mentorlik_backend.dto.profile.StudentProfileDto;
import com.mentorlik.mentorlik_backend.service.StudentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * REST controller for admin and student operations on student profiles.
 */
@Slf4j
@RestController
@RequestMapping("/api/admin/students")
@RequiredArgsConstructor
public class StudentManagementController {

    private final StudentService studentService;

    /**
     * Retrieves all students. Admin only.
     */
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<List<StudentProfileDto>>> getAll() {
        log.info("Fetching all students");
        List<StudentProfileDto> list = studentService.getAllStudents();
        return ResponseEntity.ok(ApiResponse.success(list));
    }

    /**
     * Retrieves a student by ID. Admin or owner.
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or @studentSecurityService.isStudentOwner(authentication, #id)")
    public ResponseEntity<ApiResponse<StudentProfileDto>> getById(@PathVariable Long id) {
        log.info("Fetching student id={}", id);
        StudentProfileDto dto = studentService.getStudentById(id);
        return ResponseEntity.ok(ApiResponse.success(dto));
    }

    /**
     * Searches students by query. Admin or mentor.
     */
    @GetMapping("/search")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MENTOR')")
    public ResponseEntity<ApiResponse<List<StudentProfileDto>>> search(@RequestParam String query) {
        log.info("Searching students with query='{}'", query);
        List<StudentProfileDto> results = studentService.searchStudents(query);
        return ResponseEntity.ok(ApiResponse.success(results));
    }

    /**
     * Creates a new student. Admin only.
     */
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<StudentProfileDto>> create(
            @Valid @RequestBody StudentProfileDto dto) {
        log.info("Creating student email={}", dto.getEmail());
        StudentProfileDto created = studentService.createStudent(dto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success(created, "Student created"));
    }

    /**
     * Updates student data. Student owner only.
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('STUDENT') and @studentSecurityService.isStudentOwner(authentication, #id)")
    public ResponseEntity<ApiResponse<StudentProfileDto>> update(
            @PathVariable Long id,
            @Valid @RequestBody StudentProfileDto dto) {
        log.info("Updating student id={}", id);
        StudentProfileDto updated = studentService.updateStudent(id, dto);
        return ResponseEntity.ok(ApiResponse.success(updated, "Student data updated"));
    }

    /**
     * Deletes a student. Admin or owner.
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or (hasRole('STUDENT') and @studentSecurityService.isStudentOwner(authentication, #id))")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        log.info("Deleting student id={}", id);
        studentService.deleteStudent(id);
        return ResponseEntity.ok(ApiResponse.success(null, "Student deleted"));
    }
}