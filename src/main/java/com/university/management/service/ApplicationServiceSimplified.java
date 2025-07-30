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
import com.university.management.model.Course;
import com.university.management.model.Student;
import com.university.management.repository.ApplicationRepository;
import com.university.management.repository.CourseRepository;
import com.university.management.repository.StudentRepository;

@Service
public class ApplicationServiceSimplified {

    @Autowired
    private ApplicationRepository applicationRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private final Random random = new Random();

    // Submit new application (simplified)
    public Application submitApplication(ApplicationRequestDto applicationDto) {
        // Check if email already exists
        if (applicationRepository.existsByEmail(applicationDto.getEmail())) {
            throw new RuntimeException("An application with this email already exists");
        }

        // Check if student with this email already exists
        if (studentRepository.existsByEmail(applicationDto.getEmail())) {
            throw new RuntimeException("A student with this email already exists");
        }

        // Validate course exists and is available
        Optional<Course> courseOpt = courseRepository.findByCourseName(applicationDto.getDesiredCourse());
        if (courseOpt.isEmpty()) {
            throw new RuntimeException("The selected course is not available");
        }

        Course course = courseOpt.get();
        if (!course.getIsActive()) {
            throw new RuntimeException("The selected course is currently not accepting applications");
        }

        if (!course.hasAvailableSeats()) {
            throw new RuntimeException("No seats available for the selected course");
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
        application.setUpdatedAt(LocalDateTime.now());

        // Save application
        Application savedApplication = applicationRepository.save(application);

        // Log application submission (instead of email)
        System.out.println("✅ Application submitted successfully:");
        System.out.println("   - Name: " + savedApplication.getFullName());
        System.out.println("   - Email: " + savedApplication.getEmail());
        System.out.println("   - Course: " + savedApplication.getDesiredCourse());
        System.out.println("   - Application ID: " + savedApplication.getId());

        return savedApplication;
    }

    // Get all applications (Admin only)
    public List<Application> getAllApplications() {
        return applicationRepository.findAll(Sort.by(Sort.Direction.DESC, "applicationDate"));
    }

    // Get applications by status
    public List<Application> getApplicationsByStatus(String status) {
        return applicationRepository.findByApplicationStatus(status);
    }

    // Get application by ID
    public Optional<Application> getApplicationById(String id) {
        return applicationRepository.findById(id);
    }

    // Review application (SUPER_ADMIN only) - APPROVE or REJECT (keep all records)
    public Application reviewApplication(String applicationId, ApplicationReviewDto reviewDto, Admin reviewer) {
        // Check if reviewer is SUPER_ADMIN
        if (!reviewer.isSuperAdmin()) {
            throw new RuntimeException("Only SUPER_ADMIN can review applications");
        }
        
        Optional<Application> applicationOpt = applicationRepository.findById(applicationId);
        if (applicationOpt.isEmpty()) {
            throw new RuntimeException("Application not found");
        }

        Application application = applicationOpt.get();
        
        // Only allow APPROVED or REJECTED status
        if (!"APPROVED".equals(reviewDto.getApplicationStatus()) && 
            !"REJECTED".equals(reviewDto.getApplicationStatus())) {
            throw new RuntimeException("Application can only be APPROVED or REJECTED");
        }

        // Update review details
        application.setApplicationStatus(reviewDto.getApplicationStatus());
        application.setReviewComments(reviewDto.getReviewComments());
        application.setReviewedBy(reviewer.getId());
        application.setReviewedAt(LocalDateTime.now());
        application.setUpdatedAt(LocalDateTime.now());

        // Handle rejection - Keep application record with REJECTED status
        if ("REJECTED".equals(reviewDto.getApplicationStatus())) {
            application.setRejectionReason(reviewDto.getRejectionReason());

            // Save the application with REJECTED status
            Application savedApplication = applicationRepository.save(application);

            System.out.println("❌ Application rejected and kept in database: " + application.getEmail());
            System.out.println("   - Reason: " + reviewDto.getRejectionReason());
            
            return savedApplication;
        }

        // Handle approval - Change status to APPROVED and create student
        if ("APPROVED".equals(reviewDto.getApplicationStatus())) {
            // Generate student credentials
            String studentId = generateStudentId();
            String rollNumber = generateRollNumber(application.getDesiredCourse());

            // Update application with generated info
            application.setGeneratedStudentId(studentId);
            application.setGeneratedRollNumber(rollNumber);

            // Save the approved application
            Application savedApplication = applicationRepository.save(application);

            // Create student account
            createStudentFromApplication(savedApplication);

            System.out.println("✅ Application approved and student created: " + application.getEmail());
            System.out.println("   - Student ID: " + studentId);
            System.out.println("   - Roll Number: " + rollNumber);
            
            return savedApplication;
        }

        return applicationRepository.save(application);
    }

    // Create student from approved application
    private void createStudentFromApplication(Application application) {
        Student student = new Student();
        
        // Basic information
        student.setName(application.getFullName());
        student.setEmail(application.getEmail());
        student.setPhone(application.getPhoneNumber());
        student.setRollNo(application.getGeneratedRollNumber());
        
        // Set default password (student can change later)
        String plainPassword = "student123";
        student.setPassword(passwordEncoder.encode(plainPassword));
        
        // Academic information based on course
        Course course = courseRepository.findByCourseName(application.getDesiredCourse()).orElse(null);
        if (course != null) {
            student.setDepartment(course.getDepartment());
            student.setYear(1); // First year
            student.setSemester(1); // First semester
            
            // Set fees
            if (course.getFeeAmount() != null) {
                student.setTotalFee(course.getFeeAmount());
            }
        }
        
        // Initialize academic values
        student.setCurrentCGPA(0.0);
        student.setTotalCredits(0);
        student.setPlacementStatus("Not Placed");
        
        // Save student
        studentRepository.save(student);
        
        System.out.println("✅ New student created: " + student.getName() + " (" + student.getRollNo() + ")");
        System.out.println("🔑 Default password: " + plainPassword);
        System.out.println("💰 Total fee: ₹" + student.getTotalFee());
    }

    // Generate unique student ID
    private String generateStudentId() {
        String prefix = "STU";
        String year = String.valueOf(LocalDateTime.now().getYear());
        String randomNum = String.format("%04d", random.nextInt(10000));
        return prefix + year + randomNum;
    }

    // Generate roll number based on course
    private String generateRollNumber(String course) {
        String courseCode = switch (course.toUpperCase()) {
            case "COMPUTER SCIENCE" -> "CS";
            case "INFORMATION TECHNOLOGY" -> "IT";
            case "ELECTRONICS" -> "EC";
            case "ELECTRICAL" -> "EE";
            case "MECHANICAL" -> "ME";
            default -> "GN"; // General
        };
        
        String year = String.valueOf(LocalDateTime.now().getYear()).substring(2);
        String randomNum = String.format("%03d", random.nextInt(1000));
        return courseCode + year + randomNum;
    }

    // Generate simple password
    private String generatePassword() {
        return "student123"; // Simple default password
    }

    // Get application statistics
    public Object getApplicationStats() {
        long totalApplications = applicationRepository.count();
        long appliedApplications = applicationRepository.countByApplicationStatus("APPLIED");
        long approvedApplications = applicationRepository.countByApplicationStatus("APPROVED");
        long rejectedApplications = applicationRepository.countByApplicationStatus("REJECTED");
        
        return new Object() {
            public final long total = totalApplications;
            public final long applied = appliedApplications;
            public final long approved = approvedApplications;
            public final long rejected = rejectedApplications;
        };
    }
}
