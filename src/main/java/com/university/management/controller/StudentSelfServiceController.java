package com.university.management.controller;

import com.university.management.dto.request.StudentBasicUpdateDto;
import com.university.management.dto.response.ApiResponse;
import com.university.management.model.Student;
import com.university.management.service.StudentSelfService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.Map;

@RestController
@RequestMapping("/students")
@CrossOrigin(origins = "*")
public class StudentSelfServiceController {
    
    @Autowired
    private StudentSelfService studentSelfService;
    
    /**
     * Get current student's profile
     * GET /api/students/me
     */
    @GetMapping("/me")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<ApiResponse<Student>> getMyProfile(Authentication authentication) {
        try {
            String studentEmail = authentication.getName();
            ApiResponse<Student> response = studentSelfService.getStudentProfile(studentEmail);
            
            return response.isSuccess() 
                ? ResponseEntity.ok(response)
                : ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
                
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Failed to retrieve profile", e.getMessage()));
        }
    }
    
    /**
     * Update current student's basic information
     * PUT /api/students/me
     */
    @PutMapping("/me")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<ApiResponse<Student>> updateMyProfile(
            @Valid @RequestBody StudentBasicUpdateDto updateDto,
            Authentication authentication) {
        try {
            String studentEmail = authentication.getName();
            ApiResponse<Student> response = studentSelfService.updateStudentBasicInfo(studentEmail, updateDto);
            
            return response.isSuccess()
                ? ResponseEntity.ok(response)
                : ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
                
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error("Failed to update profile", e.getMessage()));
        }
    }
    
    /**
     * Get current student's academic records
     * GET /api/students/me/academic
     */
    @GetMapping("/me/academic")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getMyAcademicRecords(Authentication authentication) {
        try {
            String studentEmail = authentication.getName();
            ApiResponse<Map<String, Object>> response = studentSelfService.getStudentAcademicRecords(studentEmail);
            
            return response.isSuccess()
                ? ResponseEntity.ok(response)
                : ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
                
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Failed to retrieve academic records", e.getMessage()));
        }
    }
    
    /**
     * Get current student's fee status
     * GET /api/students/me/fees
     */
    @GetMapping("/me/fees")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getMyFeeStatus(Authentication authentication) {
        try {
            String studentEmail = authentication.getName();
            ApiResponse<Map<String, Object>> response = studentSelfService.getStudentFeeStatus(studentEmail);
            
            return response.isSuccess()
                ? ResponseEntity.ok(response)
                : ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
                
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Failed to retrieve fee status", e.getMessage()));
        }
    }
    
    /**
     * Get current student's placement information
     * GET /api/students/me/placement
     */
    @GetMapping("/me/placement")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getMyPlacementInfo(Authentication authentication) {
        try {
            String studentEmail = authentication.getName();
            ApiResponse<Map<String, Object>> response = studentSelfService.getStudentPlacementInfo(studentEmail);
            
            return response.isSuccess()
                ? ResponseEntity.ok(response)
                : ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
                
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Failed to retrieve placement information", e.getMessage()));
        }
    }
    
    /**
     * Get current student's courses
     * GET /api/students/me/courses
     */
    @GetMapping("/me/courses")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getMyCourses(Authentication authentication) {
        try {
            String studentEmail = authentication.getName();
            ApiResponse<Map<String, Object>> response = studentSelfService.getStudentCourses(studentEmail);
            
            return response.isSuccess()
                ? ResponseEntity.ok(response)
                : ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
                
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Failed to retrieve course information", e.getMessage()));
        }
    }
    
    /**
     * Get current student's dashboard summary
     * GET /api/students/me/dashboard
     */
    @GetMapping("/me/dashboard")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getMyDashboard(Authentication authentication) {
        try {
            String studentEmail = authentication.getName();
            ApiResponse<Map<String, Object>> response = studentSelfService.getStudentDashboard(studentEmail);
            
            return response.isSuccess()
                ? ResponseEntity.ok(response)
                : ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
                
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Failed to retrieve dashboard data", e.getMessage()));
        }
    }
}
