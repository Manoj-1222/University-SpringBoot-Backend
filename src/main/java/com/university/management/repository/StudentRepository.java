package com.university.management.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.university.management.model.Student;

@Repository
public interface StudentRepository extends MongoRepository<Student, String> {
    
    // Find student by email
    Optional<Student> findByEmail(String email);
    
    // Find student by roll number
    Optional<Student> findByRollNo(String rollNo);
    
    // Check if email exists
    boolean existsByEmail(String email);
    
    // Check if roll number exists
    boolean existsByRollNo(String rollNo);
    
    // Find students by department
    List<Student> findByDepartment(String department);
    Page<Student> findByDepartment(String department, Pageable pageable);
    
    // Find students by year
    List<Student> findByYear(Integer year);
    Page<Student> findByYear(Integer year, Pageable pageable);
    
    // Find students by department and year
    List<Student> findByDepartmentAndYear(String department, Integer year);
    Page<Student> findByDepartmentAndYear(String department, Integer year, Pageable pageable);
    
    // Find students by semester
    List<Student> findBySemester(Integer semester);
    Page<Student> findBySemester(Integer semester, Pageable pageable);
    
    // Find students by department and semester
    List<Student> findByDepartmentAndSemester(String department, Integer semester);
    Page<Student> findByDepartmentAndSemester(String department, Integer semester, Pageable pageable);
    
    // Find students by year and semester
    List<Student> findByYearAndSemester(Integer year, Integer semester);
    Page<Student> findByYearAndSemester(Integer year, Integer semester, Pageable pageable);
    
    // Find students by phone number
    Optional<Student> findByPhone(String phone);
    
    // Find students by blood group
    List<Student> findByBloodGroup(String bloodGroup);
    
    // Find students by placement status
    List<Student> findByPlacementStatus(String placementStatus);
    Page<Student> findByPlacementStatus(String placementStatus, Pageable pageable);
    
    // Find students by company (for placement tracking)
    List<Student> findByCompany(String company);
    Page<Student> findByCompany(String company, Pageable pageable);
    
    // Find students by CGPA range
    @Query("{'currentCGPA': {$gte: ?0, $lte: ?1}}")
    Page<Student> findByCurrentCGPABetween(Double minCGPA, Double maxCGPA, Pageable pageable);
    
    // Find students with pending fees
    @Query("{'totalFee': {$gt: 0}, $expr: {$gt: ['$totalFee', '$paidAmount']}}")
    Page<Student> findStudentsWithPendingFees(Pageable pageable);
    
    // Find students with low attendance (less than specified percentage)
    @Query("{'attendance.percentage': {$lt: ?0}}")
    Page<Student> findStudentsWithLowAttendance(Double minAttendance, Pageable pageable);
    
    // Find students by total credits range
    @Query("{'totalCredits': {$gte: ?0, $lte: ?1}}")
    Page<Student> findByTotalCreditsBetween(Integer minCredits, Integer maxCredits, Pageable pageable);
    
    // Find students by package amount range (for placement analysis)
    @Query("{'packageAmount': {$gte: ?0, $lte: ?1}}")
    Page<Student> findByPackageAmountBetween(Double minPackage, Double maxPackage, Pageable pageable);
    
    // Count students by year
    long countByYear(Integer year);
    
    // Count students by semester
    long countBySemester(Integer semester);
    
    // Count students by blood group
    long countByBloodGroup(String bloodGroup);
    
    // Count students by department
    long countByDepartment(String department);
    
    // Count students by placement status
    long countByPlacementStatus(String placementStatus);
}
