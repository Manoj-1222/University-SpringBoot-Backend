package com.university.management.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.university.management.model.Course;
import com.university.management.repository.CourseRepository;

@Service
public class CourseService {
    
    @Autowired
    private CourseRepository courseRepository;
    
    // Get all active courses
    public List<Course> getAllActiveCourses() {
        return courseRepository.findByIsActiveTrue();
    }
    
    // Get all courses (Admin only)
    public List<Course> getAllCourses() {
        return courseRepository.findAll(Sort.by(Sort.Direction.ASC, "courseName"));
    }
    
    // Get available courses (with seats available)
    public List<Course> getAvailableCourses() {
        return courseRepository.findAvailableCourses();
    }
    
    // Get courses by department
    public List<Course> getCoursesByDepartment(String department) {
        return courseRepository.findByDepartment(department);
    }
    
    // Get courses by program type
    public List<Course> getCoursesByProgramType(String programType) {
        return courseRepository.findByProgramType(programType);
    }
    
    // Get course by ID
    public Optional<Course> getCourseById(String id) {
        return courseRepository.findById(id);
    }
    
    // Get course by name
    public Optional<Course> getCourseByName(String courseName) {
        return courseRepository.findByCourseName(courseName);
    }
    
    // Create new course (Admin only)
    public Course createCourse(Course course) {
        // Check if course code already exists
        if (courseRepository.existsByCourseCode(course.getCourseCode())) {
            throw new RuntimeException("Course with this code already exists");
        }
        
        // Check if course name already exists
        if (courseRepository.existsByCourseName(course.getCourseName())) {
            throw new RuntimeException("Course with this name already exists");
        }
        
        // Set initial available seats to total seats
        if (course.getAvailableSeats() == null) {
            course.setAvailableSeats(course.getTotalSeats());
        }
        
        course.setCreatedAt(LocalDateTime.now());
        course.setUpdatedAt(LocalDateTime.now());
        
        return courseRepository.save(course);
    }
    
    // Update course (Admin only)
    public Course updateCourse(String id, Course courseDetails) {
        Optional<Course> courseOpt = courseRepository.findById(id);
        if (courseOpt.isEmpty()) {
            throw new RuntimeException("Course not found");
        }
        
        Course course = courseOpt.get();
        
        // Update fields
        if (courseDetails.getCourseName() != null) {
            course.setCourseName(courseDetails.getCourseName());
        }
        if (courseDetails.getDepartment() != null) {
            course.setDepartment(courseDetails.getDepartment());
        }
        if (courseDetails.getProgramType() != null) {
            course.setProgramType(courseDetails.getProgramType());
        }
        if (courseDetails.getDurationYears() != null) {
            course.setDurationYears(courseDetails.getDurationYears());
        }
        if (courseDetails.getTotalSeats() != null) {
            course.setTotalSeats(courseDetails.getTotalSeats());
        }
        if (courseDetails.getAvailableSeats() != null) {
            course.setAvailableSeats(courseDetails.getAvailableSeats());
        }
        if (courseDetails.getDescription() != null) {
            course.setDescription(courseDetails.getDescription());
        }
        if (courseDetails.getEligibilityCriteria() != null) {
            course.setEligibilityCriteria(courseDetails.getEligibilityCriteria());
        }
        if (courseDetails.getFeeAmount() != null) {
            course.setFeeAmount(courseDetails.getFeeAmount());
        }
        if (courseDetails.getFeeType() != null) {
            course.setFeeType(courseDetails.getFeeType());
        }
        if (courseDetails.getIsActive() != null) {
            course.setIsActive(courseDetails.getIsActive());
        }
        if (courseDetails.getSubjects() != null) {
            course.setSubjects(courseDetails.getSubjects());
        }
        
        course.setUpdatedAt(LocalDateTime.now());
        
        return courseRepository.save(course);
    }
    
    // Delete course (Admin only)
    public void deleteCourse(String id) {
        if (!courseRepository.existsById(id)) {
            throw new RuntimeException("Course not found");
        }
        courseRepository.deleteById(id);
    }
    
