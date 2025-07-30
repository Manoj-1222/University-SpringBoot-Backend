package com.university.management.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.university.management.model.Course;
import com.university.management.service.CourseService;
import com.university.management.service.CourseService.CourseStats;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/courses")
@CrossOrigin(origins = "*")
public class CourseController {
    
    @Autowired
    private CourseService courseService;
    
    /**
     * Get all available courses (Public endpoint for application form)
     * GET /api/courses/available
     */
    @GetMapping("/available")
    public ResponseEntity<?> getAvailableCourses() {
        try {
            List<Course> courses = courseService.getAvailableCourses();
            
            return ResponseEntity.ok(new ApiResponse(
                true,
                "Available courses retrieved successfully",
                courses.stream().map(CourseResponse::new).toList()
            ));
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(
                false,
                "Failed to retrieve available courses: " + e.getMessage(),
                null
            ));
        }
    }
    
    /**
     * Get all active courses (Public endpoint)
     * GET /api/courses/active
     */
    @GetMapping("/active")
    public ResponseEntity<?> getAllActiveCourses() {
        try {
            List<Course> courses = courseService.getAllActiveCourses();
            
            return ResponseEntity.ok(new ApiResponse(
                true,
                "Active courses retrieved successfully",
                courses.stream().map(CourseResponse::new).toList()
            ));
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(
                false,
                "Failed to retrieve active courses: " + e.getMessage(),
                null
            ));
        }
    }
    
    /**
     * Get all courses (Admin only)
     * GET /api/courses
     */
    @GetMapping
    @PreAuthorize("hasRole('SUPER_ADMIN') or hasRole('STAFF_ADMIN')")
    public ResponseEntity<?> getAllCourses() {
        try {
            List<Course> courses = courseService.getAllCourses();
            
            return ResponseEntity.ok(new ApiResponse(
                true,
                "All courses retrieved successfully",
                courses.stream().map(CourseResponse::new).toList()
            ));
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(
                false,
                "Failed to retrieve courses: " + e.getMessage(),
                null
            ));
        }
    }
    
    /**
     * Get courses by department (Public endpoint)
     * GET /api/courses/department/{department}
     */
    @GetMapping("/department/{department}")
    public ResponseEntity<?> getCoursesByDepartment(@PathVariable String department) {
        try {
            List<Course> courses = courseService.getCoursesByDepartment(department);
            
            return ResponseEntity.ok(new ApiResponse(
                true,
                "Courses retrieved successfully",
                courses.stream().map(CourseResponse::new).toList()
            ));
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(
                false,
                "Failed to retrieve courses: " + e.getMessage(),
                null
            ));
        }
    }
    
    /**
     * Get courses by program type (Public endpoint)
     * GET /api/courses/program/{programType}
     */
    @GetMapping("/program/{programType}")
    public ResponseEntity<?> getCoursesByProgramType(@PathVariable String programType) {
        try {
            List<Course> courses = courseService.getCoursesByProgramType(programType);
            
            return ResponseEntity.ok(new ApiResponse(
                true,
                "Courses retrieved successfully",
                courses.stream().map(CourseResponse::new).toList()
            ));
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(
                false,
                "Failed to retrieve courses: " + e.getMessage(),
                null
            ));
        }
    }
    
    /**
     * Get course by ID (Public endpoint)
     * GET /api/courses/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getCourseById(@PathVariable String id) {
        try {
            Optional<Course> course = courseService.getCourseById(id);
            
            if (course.isPresent()) {
                return ResponseEntity.ok(new ApiResponse(
                    true,
                    "Course retrieved successfully",
                    new CourseResponse(course.get())
                ));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(
                    false,
                    "Course not found",
                    null
                ));
            }
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(
                false,
                "Failed to retrieve course: " + e.getMessage(),
                null
            ));
        }
    }
    
    /**
     * Create new course (Admin only)
     * POST /api/courses
     */
    @PostMapping
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<?> createCourse(@Valid @RequestBody Course course) {
        try {
            Course createdCourse = courseService.createCourse(course);
            
            return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse(
                true,
                "Course created successfully",
                new CourseResponse(createdCourse)
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
     * Update course (Admin only)
     * PUT /api/courses/{id}
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<?> updateCourse(@PathVariable String id, @RequestBody Course courseDetails) {
        try {
            Course updatedCourse = courseService.updateCourse(id, courseDetails);
            
            return ResponseEntity.ok(new ApiResponse(
                true,
                "Course updated successfully",
                new CourseResponse(updatedCourse)
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
     * Delete course (Admin only)
     * DELETE /api/courses/{id}
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<?> deleteCourse(@PathVariable String id) {
        try {
            courseService.deleteCourse(id);
            
            return ResponseEntity.ok(new ApiResponse(
                true,
                "Course deleted successfully",
                null
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
     * Toggle course active status (Admin only)
     * PUT /api/courses/{id}/toggle-status
     */
    @PutMapping("/{id}/toggle-status")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<?> toggleCourseStatus(@PathVariable String id) {
        try {
            Course course = courseService.toggleCourseStatus(id);
            
            String status = course.getIsActive() ? "activated" : "deactivated";
            return ResponseEntity.ok(new ApiResponse(
                true,
                "Course " + status + " successfully",
                new CourseResponse(course)
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
     * Get course statistics (Admin only)
     * GET /api/courses/stats
     */
    @GetMapping("/stats")
    @PreAuthorize("hasRole('SUPER_ADMIN') or hasRole('STAFF_ADMIN')")
    public ResponseEntity<?> getCourseStats() {
        try {
            CourseStats stats = courseService.getCourseStats();
            
            return ResponseEntity.ok(new ApiResponse(
                true,
                "Course statistics retrieved successfully",
                stats
            ));
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(
                false,
                "Failed to retrieve course statistics: " + e.getMessage(),
                null
            ));
        }
    }
    
    /**
     * Initialize default courses (Admin only - for initial setup)
     * POST /api/courses/initialize-defaults
     */
    @PostMapping("/initialize-defaults")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<?> initializeDefaultCourses() {
        try {
            courseService.initializeDefaultCourses();
            
            return ResponseEntity.ok(new ApiResponse(
                true,
                "Default courses initialized successfully",
                null
            ));
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(
                false,
                "Failed to initialize default courses: " + e.getMessage(),
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
    
    public static class CourseResponse {
        private String id;
        private String courseCode;
        private String courseName;
        private String department;
        private String programType;
        private Integer durationYears;
        private Integer totalSeats;
        private Integer availableSeats;
        private String description;
        private List<String> eligibilityCriteria;
        private Double feeAmount;
        private String feeType;
        private Boolean isActive;
        private List<String> subjects;
        private String createdAt;
        private String updatedAt;
        
        public CourseResponse(Course course) {
            this.id = course.getId();
            this.courseCode = course.getCourseCode();
            this.courseName = course.getCourseName();
            this.department = course.getDepartment();
            this.programType = course.getProgramType();
            this.durationYears = course.getDurationYears();
            this.totalSeats = course.getTotalSeats();
            this.availableSeats = course.getAvailableSeats();
            this.description = course.getDescription();
            this.eligibilityCriteria = course.getEligibilityCriteria();
            this.feeAmount = course.getFeeAmount();
            this.feeType = course.getFeeType();
            this.isActive = course.getIsActive();
            this.subjects = course.getSubjects();
            this.createdAt = course.getCreatedAt() != null ? course.getCreatedAt().toString() : null;
            this.updatedAt = course.getUpdatedAt() != null ? course.getUpdatedAt().toString() : null;
        }
        
        // Getters
        public String getId() { return id; }
        public String getCourseCode() { return courseCode; }
        public String getCourseName() { return courseName; }
        public String getDepartment() { return department; }
        public String getProgramType() { return programType; }
        public Integer getDurationYears() { return durationYears; }
        public Integer getTotalSeats() { return totalSeats; }
        public Integer getAvailableSeats() { return availableSeats; }
        public String getDescription() { return description; }
        public List<String> getEligibilityCriteria() { return eligibilityCriteria; }
        public Double getFeeAmount() { return feeAmount; }
        public String getFeeType() { return feeType; }
        public Boolean getIsActive() { return isActive; }
        public List<String> getSubjects() { return subjects; }
        public String getCreatedAt() { return createdAt; }
        public String getUpdatedAt() { return updatedAt; }
    }
}
