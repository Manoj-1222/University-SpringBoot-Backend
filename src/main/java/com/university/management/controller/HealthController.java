package com.university.management.controller;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.university.management.dto.response.ApiResponse;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
public class HealthController {
    
    @GetMapping("/health")
    public ResponseEntity<ApiResponse<Map<String, Object>>> healthCheck() {
        Map<String, Object> healthInfo = new HashMap<>();
        healthInfo.put("status", "UP");
        healthInfo.put("timestamp", LocalDateTime.now());
        healthInfo.put("service", "University Management System - Spring Boot Backend");
        healthInfo.put("version", "1.0.0");
        
        return ResponseEntity.ok(ApiResponse.success("Service is healthy", healthInfo));
    }
    
    @GetMapping("/")
    public ResponseEntity<ApiResponse<String>> root() {
        return ResponseEntity.ok(ApiResponse.success("University Management System API is running"));
    }
    
    @GetMapping("/api")
    public ResponseEntity<ApiResponse<Map<String, Object>>> apiRoot() {
        Map<String, Object> apiInfo = new HashMap<>();
        apiInfo.put("message", "University Management System API");
        apiInfo.put("version", "1.0.0");
        apiInfo.put("status", "active");
        apiInfo.put("endpoints", new String[]{
            "/api/health - Health check",
            "/api/admin/auth/login - Admin login",
            "/api/students - Student operations (requires auth)",
            "/api/admissions - Admission operations"
        });
        
        return ResponseEntity.ok(ApiResponse.success("API is running", apiInfo));
    }
}
