package com.university.management.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.university.management.model.Course;

@Repository
public interface CourseRepository extends MongoRepository<Course, String> {
    
    // Find by course code
    Optional<Course> findByCourseCode(String courseCode);
    
    // Find by course name
    Optional<Course> findByCourseName(String courseName);
    
    // Find by department
    List<Course> findByDepartment(String department);
    
    // Find by program type
    List<Course> findByProgramType(String programType);
    
    // Find active courses
    List<Course> findByIsActiveTrue();
    
    // Find courses with available seats
    @Query("{'availableSeats': {$gt: 0}, 'isActive': true}")
    List<Course> findAvailableCourses();
    
    // Find by department and program type
    List<Course> findByDepartmentAndProgramType(String department, String programType);
    
    // Find by duration
    List<Course> findByDurationYears(Integer durationYears);
    
    // Check if course code exists
    boolean existsByCourseCode(String courseCode);
    
    // Check if course name exists
    boolean existsByCourseName(String courseName);
    
    // Custom query to find courses by fee range
    @Query("{'feeAmount': {$gte: ?0, $lte: ?1}, 'isActive': true}")
    List<Course> findByFeeRange(Double minFee, Double maxFee);
    
    // Count active courses
    long countByIsActiveTrue();
    
    // Count courses by department
    long countByDepartment(String department);
}
