package com.university.management.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.university.management.dto.ApplicationRequestDto;
import com.university.management.dto.ApplicationReviewDto;
import com.university.management.model.Admin;
import com.university.management.model.Application;
import com.university.management.model.Student;
import com.university.management.repository.ApplicationRepository;
import com.university.management.repository.StudentRepository;

@Service
public class ApplicationService {
    
    @Autowired
    private ApplicationRepository applicationRepository;
    
    @Autowired
    private StudentRepository studentRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    /**
     * Submit a new application (simplified version)
     */
    public Application submitApplication(ApplicationRequestDto applicationDto) {
        try {
            // Check if application already exists with same email
            if (applicationRepository.findByEmail(applicationDto.getEmail()).isPresent()) {
                throw new RuntimeException("An application with this email already exists");
            }
            
            // Create new application with simplified fields
            Application application = new Application();
            application.setFullName(applicationDto.getFullName());
            application.setEmail(applicationDto.getEmail());
            application.setPhoneNumber(applicationDto.getPhoneNumber());
            application.setDesiredCourse(applicationDto.getDesiredCourse());
            application.setPreviousQualification(applicationDto.getPreviousQualification());
            application.setPreviousGrade(applicationDto.getPreviousGrade());
            application.setApplicationStatus("APPLIED");
            application.setApplicationDate(LocalDateTime.now());
            
            // Save application
            Application savedApplication = applicationRepository.save(application);
            
            // Log to console instead of sending email
            System.out.println("📧 [CONSOLE LOG] New Application Received:");
            System.out.println("   Name: " + savedApplication.getFullName());
            System.out.println("   Email: " + savedApplication.getEmail());
            System.out.println("   Course: " + savedApplication.getDesiredCourse());
            System.out.println("   Status: " + savedApplication.getApplicationStatus());
            System.out.println("   Date: " + savedApplication.getApplicationDate());
            
            return savedApplication;
            
        } catch (Exception e) {
            throw new RuntimeException("Failed to submit application: " + e.getMessage());
        }
    }
    
    /**
     * Get all applications (SUPER_ADMIN only)
     */
    public List<Application> getAllApplications() {
        return applicationRepository.findAll(Sort.by(Sort.Direction.DESC, "applicationDate"));
    }
    
    /**
     * Get applications by status
     */
    public List<Application> getApplicationsByStatus(String status) {
        return applicationRepository.findByApplicationStatus(status, Sort.by(Sort.Direction.DESC, "applicationDate"));
    }
    
    /**
     * Get application by ID
     */
    public Optional<Application> getApplicationById(String id) {
        return applicationRepository.findById(id);
    }
    
    /**
     * Get applications by desired course
     */
    public List<Application> getApplicationsByCourse(String course) {
        return applicationRepository.findByDesiredCourse(course, Sort.by(Sort.Direction.DESC, "applicationDate"));
    }
    
    /**
     * Review application - approve or reject (simplified version)
     */
    public Application reviewApplication(String applicationId, ApplicationReviewDto reviewDto, Admin reviewedBy) {
        try {
            Optional<Application> optionalApp = applicationRepository.findById(applicationId);
            if (optionalApp.isEmpty()) {
                throw new RuntimeException("Application not found");
            }
            
            Application application = optionalApp.get();
            
            // Update application status and review details
            application.setApplicationStatus(reviewDto.getApplicationStatus());
            application.setReviewComments(reviewDto.getReviewComments());
            application.setReviewedBy(reviewedBy.getUsername());
            application.setReviewedAt(LocalDateTime.now());
            application.setUpdatedAt(LocalDateTime.now());
            
            if ("REJECTED".equals(reviewDto.getApplicationStatus())) {
                application.setRejectionReason(reviewDto.getRejectionReason());
            }
            
            // Save updated application
            Application updatedApplication = applicationRepository.save(application);
            
            // Handle approval - create student account (simplified)
            if ("APPROVED".equals(reviewDto.getApplicationStatus())) {
                createStudentFromApplication(updatedApplication);
                
                // Log approval to console
                System.out.println("✅ [CONSOLE LOG] Application Approved:");
                System.out.println("   Name: " + updatedApplication.getFullName());
                System.out.println("   Course: " + updatedApplication.getDesiredCourse());
                System.out.println("   Student account created successfully");
                
            } else if ("REJECTED".equals(reviewDto.getApplicationStatus())) {
                // Log rejection to console
                System.out.println("❌ [CONSOLE LOG] Application Rejected:");
                System.out.println("   Name: " + updatedApplication.getFullName());
                System.out.println("   Reason: " + updatedApplication.getRejectionReason());
            }
            
            return updatedApplication;
            
        } catch (Exception e) {
            throw new RuntimeException("Failed to review application: " + e.getMessage());
        }
    }
    
