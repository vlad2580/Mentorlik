package com.mentorlik.mentorlik_backend.api.controller;

import com.mentorlik.mentorlik_backend.api.dto.AdminProfileDto;
import com.mentorlik.mentorlik_backend.application.AdminService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for admin-related endpoints.
 * <p>
 * This controller handles HTTP requests related to admin operations,
 * delegating business logic to the application service layer.
 * </p>
 */
@RestController
@RequestMapping("/api/admin")
public class AdminController {
    
    private final AdminService adminService;
    
    /**
     * Constructs a new AdminController with the specified service.
     *
     * @param adminService the service for admin operations
     */
    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }
    
    /**
     * Retrieves an admin by their ID.
     *
     * @param id the ID of the admin to retrieve
     * @return a ResponseEntity containing the admin's data if found
     */
    @GetMapping("/{id}")
    public ResponseEntity<AdminProfileDto> getAdminById(@PathVariable Long id) {
        AdminProfileDto adminDto = adminService.getAdminById(id);
        return ResponseEntity.ok(adminDto);
    }
} 