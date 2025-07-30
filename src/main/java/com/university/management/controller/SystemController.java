package com.university.management.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.university.management.repository.AdminRepository;
import com.university.management.repository.ApplicationRepository;
import com.university.management.repository.CourseRepository;
import com.university.management.repository.StudentRepository;

@RestController
@RequestMapping("/system")
@CrossOrigin(origins = "*")
public class SystemController {
    
    @Autowired
    private AdminRepository adminRepository;
    
    @Autowired
    private ApplicationRepository applicationRepository;
    
    @Autowired
    private CourseRepository courseRepository;
    
    @Autowired
    private StudentRepository studentRepository;
    
    /**
     * Check database collections and their counts
     * GET /api/system/database-status
     */
    @GetMapping("/database-status")
    public ResponseEntity<?> getDatabaseStatus() {
        try {
            Map<String, Object> status = new HashMap<>();
            
            // Collection counts
            Map<String, Long> collections = new HashMap<>();
            collections.put("admins", adminRepository.count());
            collections.put("applications", applicationRepository.count());
            collections.put("courses", courseRepository.count());
            collections.put("students", studentRepository.count());
            
            status.put("collections", collections);
            status.put("totalCollections", collections.size());
            status.put("databaseName", "University");
            status.put("status", "Connected");
            
            // Check if initial data exists
            Map<String, Boolean> dataStatus = new HashMap<>();
            dataStatus.put("adminsInitialized", adminRepository.count() > 0);
            dataStatus.put("coursesInitialized", courseRepository.count() > 0);
            dataStatus.put("readyForApplications", true);
            dataStatus.put("readyForStudents", true);
            
            status.put("initialization", dataStatus);
            
            return ResponseEntity.ok(new ApiResponse(
                true,
                "Database status retrieved successfully",
                status
            ));
            
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ApiResponse(
                false,
                "Failed to retrieve database status: " + e.getMessage(),
                null
            ));
        }
    }
    
    /**
     * Get list of admin users with basic info
     * GET /api/system/admin-list
     */
    @GetMapping("/admin-list")
    public ResponseEntity<?> getAdminList() {
        try {
            var admins = adminRepository.findAll();
            
            var adminInfo = admins.stream().map(admin -> {
                Map<String, Object> info = new HashMap<>();
                info.put("id", admin.getId());
                info.put("name", admin.getName());
                info.put("username", admin.getUsername());
                info.put("email", admin.getEmail());
                info.put("role", admin.getRole());
                info.put("department", admin.getDepartment());
                info.put("isActive", admin.isActive());
                info.put("isSuperAdmin", admin.isSuperAdmin());
                return info;
            }).toList();
            
            return ResponseEntity.ok(new ApiResponse(
                true,
                "Admin list retrieved successfully",
                adminInfo
            ));
            
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ApiResponse(
                false,
                "Failed to retrieve admin list: " + e.getMessage(),
                null
            ));
        }
    }
    
    // Response class
    public static class ApiResponse {
        private boolean success;
        private String message;
        private Object data;
        
        public ApiResponse(boolean success, String message, Object data) {
            this.success = success;
            this.message = message;
            this.data = data;
        }
        
        // Getters
        public boolean isSuccess() { return success; }
        public String getMessage() { return message; }
        public Object getData() { return data; }
    }
}
