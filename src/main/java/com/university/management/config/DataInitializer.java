package com.university.management.config;

import java.time.LocalDateTime;
import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.university.management.model.Admin;

/**
 * Main Data Initializer - Creates default admin account
 * Runs in all profiles to ensure basic admin exists
 */
@Component
@Order(1) // Run before AdminDataInitializer
public class DataInitializer implements CommandLineRunner {
    
    @Autowired
    private MongoTemplate mongoTemplate;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    private static final String COLLECTION_NAME = "admins";
    
    @Override
    public void run(String... args) throws Exception {
        System.out.println("🔧 DataInitializer: Starting execution...");
        
        // Check if basic admin exists
        Query query = new Query(Criteria.where("username").is("admin"));
        if (mongoTemplate.exists(query, Admin.class, COLLECTION_NAME)) {
            System.out.println("ℹ️  Default admin user already exists, skipping initialization");
            return;
        }
        
        System.out.println("🚀 Creating default admin user...");
        
        // Create default admin
        Admin defaultAdmin = new Admin();
        defaultAdmin.setName("System Administrator");
        defaultAdmin.setUsername("admin");
        defaultAdmin.setEmail("admin@university.edu");
        defaultAdmin.setPassword(passwordEncoder.encode("admin123"));
        defaultAdmin.setRole("SUPER_ADMIN");
        defaultAdmin.setDepartment("Administration");
        defaultAdmin.setPhoneNumber("+91-9999999999");
        defaultAdmin.setPermissions(Arrays.asList(
            "MANAGE_APPLICATIONS", "MANAGE_STUDENTS", "MANAGE_COURSES", 
            "VIEW_REPORTS", "MANAGE_ACADEMIC_RECORDS", "MANAGE_ADMINS"
        ));
        defaultAdmin.setActive(true);
        defaultAdmin.setCreatedAt(LocalDateTime.now());
        defaultAdmin.setUpdatedAt(LocalDateTime.now());
        
        mongoTemplate.save(defaultAdmin, COLLECTION_NAME);
        
        System.out.println("✅ Successfully created default admin user");
        System.out.println("\n📋 Default Admin Login Credentials:");
        System.out.println("=====================================================");
        System.out.println("👤 Username: admin");
        System.out.println("📧 Email: admin@university.edu");
        System.out.println("🔑 Password: admin123");
        System.out.println("🎯 Role: SUPER_ADMIN");
        System.out.println("=====================================================");
    }
}