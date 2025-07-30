package com.university.management.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.university.management.dto.request.StudentAcademicUpdateDto;
import com.university.management.dto.request.StudentFullUpdateDto;
import com.university.management.dto.request.StudentRegistrationRequest;
import com.university.management.dto.response.ApiResponse;
import com.university.management.dto.response.StudentResponse;
import com.university.management.service.StudentService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/students")
@CrossOrigin(origins = "*", maxAge = 3600)
public class StudentController {
    
    @Autowired
    private StudentService studentService;
    
    @GetMapping
    public ResponseEntity<ApiResponse<List<StudentResponse>>> getAllStudents(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "name") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        
        try {
            if (page == -1) {
                // Return all students without pagination
                List<StudentResponse> students = studentService.getAllStudents();
                return ResponseEntity.ok(ApiResponse.success("Students retrieved successfully", students));
            } else {
                // Return paginated results
                Sort.Direction direction = sortDir.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
                Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
                Page<StudentResponse> studentsPage = studentService.getStudentsPaginated(pageable);
                return ResponseEntity.ok(ApiResponse.success("Students retrieved successfully", studentsPage.getContent()));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Failed to retrieve students", e.getMessage()));
        }
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<StudentResponse>> getStudentById(@PathVariable String id) {
        try {
            Optional<StudentResponse> student = studentService.getStudentById(id);
            if (student.isPresent()) {
                return ResponseEntity.ok(ApiResponse.success("Student found", student.get()));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiResponse.error("Student not found with id: " + id));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Failed to retrieve student", e.getMessage()));
        }
    }
    
    @GetMapping("/email/{email}")
    public ResponseEntity<ApiResponse<StudentResponse>> getStudentByEmail(@PathVariable String email) {
        try {
            Optional<StudentResponse> student = studentService.getStudentByEmail(email);
            if (student.isPresent()) {
                return ResponseEntity.ok(ApiResponse.success("Student found", student.get()));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiResponse.error("Student not found with email: " + email));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Failed to retrieve student", e.getMessage()));
        }
    }
    
    @GetMapping("/rollno/{rollNo}")
    public ResponseEntity<ApiResponse<StudentResponse>> getStudentByRollNo(@PathVariable String rollNo) {
        try {
            Optional<StudentResponse> student = studentService.getStudentByRollNo(rollNo);
            if (student.isPresent()) {
                return ResponseEntity.ok(ApiResponse.success("Student found", student.get()));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiResponse.error("Student not found with roll number: " + rollNo));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Failed to retrieve student", e.getMessage()));
        }
    }
    
    @GetMapping("/department/{department}")
    public ResponseEntity<ApiResponse<List<StudentResponse>>> getStudentsByDepartment(@PathVariable String department) {
        try {
            List<StudentResponse> students = studentService.getStudentsByDepartment(department);
            return ResponseEntity.ok(ApiResponse.success("Students retrieved successfully", students));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Failed to retrieve students", e.getMessage()));
        }
    }
    
    @GetMapping("/year/{year}")
    public ResponseEntity<ApiResponse<List<StudentResponse>>> getStudentsByYear(@PathVariable Integer year) {
        try {
            List<StudentResponse> students = studentService.getStudentsByYear(year);
            return ResponseEntity.ok(ApiResponse.success("Students retrieved successfully", students));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Failed to retrieve students", e.getMessage()));
        }
    }
    
    @GetMapping("/department/{department}/year/{year}")
    public ResponseEntity<ApiResponse<List<StudentResponse>>> getStudentsByDepartmentAndYear(
            @PathVariable String department, @PathVariable Integer year) {
        try {
            List<StudentResponse> students = studentService.getStudentsByDepartmentAndYear(department, year);
            return ResponseEntity.ok(ApiResponse.success("Students retrieved successfully", students));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Failed to retrieve students", e.getMessage()));
        }
    }
    
    @GetMapping("/placed")
    public ResponseEntity<ApiResponse<List<StudentResponse>>> getPlacedStudents() {
        try {
            List<StudentResponse> students = studentService.getPlacedStudents();
            return ResponseEntity.ok(ApiResponse.success("Placed students retrieved successfully", students));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Failed to retrieve placed students", e.getMessage()));
        }
    }
    
    @PostMapping
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<ApiResponse<StudentResponse>> createStudent(@Valid @RequestBody StudentRegistrationRequest request) {
        try {
            StudentResponse student = studentService.createStudent(request);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponse.success("Student created successfully", student));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error("Failed to create student", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Failed to create student", e.getMessage()));
        }
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<StudentResponse>> updateStudent(
            @PathVariable String id, @Valid @RequestBody StudentRegistrationRequest request) {
        try {
            StudentResponse student = studentService.updateStudent(id, request);
            return ResponseEntity.ok(ApiResponse.success("Student updated successfully", student));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error("Failed to update student", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Failed to update student", e.getMessage()));
        }
    }
    
    @PutMapping("/{id}/academic")
    @PreAuthorize("hasRole('STAFF_ADMIN') or hasRole('SUPER_ADMIN')")
    public ResponseEntity<ApiResponse<StudentResponse>> updateStudentAcademicInfo(
            @PathVariable String id, @Valid @RequestBody StudentAcademicUpdateDto request) {
        try {
            StudentResponse student = studentService.updateStudentAcademicData(id, request);
            return ResponseEntity.ok(ApiResponse.success("Academic information updated successfully", student));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error("Failed to update academic information", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Failed to update academic information", e.getMessage()));
        }
    }

    @PutMapping("/{id}/full")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<ApiResponse<StudentResponse>> updateStudentFullData(
            @PathVariable String id, @Valid @RequestBody StudentFullUpdateDto request) {
        try {
            StudentResponse student = studentService.updateStudentFullData(id, request);
            return ResponseEntity.ok(ApiResponse.success("Student profile updated successfully", student));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error("Failed to update student profile", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Failed to update student profile", e.getMessage()));
        }
    }
    
    @PutMapping("/{id}/academic-legacy")
    public ResponseEntity<ApiResponse<StudentResponse>> updateStudentAcademicInfoLegacy(
            @PathVariable String id, @RequestBody AcademicUpdateRequest request) {
        try {
            StudentResponse student = studentService.updateStudentAcademicInfo(
                    id, request.getDepartment(), request.getYear(), request.getSemester());
            return ResponseEntity.ok(ApiResponse.success("Academic information updated successfully", student));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error("Failed to update academic information", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Failed to update academic information", e.getMessage()));
        }
    }
    
    @PutMapping("/{id}/cgpa")
    @PreAuthorize("hasRole('STAFF_ADMIN') or hasRole('SUPER_ADMIN')")
    public ResponseEntity<ApiResponse<StudentResponse>> updateStudentCGPA(
            @PathVariable String id, @RequestBody CGPAUpdateRequest request) {
        try {
            StudentResponse student = studentService.updateStudentCGPA(id, request.getCgpa(), request.getCredits());
            return ResponseEntity.ok(ApiResponse.success("CGPA updated successfully", student));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error("Failed to update CGPA", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Failed to update CGPA", e.getMessage()));
        }
    }
    
    @PutMapping("/{id}/attendance")
    @PreAuthorize("hasRole('STAFF_ADMIN') or hasRole('SUPER_ADMIN')")
    public ResponseEntity<ApiResponse<StudentResponse>> updateStudentAttendance(
            @PathVariable String id, @RequestBody AttendanceUpdateRequest request) {
        try {
            StudentResponse student = studentService.updateStudentAttendance(id, request.getAttendancePercentage());
            return ResponseEntity.ok(ApiResponse.success("Attendance updated successfully", student));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error("Failed to update attendance", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Failed to update attendance", e.getMessage()));
        }
    }
    
    @PutMapping("/{id}/fee")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<ApiResponse<StudentResponse>> updateStudentFee(
            @PathVariable String id, @RequestBody FeeUpdateRequest request) {
        try {
            StudentResponse student = studentService.updateStudentFee(id, request.getTotalFee(), request.getPaidAmount());
            return ResponseEntity.ok(ApiResponse.success("Fee information updated successfully", student));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error("Failed to update fee information", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Failed to update fee information", e.getMessage()));
        }
    }
    
    @PutMapping("/{id}/placement")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<ApiResponse<StudentResponse>> updateStudentPlacement(
            @PathVariable String id, @RequestBody PlacementUpdateRequest request) {
        try {
            StudentResponse student = studentService.updateStudentPlacement(
                    id, request.getCompany(), "", request.getPackageAmount());
            return ResponseEntity.ok(ApiResponse.success("Placement information updated successfully", student));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error("Failed to update placement information", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Failed to update placement information", e.getMessage()));
        }
    }
    
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<ApiResponse<String>> deleteStudent(@PathVariable String id) {
        try {
            studentService.deleteStudent(id);
            return ResponseEntity.ok(ApiResponse.success("Student deleted successfully"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error("Failed to delete student", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Failed to delete student", e.getMessage()));
        }
    }
    
    @GetMapping("/count")
    public ResponseEntity<ApiResponse<Long>> getTotalStudentCount() {
        try {
            long count = studentService.getTotalStudentCount();
            return ResponseEntity.ok(ApiResponse.success("Total student count retrieved", count));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Failed to get student count", e.getMessage()));
        }
    }
    
    @GetMapping("/count/department/{department}")
    public ResponseEntity<ApiResponse<Long>> getStudentCountByDepartment(@PathVariable String department) {
        try {
            long count = studentService.getStudentCountByDepartment(department);
            return ResponseEntity.ok(ApiResponse.success("Student count by department retrieved", count));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Failed to get student count", e.getMessage()));
        }
    }
    
    @GetMapping("/count/placed")
    public ResponseEntity<ApiResponse<Long>> getPlacedStudentCount() {
        try {
            long count = studentService.getPlacedStudentCount();
            return ResponseEntity.ok(ApiResponse.success("Placed student count retrieved", count));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Failed to get placed student count", e.getMessage()));
        }
    }
    
    // Inner classes for request DTOs
    public static class AcademicUpdateRequest {
        private String department;
        private Integer year;
        private Integer semester;
        
        // Constructors, getters, and setters
        public AcademicUpdateRequest() {}
        
        public String getDepartment() { return department; }
        public void setDepartment(String department) { this.department = department; }
        
        public Integer getYear() { return year; }
        public void setYear(Integer year) { this.year = year; }
        
        public Integer getSemester() { return semester; }
        public void setSemester(Integer semester) { this.semester = semester; }
    }
    
    public static class CGPAUpdateRequest {
        private Double cgpa;
        private Integer credits;
        
        // Constructors, getters, and setters
        public CGPAUpdateRequest() {}
        
        public Double getCgpa() { return cgpa; }
        public void setCgpa(Double cgpa) { this.cgpa = cgpa; }
        
        public Integer getCredits() { return credits; }
        public void setCredits(Integer credits) { this.credits = credits; }
    }
    
    public static class AttendanceUpdateRequest {
        private Double attendancePercentage;
        
        // Constructors, getters, and setters
        public AttendanceUpdateRequest() {}
        
        public Double getAttendancePercentage() { return attendancePercentage; }
        public void setAttendancePercentage(Double attendancePercentage) { this.attendancePercentage = attendancePercentage; }
    }
    
    public static class FeeUpdateRequest {
        private Double totalFee;
        private Double paidAmount;
        
        // Constructors, getters, and setters
        public FeeUpdateRequest() {}
        
        public Double getTotalFee() { return totalFee; }
        public void setTotalFee(Double totalFee) { this.totalFee = totalFee; }
        
        public Double getPaidAmount() { return paidAmount; }
        public void setPaidAmount(Double paidAmount) { this.paidAmount = paidAmount; }
    }
    
    public static class PlacementUpdateRequest {
        private String placementStatus;
        private String company;
        private Double packageAmount;
        
        // Constructors, getters, and setters
        public PlacementUpdateRequest() {}
        
        public String getPlacementStatus() { return placementStatus; }
        public void setPlacementStatus(String placementStatus) { this.placementStatus = placementStatus; }
        
        public String getCompany() { return company; }
        public void setCompany(String company) { this.company = company; }
        
        public Double getPackageAmount() { return packageAmount; }
        public void setPackageAmount(Double packageAmount) { this.packageAmount = packageAmount; }
    }
}
