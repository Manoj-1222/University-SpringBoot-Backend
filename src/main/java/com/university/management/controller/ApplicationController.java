package com.university.management.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.university.management.dto.ApplicationRequestDto;
import com.university.management.dto.ApplicationReviewDto;
import com.university.management.model.Admin;
import com.university.management.model.Application;
import com.university.management.service.ApplicationService;
import com.university.management.service.ApplicationService.ApplicationStats;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/applications")
@CrossOrigin(origins = "*")
public class ApplicationController {
    
    @Autowired
    private ApplicationService applicationService;
    
    /**
     * Submit new admission application (Public endpoint)
     * POST /api/applications/submit
     */
    @PostMapping("/submit")
    public ResponseEntity<com.university.management.dto.response.ApiResponse<ApplicationResponse>> submitApplication(@Valid @RequestBody ApplicationRequestDto applicationDto) {
        try {
            Application application = applicationService.submitApplication(applicationDto);
            
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(com.university.management.dto.response.ApiResponse.success("Application submitted successfully! You will receive confirmation shortly.", 
                                            new ApplicationResponse(application)));
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(com.university.management.dto.response.ApiResponse.error("Failed to submit application", e.getMessage()));
        }
    }
    
    /**
     * Get all applications (SUPER_ADMIN only)
     * GET /api/applications
     */
    @GetMapping
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<?> getAllApplications() {
        try {
            List<Application> applications = applicationService.getAllApplications();
            
            return ResponseEntity.ok(new ApiResponse(
                true,
                "Applications retrieved successfully",
                applications.stream().map(ApplicationResponse::new).toList()
            ));
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(
                false,
                "Failed to retrieve applications: " + e.getMessage(),
                null
            ));
        }
    }
    
    /**
     * Get applications by status (SUPER_ADMIN only)
     * GET /api/applications/status/{status}
     */
    @GetMapping("/status/{status}")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<?> getApplicationsByStatus(@PathVariable String status) {
        try {
            List<Application> applications = applicationService.getApplicationsByStatus(status.toUpperCase());
            
            return ResponseEntity.ok(new ApiResponse(
                true,
                "Applications retrieved successfully",
                applications.stream().map(ApplicationResponse::new).toList()
            ));
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(
                false,
                "Failed to retrieve applications: " + e.getMessage(),
                null
            ));
        }
    }
    
    /**
     * Get application by ID (SUPER_ADMIN only)
     * GET /api/applications/{id}
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<?> getApplicationById(@PathVariable String id) {
        try {
            Optional<Application> application = applicationService.getApplicationById(id);
            
            if (application.isPresent()) {
                return ResponseEntity.ok(new ApiResponse(
                    true,
                    "Application retrieved successfully",
                    new ApplicationResponse(application.get())
                ));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(
                    false,
                    "Application not found",
                    null
                ));
            }
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(
                false,
                "Failed to retrieve application: " + e.getMessage(),
                null
            ));
        }
    }
    
    /**
     * Get applications by course (SUPER_ADMIN only)
     * GET /api/applications/course/{course}
     */
    @GetMapping("/course/{course}")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<?> getApplicationsByCourse(@PathVariable String course) {
        try {
            List<Application> applications = applicationService.getApplicationsByCourse(course);
            
            return ResponseEntity.ok(new ApiResponse(
                true,
                "Applications retrieved successfully",
                applications.stream().map(ApplicationResponse::new).toList()
            ));
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(
                false,
                "Failed to retrieve applications: " + e.getMessage(),
                null
            ));
        }
    }
    
    /**
     * Review application - Accept or Reject (SUPER_ADMIN only)
     * PUT /api/applications/{id}/review
     */
    @PutMapping("/{id}/review")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<?> reviewApplication(@PathVariable String id, 
                                             @Valid @RequestBody ApplicationReviewDto reviewDto,
                                             @AuthenticationPrincipal Admin admin) {
        try {
            // Validate required fields for rejection
            if ("REJECTED".equals(reviewDto.getApplicationStatus()) && 
                (reviewDto.getRejectionReason() == null || reviewDto.getRejectionReason().trim().isEmpty())) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse(
                    false,
                    "Rejection reason is required when rejecting an application",
                    null
                ));
            }

            Application application = applicationService.reviewApplication(id, reviewDto, admin);

            String message = switch (reviewDto.getApplicationStatus()) {
                case "APPROVED" -> "✅ Application approved successfully! Student account created and credentials sent via email.";
                case "REJECTED" -> "❌ Application rejected successfully. Application status updated and rejection email sent.";
                default -> "Application status updated successfully.";
            };            return ResponseEntity.ok(new ApiResponse(
                true,
                message,
                new ApplicationResponse(application)
            ));
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse(
                false,
                e.getMessage(),
                null
            ));
        }
    }
    
    /**
     * Get application statistics (SUPER_ADMIN only)
     * GET /api/applications/stats
     */
    @GetMapping("/stats")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<?> getApplicationStats() {
        try {
            ApplicationStats stats = applicationService.getApplicationStats();
            
            return ResponseEntity.ok(new ApiResponse(
                true,
                "Application statistics retrieved successfully",
                stats
            ));
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(
                false,
                "Failed to retrieve application statistics: " + e.getMessage(),
                null
            ));
        }
    }
    
    // Response DTOs
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
    
    public static class ApplicationResponse {
        private String id;
        private String fullName;
        private String email;
        private String phoneNumber;
        private String desiredCourse;
        private String previousQualification;
        private String previousGrade;
        private String applicationStatus;
        private String rejectionReason;
        private String reviewComments;
        private String applicationDate;
        private String updatedAt;
        
        public ApplicationResponse(Application application) {
            this.id = application.getId();
            this.fullName = application.getFullName();
            this.email = application.getEmail();
            this.phoneNumber = application.getPhoneNumber();
            this.desiredCourse = application.getDesiredCourse();
            this.previousQualification = application.getPreviousQualification();
            this.previousGrade = application.getPreviousGrade();
            this.applicationStatus = application.getApplicationStatus();
            this.rejectionReason = application.getRejectionReason();
            this.reviewComments = application.getReviewComments();
            this.applicationDate = application.getApplicationDate() != null ? application.getApplicationDate().toString() : null;
            this.updatedAt = application.getUpdatedAt() != null ? application.getUpdatedAt().toString() : null;
        }
        
        // Getters
        public String getId() { return id; }
        public String getFullName() { return fullName; }
        public String getEmail() { return email; }
        public String getPhoneNumber() { return phoneNumber; }
        public String getDesiredCourse() { return desiredCourse; }
        public String getPreviousQualification() { return previousQualification; }
        public String getPreviousGrade() { return previousGrade; }
        public String getApplicationStatus() { return applicationStatus; }
        public String getRejectionReason() { return rejectionReason; }
        public String getReviewComments() { return reviewComments; }
        public String getApplicationDate() { return applicationDate; }
        public String getUpdatedAt() { return updatedAt; }
    }
}
