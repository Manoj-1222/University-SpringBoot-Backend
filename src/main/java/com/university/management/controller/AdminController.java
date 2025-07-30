package com.university.management.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.university.management.dto.request.StudentAcademicUpdateDto;
import com.university.management.dto.request.StudentFullUpdateDto;
import com.university.management.dto.response.ApiResponse;
import com.university.management.dto.response.StudentResponse;
import com.university.management.model.Admin;
import com.university.management.service.StudentService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/admin")
@CrossOrigin(origins = "*")
public class AdminController {
    
    @Autowired
    private StudentService studentService;
    
    /**
     * Get all students - Both SUPER_ADMIN and STAFF_ADMIN can access
     * GET /api/admin/students
     */
    @GetMapping("/students")
    @PreAuthorize("hasRole('SUPER_ADMIN') or hasRole('STAFF_ADMIN')")
    public ResponseEntity<ApiResponse<List<StudentResponse>>> getAllStudents(@AuthenticationPrincipal Admin admin) {
        try {
            List<StudentResponse> students = studentService.getAllStudents();
            
            return ResponseEntity.ok(ApiResponse.success(
                "Students retrieved successfully", 
                students
            ));
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error("Failed to retrieve students", e.getMessage()));
        }
    }
    
    /**
     * Get student by ID - Both SUPER_ADMIN and STAFF_ADMIN can access
     * GET /api/admin/students/{studentId}
     */
    @GetMapping("/students/{studentId}")
    @PreAuthorize("hasRole('SUPER_ADMIN') or hasRole('STAFF_ADMIN')")
    public ResponseEntity<ApiResponse<StudentResponse>> getStudentById(
            @PathVariable String studentId,
            @AuthenticationPrincipal Admin admin) {
        try {
            StudentResponse student = studentService.getStudentById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found with id: " + studentId));
            
            return ResponseEntity.ok(ApiResponse.success(
                "Student retrieved successfully", 
                student
            ));
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ApiResponse.error("Student not found", e.getMessage()));
        }
    }
    
    /**
     * Update student academic records - Only STAFF_ADMIN can access
     * PUT /api/admin/students/{studentId}/academic
     */
    @PutMapping("/students/{studentId}/academic")
    @PreAuthorize("hasRole('STAFF_ADMIN') or hasRole('SUPER_ADMIN')")
    public ResponseEntity<ApiResponse<StudentResponse>> updateStudentAcademics(
            @PathVariable String studentId,
            @Valid @RequestBody StudentAcademicUpdateDto request,
            @AuthenticationPrincipal Admin admin) {
        try {
            StudentResponse updatedStudent = studentService.updateStudentAcademicData(studentId, request);
            
            return ResponseEntity.ok(ApiResponse.success(
                "Student academic records updated successfully", 
                updatedStudent
            ));
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error("Failed to update student academic records", e.getMessage()));
        }
    }
    
    /**
     * Update student profile (all fields) - Only SUPER_ADMIN can access
     * PUT /api/admin/students/{studentId}
     */
    @PutMapping("/students/{studentId}")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<ApiResponse<StudentResponse>> updateStudent(
            @PathVariable String studentId,
            @Valid @RequestBody StudentFullUpdateDto request,
            @AuthenticationPrincipal Admin admin) {
        try {
            StudentResponse updatedStudent = studentService.updateStudentFullData(studentId, request);
            
            return ResponseEntity.ok(ApiResponse.success(
                "Student updated successfully", 
                updatedStudent
            ));
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error("Failed to update student", e.getMessage()));
        }
    }
}
