package com.university.management.service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.university.management.dto.request.StudentBasicUpdateDto;
import com.university.management.dto.response.ApiResponse;
import com.university.management.model.Student;
import com.university.management.repository.StudentRepository;

@Service
public class StudentSelfService {
    
    @Autowired
    private StudentRepository studentRepository;
    
    /**
     * Get student profile information by email
     */
    public ApiResponse<Student> getStudentProfile(String studentEmail) {
        try {
            Optional<Student> studentOpt = studentRepository.findByEmail(studentEmail);
            
            if (!studentOpt.isPresent()) {
                return ApiResponse.error("Student not found");
            }
            
            return ApiResponse.success("Student profile retrieved successfully", studentOpt.get());
        } catch (Exception e) {
            return ApiResponse.error("Error retrieving student profile: " + e.getMessage());
        }
    }
    
    /**
     * Update basic student information by email
     */
    public ApiResponse<Student> updateStudentBasicInfo(String studentEmail, StudentBasicUpdateDto updateDto) {
        try {
            Optional<Student> studentOpt = studentRepository.findByEmail(studentEmail);
            
            if (!studentOpt.isPresent()) {
                return ApiResponse.error("Student not found");
            }
            
            Student student = studentOpt.get();
            
            // Update only the fields that are provided (not null)
            if (updateDto.getName() != null && !updateDto.getName().trim().isEmpty()) {
                student.setName(updateDto.getName().trim());
            }
            
            if (updateDto.getEmail() != null && !updateDto.getEmail().trim().isEmpty()) {
                // Check if email is already taken by another student
                Optional<Student> existingStudentOpt = studentRepository.findByEmail(updateDto.getEmail());
                if (existingStudentOpt.isPresent() && !existingStudentOpt.get().getId().equals(student.getId())) {
                    return ApiResponse.error("Email is already registered with another student");
                }
                student.setEmail(updateDto.getEmail().trim());
            }
            
            if (updateDto.getPhone() != null && !updateDto.getPhone().trim().isEmpty()) {
                student.setPhone(updateDto.getPhone().trim());
            }
            
            // Note: Student model doesn't have address, guardianName, guardianPhone, emergencyContact fields
            // These would need to be added to the Student model if required
            
            Student updatedStudent = studentRepository.save(student);
            return ApiResponse.success("Student profile updated successfully", updatedStudent);
            
        } catch (Exception e) {
            return ApiResponse.error("Error updating student profile: " + e.getMessage());
        }
    }
    
    /**
     * Get student academic records by email
     */
    public ApiResponse<Map<String, Object>> getStudentAcademicRecords(String studentEmail) {
        try {
            Optional<Student> studentOpt = studentRepository.findByEmail(studentEmail);
            
            if (!studentOpt.isPresent()) {
                return ApiResponse.error("Student not found");
            }
            
            Student student = studentOpt.get();
            
            Map<String, Object> academicInfo = new HashMap<>();
            academicInfo.put("rollNo", student.getRollNo());
            academicInfo.put("studentId", student.getId()); // Using getId() instead of getStudentId()
            academicInfo.put("department", student.getDepartment()); // Using department instead of course/branch
            academicInfo.put("year", student.getYear());
            academicInfo.put("semester", student.getSemester());
            academicInfo.put("cgpa", student.getCurrentCGPA()); // Using getCurrentCGPA() instead of getCgpa()
            academicInfo.put("totalCredits", student.getTotalCredits());
            academicInfo.put("createdAt", student.getCreatedAt()); // Using createdAt instead of admissionDate
            
            return ApiResponse.success("Academic records retrieved successfully", academicInfo);
        } catch (Exception e) {
            return ApiResponse.error("Error retrieving academic records: " + e.getMessage());
        }
    }
    
    /**
     * Get student fee status by email
     */
    public ApiResponse<Map<String, Object>> getStudentFeeStatus(String studentEmail) {
        try {
            Optional<Student> studentOpt = studentRepository.findByEmail(studentEmail);
            
            if (!studentOpt.isPresent()) {
                return ApiResponse.error("Student not found");
            }
            
            Student student = studentOpt.get();
            
            Map<String, Object> feeInfo = new HashMap<>();
            feeInfo.put("studentId", student.getId());
            feeInfo.put("rollNo", student.getRollNo());
            feeInfo.put("name", student.getName());
            feeInfo.put("department", student.getDepartment());
            feeInfo.put("year", student.getYear());
            feeInfo.put("semester", student.getSemester());
            
            // Using actual fee fields from Student model
            feeInfo.put("totalFee", student.getTotalFee());
            feeInfo.put("paidAmount", student.getPaidAmount());
            feeInfo.put("pendingAmount", student.getPendingAmount()); // Helper method in Student model
            feeInfo.put("feeStatus", student.getFeeStatus()); // Helper method in Student model
            feeInfo.put("lastUpdate", student.getUpdatedAt());
            
            return ApiResponse.success("Fee status retrieved successfully", feeInfo);
        } catch (Exception e) {
            return ApiResponse.error("Error retrieving fee status: " + e.getMessage());
        }
    }
    
