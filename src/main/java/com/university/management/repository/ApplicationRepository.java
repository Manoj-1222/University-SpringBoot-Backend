package com.university.management.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.university.management.model.Application;

@Repository
public interface ApplicationRepository extends MongoRepository<Application, String> {
    
    // Find by email
    Optional<Application> findByEmail(String email);
    
    // Find by application status (simplified - removed department and programType queries)
    List<Application> findByApplicationStatus(String applicationStatus);
    List<Application> findByApplicationStatus(String applicationStatus, Sort sort);
    
    // Find by desired course
    List<Application> findByDesiredCourse(String desiredCourse);
    List<Application> findByDesiredCourse(String desiredCourse, Sort sort);
    
    // Find by previous qualification (new field in simplified model)
    List<Application> findByPreviousQualification(String previousQualification);
    
    // Find by reviewer (simplified - uses reviewedBy instead of reviewedById)
    List<Application> findByReviewedBy(String reviewedBy);
    
    // Check if email already exists
    boolean existsByEmail(String email);
    
    // Custom query to find applications by status and course
    @Query("{'applicationStatus': ?0, 'desiredCourse': ?1}")
    List<Application> findByStatusAndCourse(String status, String course);
    
    // Custom query to find applied applications (waiting for review)
    @Query("{'applicationStatus': 'APPLIED'}")
    List<Application> findAppliedApplications();
    
    // Custom query to find approved applications
    @Query("{'applicationStatus': 'APPROVED'}")
    List<Application> findApprovedApplications();
    
    // Custom query to find rejected applications
    @Query("{'applicationStatus': 'REJECTED'}")
    List<Application> findRejectedApplications();
    
    // Custom query to find applications by previous qualification
    @Query("{'previousQualification': ?0}")
    List<Application> findByQualificationType(String qualification);
    
    // Count applications by status
    long countByApplicationStatus(String applicationStatus);
    
    // Count applications by course
    long countByDesiredCourse(String desiredCourse);
    
    // Count applications by previous qualification
    long countByPreviousQualification(String previousQualification);
}