    // Activate/Deactivate course (Admin only)
    public Course toggleCourseStatus(String id) {
        Optional<Course> courseOpt = courseRepository.findById(id);
        if (courseOpt.isEmpty()) {
            throw new RuntimeException("Course not found");
        }
        
        Course course = courseOpt.get();
        course.setIsActive(!course.getIsActive());
        course.setUpdatedAt(LocalDateTime.now());
        
        return courseRepository.save(course);
    }
    
    // Get course statistics
    public CourseStats getCourseStats() {
        long totalCourses = courseRepository.count();
        long activeCourses = courseRepository.countByIsActiveTrue();
        
        return new CourseStats(totalCourses, activeCourses);
    }
    
    // Initialize default courses (for first-time setup)
    public void initializeDefaultCourses() {
        if (courseRepository.count() == 0) {
            createDefaultCourses();
        }
    }
    
    private void createDefaultCourses() {
        // Undergraduate Courses
        Course cse = new Course("CSE101", "Computer Science Engineering", "Computer Science", "Undergraduate", 4, 60);
        cse.setDescription("Comprehensive program covering programming, algorithms, data structures, and software engineering.");
        cse.setFeeAmount(50000.0);
        cse.setFeeType("Per Year");
        cse.setEligibilityCriteria(List.of("12th Pass with PCM", "Minimum 60% marks", "JEE/Entrance Exam"));
        cse.setSubjects(List.of("Programming", "Data Structures", "Algorithms", "Database Systems", "Software Engineering"));
        
        Course ece = new Course("ECE101", "Electronics and Communication Engineering", "Electronics", "Undergraduate", 4, 50);
        ece.setDescription("Focus on electronics, communication systems, and signal processing.");
        ece.setFeeAmount(45000.0);
        ece.setFeeType("Per Year");
        ece.setEligibilityCriteria(List.of("12th Pass with PCM", "Minimum 60% marks", "JEE/Entrance Exam"));
        ece.setSubjects(List.of("Electronic Circuits", "Communication Systems", "Signal Processing", "Microprocessors"));
        
        Course mba = new Course("MBA101", "Master of Business Administration", "Management", "Postgraduate", 2, 40);
        mba.setDescription("Comprehensive business management program covering all aspects of business administration.");
        mba.setFeeAmount(80000.0);
        mba.setFeeType("Per Year");
        mba.setEligibilityCriteria(List.of("Bachelor's Degree", "Minimum 50% marks", "CAT/MAT/Entrance Exam"));
        mba.setSubjects(List.of("Marketing", "Finance", "Operations", "Human Resources", "Strategy"));
        
        Course bba = new Course("BBA101", "Bachelor of Business Administration", "Business Administration", "Undergraduate", 3, 45);
        bba.setDescription("Foundation program in business administration and management principles.");
        bba.setFeeAmount(35000.0);
        bba.setFeeType("Per Year");
        bba.setEligibilityCriteria(List.of("12th Pass", "Minimum 50% marks"));
        bba.setSubjects(List.of("Business Studies", "Economics", "Accounting", "Marketing Basics", "Management Principles"));
        
        Course mca = new Course("MCA101", "Master of Computer Applications", "Computer Science", "Postgraduate", 3, 35);
        mca.setDescription("Advanced program in computer applications and software development.");
        mca.setFeeAmount(60000.0);
        mca.setFeeType("Per Year");
        mca.setEligibilityCriteria(List.of("Bachelor's with Mathematics", "Minimum 50% marks", "Entrance Exam"));
        mca.setSubjects(List.of("Advanced Programming", "Database Management", "Web Technologies", "Software Engineering", "System Analysis"));
        
        // Save all courses
        courseRepository.saveAll(List.of(cse, ece, mba, bba, mca));
    }
    
    // Inner class for course statistics
    public static class CourseStats {
        private final long totalCourses;
        private final long activeCourses;
        
        public CourseStats(long totalCourses, long activeCourses) {
            this.totalCourses = totalCourses;
            this.activeCourses = activeCourses;
        }
        
        // Getters
        public long getTotalCourses() { return totalCourses; }
        public long getActiveCourses() { return activeCourses; }
    }
}