    /**
     * Get student placement information by email
     */
    public ApiResponse<Map<String, Object>> getStudentPlacementInfo(String studentEmail) {
        try {
            Optional<Student> studentOpt = studentRepository.findByEmail(studentEmail);
            
            if (!studentOpt.isPresent()) {
                return ApiResponse.error("Student not found");
            }
            
            Student student = studentOpt.get();
            
            Map<String, Object> placementInfo = new HashMap<>();
            placementInfo.put("studentId", student.getId());
            placementInfo.put("rollNo", student.getRollNo());
            placementInfo.put("name", student.getName());
            placementInfo.put("department", student.getDepartment());
            placementInfo.put("cgpa", student.getCurrentCGPA());
            
            // Using actual placement fields from Student model
            placementInfo.put("placementStatus", student.getPlacementStatus());
            placementInfo.put("company", student.getCompany());
            placementInfo.put("packageAmount", student.getPackageAmount());
            
            // Mock additional data - in real application, this would come from placement management system
            placementInfo.put("companiesApplied", 5);
            placementInfo.put("interviewsScheduled", 2);
            placementInfo.put("placementCoordinator", "Dr. Sharma");
            placementInfo.put("upcomingDrives", "TCS - 25th Jan, Infosys - 2nd Feb");
            
            return ApiResponse.success("Placement information retrieved successfully", placementInfo);
        } catch (Exception e) {
            return ApiResponse.error("Error retrieving placement information: " + e.getMessage());
        }
    }
    
    /**
     * Get student courses by email
     */
    public ApiResponse<Map<String, Object>> getStudentCourses(String studentEmail) {
        try {
            Optional<Student> studentOpt = studentRepository.findByEmail(studentEmail);
            
            if (!studentOpt.isPresent()) {
                return ApiResponse.error("Student not found");
            }
            
            Student student = studentOpt.get();
            
            Map<String, Object> courseInfo = new HashMap<>();
            courseInfo.put("studentId", student.getId());
            courseInfo.put("rollNo", student.getRollNo());
            courseInfo.put("department", student.getDepartment());
            courseInfo.put("year", student.getYear());
            courseInfo.put("semester", student.getSemester());
            
            // Mock course data - in real application, this would come from course management system
            Map<String, Object> currentSemesterCourses = new HashMap<>();
            currentSemesterCourses.put("CSE101", "Data Structures and Algorithms");
            currentSemesterCourses.put("CSE102", "Database Management Systems");
            currentSemesterCourses.put("CSE103", "Computer Networks");
            currentSemesterCourses.put("MAT101", "Discrete Mathematics");
            currentSemesterCourses.put("ENG101", "Technical Communication");
            
            courseInfo.put("currentSemesterCourses", currentSemesterCourses);
            courseInfo.put("totalCredits", student.getTotalCredits());
            courseInfo.put("completedCredits", student.getTotalCredits()); // Mock - should be calculated
            
            return ApiResponse.success("Course information retrieved successfully", courseInfo);
        } catch (Exception e) {
            return ApiResponse.error("Error retrieving course information: " + e.getMessage());
        }
    }
    
    /**
     * Get student dashboard summary by email
     */
    public ApiResponse<Map<String, Object>> getStudentDashboard(String studentEmail) {
        try {
            Optional<Student> studentOpt = studentRepository.findByEmail(studentEmail);
            
            if (!studentOpt.isPresent()) {
                return ApiResponse.error("Student not found");
            }
            
            Student student = studentOpt.get();
            
            Map<String, Object> dashboard = new HashMap<>();
            
            // Basic Info
            Map<String, Object> basicInfo = new HashMap<>();
            basicInfo.put("name", student.getName());
            basicInfo.put("rollNo", student.getRollNo());
            basicInfo.put("studentId", student.getId());
            basicInfo.put("department", student.getDepartment());
            basicInfo.put("year", student.getYear());
            basicInfo.put("semester", student.getSemester());
            basicInfo.put("cgpa", student.getCurrentCGPA());
            
            // Quick Stats
            Map<String, Object> quickStats = new HashMap<>();
            Double attendancePercentage = 0.0;
            if (student.getAttendance() != null) {
                Double percentage = student.getAttendance().getPercentage();
                if (percentage != null) {
                    attendancePercentage = percentage;
                }
            }
            quickStats.put("attendancePercentage", attendancePercentage);
            quickStats.put("currentSemesterCourses", 5); // Mock data
            quickStats.put("pendingAssignments", 2); // Mock data
            quickStats.put("upcomingExams", 3); // Mock data
            
            // Recent Activities
            Map<String, Object> recentActivities = new HashMap<>();
            recentActivities.put("lastLogin", "2024-01-20 10:30:00"); // Mock data
            recentActivities.put("recentGrades", "Database Systems: A, Networks: B+"); // Mock data
            recentActivities.put("recentNotifications", "Fee payment due on 15th Feb"); // Mock data
            
            dashboard.put("basicInfo", basicInfo);
            dashboard.put("quickStats", quickStats);
            dashboard.put("recentActivities", recentActivities);
            
            return ApiResponse.success("Dashboard data retrieved successfully", dashboard);
        } catch (Exception e) {
            return ApiResponse.error("Error retrieving dashboard data: " + e.getMessage());
        }
    }
}
