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
 * Controller for managing student data.
 * <p>
 * Provides endpoints for retrieving, creating, updating, and deleting
 * student information in the system.
 * </p>
 */
@Slf4j
@RestController
@RequestMapping("/api/students")
public class StudentController {

    private final StudentService studentService;

    /**
     * Creates an instance of the student controller.
     *
     * @param studentService service for student data operations
     */
    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    /**
     * Gets a list of all students.
     *
     * @return response with a list of all students
     */
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<List<StudentProfileDto>>> getAllStudents() {
        log.info("Request to get all students");
        List<StudentProfileDto> students = studentService.getAllStudents();
        return ResponseEntity.ok(ApiResponse.success(students));
    }

    /**
     * Gets a student by ID.
     *
     * @param id the student identifier
     * @return response with student information
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or @studentSecurityService.isStudentOwner(authentication, #id)")
    public ResponseEntity<ApiResponse<StudentProfileDto>> getStudentById(@PathVariable Long id) {
        log.info("Request to get student with ID: {}", id);
        StudentProfileDto student = studentService.getStudentById(id);
        return ResponseEntity.ok(ApiResponse.success(student));
    }

    /**
     * Updates student data.
     *
     * @param id the student identifier
     * @param studentDto student data for updating
     * @return response with updated student information
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('STUDENT') and @studentSecurityService.isStudentOwner(authentication, #id)")
    public ResponseEntity<ApiResponse<StudentProfileDto>> updateStudent(
            @PathVariable Long id,
            @Valid @RequestBody StudentProfileDto studentDto) {
        log.info("Request to update student with ID: {}", id);
        StudentProfileDto updatedStudent = studentService.updateStudent(id, studentDto);
        return ResponseEntity.ok(ApiResponse.success(updatedStudent, "Student data successfully updated"));
    }

    /**
     * Deletes a student from the system.
     *
     * @param id the student identifier
     * @return response with deletion confirmation
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or (hasRole('STUDENT') and @studentSecurityService.isStudentOwner(authentication, #id))")
    public ResponseEntity<ApiResponse<Void>> deleteStudent(@PathVariable Long id) {
        log.info("Request to delete student with ID: {}", id);
        studentService.deleteStudent(id);
        return ResponseEntity.ok(ApiResponse.success(null, "Student successfully deleted"));
    }

    /**
     * Searches for students based on specified criteria.
     *
     * @param query search string (name, study field, etc.)
     * @return response with a list of found students
     */
    @GetMapping("/search")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MENTOR')")
    public ResponseEntity<ApiResponse<List<StudentProfileDto>>> searchStudents(@RequestParam String query) {
        log.info("Request to search students with query: {}", query);
        List<StudentProfileDto> students = studentService.searchStudents(query);
        return ResponseEntity.ok(ApiResponse.success(students));
    }

    /**
     * Creates a new student.
     *
     * @param studentDto new student data
     * @return response with information about the created student
     */
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<StudentProfileDto>> createStudent(@Valid @RequestBody StudentProfileDto studentDto) {
        log.info("Request to create a new student");
        StudentProfileDto createdStudent = studentService.createStudent(studentDto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success(createdStudent, "Student successfully created"));
    }
} 