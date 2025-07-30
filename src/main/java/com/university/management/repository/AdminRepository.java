package com.university.management.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.university.management.model.Admin;

@Repository
public interface AdminRepository extends MongoRepository<Admin, String> {
    
    // Find admin by username
    Optional<Admin> findByUsername(String username);
    
    // Find admin by email
    Optional<Admin> findByEmail(String email);
    
    // Find admin by username or email
    Optional<Admin> findByUsernameOrEmail(String username, String email);
    
    // Check if username exists
    boolean existsByUsername(String username);
    
    // Check if email exists
    boolean existsByEmail(String email);
    
    // Find admins by role
    List<Admin> findByRole(String role);
    
    // Find active admins
    List<Admin> findByIsActive(boolean isActive);
    
    // Find admins by department
    List<Admin> findByDepartment(String department);
    
    // Count admins by role
    long countByRole(String role);
    
    // Count active admins
    long countByIsActive(boolean isActive);
}