    /**
     * Create student account from approved application (simplified)
     */
    private void createStudentFromApplication(Application application) {
        try {
            // Check if student already exists
            if (studentRepository.findByEmail(application.getEmail()).isPresent()) {
                System.out.println("⚠️  Student account already exists for: " + application.getEmail());
                return;
            }
            
            // Generate student ID and temporary password
            String rollNo = generateRollNumber();
            String tempPassword = generateTemporaryPassword();
            
            // Create new student with fields that match the Student model
            Student student = new Student();
            student.setName(application.getFullName());
            student.setRollNo(rollNo);
            student.setEmail(application.getEmail());
            student.setPhone(application.getPhoneNumber());
            student.setPassword(passwordEncoder.encode(tempPassword));
            
            // Map desired course to department (simplified mapping)
            String department = mapCourseToDepartment(application.getDesiredCourse());
            student.setDepartment(department);
            
            // Set default academic values
            student.setYear(1); // Default to first year
            student.setSemester(1); // Default to first semester
            
            // Save student
            studentRepository.save(student);
            
            // Log student creation to console
            System.out.println("👤 [CONSOLE LOG] Student Account Created:");
            System.out.println("   Roll Number: " + rollNo);
            System.out.println("   Name: " + student.getName());
            System.out.println("   Email: " + student.getEmail());
            System.out.println("   Department: " + student.getDepartment());
            System.out.println("   Temporary Password: " + tempPassword);
            
        } catch (Exception e) {
            throw new RuntimeException("Failed to create student account: " + e.getMessage());
        }
    }
    
    /**
     * Map course name to department (simplified mapping)
     */
    private String mapCourseToDepartment(String desiredCourse) {
        if (desiredCourse == null) return "General";
        
        String course = desiredCourse.toLowerCase();
        if (course.contains("computer") || course.contains("software") || course.contains("it")) {
            return "Computer Science";
        } else if (course.contains("electronic") || course.contains("electrical")) {
            return "Electronics";
        } else if (course.contains("mechanical")) {
            return "Mechanical";
        } else if (course.contains("business") || course.contains("mba") || course.contains("management")) {
            return "Management";
        } else if (course.contains("civil")) {
            return "Civil";
        } else {
            return "General";
        }
    }
    
    /**
     * Generate unique roll number
     */
    private String generateRollNumber() {
        String year = String.valueOf(LocalDateTime.now().getYear());
        String randomNum = String.format("%04d", new Random().nextInt(10000));
        return year + randomNum;
    }
    
    /**
     * Generate temporary password
     */
    private String generateTemporaryPassword() {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder password = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 8; i++) {
            password.append(chars.charAt(random.nextInt(chars.length())));
        }
        return password.toString();
    }
    
    /**
     * Get application statistics
     */
    public ApplicationStats getApplicationStats() {
        long totalApplications = applicationRepository.count();
        long appliedCount = applicationRepository.countByApplicationStatus("APPLIED");
        long approvedCount = applicationRepository.countByApplicationStatus("APPROVED");
        long rejectedCount = applicationRepository.countByApplicationStatus("REJECTED");
        long pendingCount = applicationRepository.countByApplicationStatus("PENDING");
        
        return new ApplicationStats(totalApplications, appliedCount, approvedCount, rejectedCount, pendingCount);
    }
    
    /**
     * Application statistics DTO
     */
    public static class ApplicationStats {
        private long totalApplications;
        private long appliedCount;
        private long approvedCount;
        private long rejectedCount;
        private long pendingCount;
        
        public ApplicationStats(long totalApplications, long appliedCount, long approvedCount, long rejectedCount, long pendingCount) {
            this.totalApplications = totalApplications;
            this.appliedCount = appliedCount;
            this.approvedCount = approvedCount;
            this.rejectedCount = rejectedCount;
            this.pendingCount = pendingCount;
        }
        
        // Getters
        public long getTotalApplications() { return totalApplications; }
        public long getAppliedCount() { return appliedCount; }
        public long getApprovedCount() { return approvedCount; }
        public long getRejectedCount() { return rejectedCount; }
        public long getPendingCount() { return pendingCount; }
    }
}
