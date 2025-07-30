package com.university.management.config;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.university.management.model.Admin;

/**
 * Additional Admin Data Initializer - DEVELOPMENT ONLY
 * Populates the admin collection with additional admin users
 * Only runs in development profile to avoid production data issues
 */
@Component
@Profile({"dev", "development", "local"}) // Only run in development
@Order(2) // Run after the main DataInitializer
public class AdminDataInitializer implements CommandLineRunner {
    
    @Autowired
    private MongoTemplate mongoTemplate;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    private static final String COLLECTION_NAME = "admins";
    
    @Override
    public void run(String... args) throws Exception {
        System.out.println("🔧 AdminDataInitializer: Starting execution...");
        
        // Check if additional admin users already exist by checking for specific usernames
        List<String> additionalUsernames = Arrays.asList("academic_head", "finance_manager", "dean_student", "registrar", "hod_cs");
        
        boolean allExist = true;
        for (String username : additionalUsernames) {
            Query query = new Query(Criteria.where("username").is(username));
            if (!mongoTemplate.exists(query, Admin.class, COLLECTION_NAME)) {
                allExist = false;
                break;
            }
        }
        
        if (allExist) {
            System.out.println("ℹ️  Additional admin users already exist, skipping initialization");
            return;
        }
        
        System.out.println("🚀 Initializing additional admin users...");
        
        // Create additional admin users
        List<Admin> additionalAdmins = createAdditionalAdminData();
        
        // Save admins to the collection (only if they don't exist)
        int created = 0;
        for (Admin admin : additionalAdmins) {
            Query query = new Query(Criteria.where("username").is(admin.getUsername()));
            if (!mongoTemplate.exists(query, Admin.class, COLLECTION_NAME)) {
                mongoTemplate.save(admin, COLLECTION_NAME);
                created++;
            }
        }
        
        System.out.println("✅ Successfully created " + created + " additional admin users");
        System.out.println("\n📋 Additional Admin Login Credentials:");
        System.out.println("=====================================================");
        
        for (Admin admin : additionalAdmins) {
            System.out.println("👤 " + admin.getName() + " (" + admin.getRole() + ")");
            System.out.println("   👤 Username: " + admin.getUsername());
            System.out.println("   📧 Email: " + admin.getEmail());
            System.out.println("   🔑 Password: " + getPasswordFromUsername(admin.getUsername()));
            System.out.println("   🏢 Department: " + admin.getDepartment());
            System.out.println("   📞 Phone: " + admin.getPhoneNumber());
            System.out.println("   🎯 Permissions: " + String.join(", ", admin.getPermissions()));
            System.out.println("   ──────────────────────────────────────────────────");
        }
        
        System.out.println("\n🎯 Admin Collection Summary:");
        System.out.println("   📍 Collection Name: admins");
        System.out.println("   👥 Total Additional Admins: " + additionalAdmins.size());
        System.out.println("   🔐 All passwords follow the pattern: username123");
        System.out.println("   💡 Existing admins: admin/admin123, staff/staff123");
    }
    
    private List<Admin> createAdditionalAdminData() {
        LocalDateTime now = LocalDateTime.now();
        
        // Academic Head (SUPER_ADMIN)
        Admin academicHead = new Admin();
        academicHead.setName("Dr. Rajesh Gupta");
        academicHead.setUsername("academic_head");
        academicHead.setEmail("rajesh.gupta@university.edu");
        academicHead.setPassword(passwordEncoder.encode("academic_head123"));
        academicHead.setRole("SUPER_ADMIN");
        academicHead.setDepartment("Academic Affairs");
        academicHead.setPhoneNumber("+91-9876543210");
        academicHead.setPermissions(Arrays.asList(
            "MANAGE_APPLICATIONS", "MANAGE_STUDENTS", "MANAGE_COURSES", "VIEW_REPORTS", "MANAGE_ACADEMIC_RECORDS"
        ));
        academicHead.setActive(true);
        academicHead.setCreatedAt(now);
        academicHead.setUpdatedAt(now);
        
        // Finance Manager (STAFF_ADMIN)
        Admin financeManager = new Admin();
        financeManager.setName("Ms. Priya Patel");
        financeManager.setUsername("finance_manager");
        financeManager.setEmail("priya.patel@university.edu");
        financeManager.setPassword(passwordEncoder.encode("finance_manager123"));
        financeManager.setRole("STAFF_ADMIN");
        financeManager.setDepartment("Finance");
        financeManager.setPhoneNumber("+91-9876543211");
        financeManager.setPermissions(Arrays.asList(
            "MANAGE_FEES", "VIEW_REPORTS", "VIEW_STUDENTS"
        ));
        financeManager.setActive(true);
        financeManager.setCreatedAt(now);
        financeManager.setUpdatedAt(now);
        
        // Dean of Students (SUPER_ADMIN)
        Admin deanStudent = new Admin();
        deanStudent.setName("Prof. Amit Kumar");
        deanStudent.setUsername("dean_student");
        deanStudent.setEmail("amit.kumar@university.edu");
        deanStudent.setPassword(passwordEncoder.encode("dean_student123"));
        deanStudent.setRole("SUPER_ADMIN");
        deanStudent.setDepartment("Student Affairs");
        deanStudent.setPhoneNumber("+91-9876543212");
        deanStudent.setPermissions(Arrays.asList(
            "MANAGE_STUDENTS", "MANAGE_APPLICATIONS", "VIEW_REPORTS", "MANAGE_PLACEMENTS"
        ));
        deanStudent.setActive(true);
        deanStudent.setCreatedAt(now);
        deanStudent.setUpdatedAt(now);
        
        // Registrar (STAFF_ADMIN)
        Admin registrar = new Admin();
        registrar.setName("Mr. Suresh Sharma");
        registrar.setUsername("registrar");
        registrar.setEmail("suresh.sharma@university.edu");
        registrar.setPassword(passwordEncoder.encode("registrar123"));
        registrar.setRole("STAFF_ADMIN");
        registrar.setDepartment("Registration");
        registrar.setPhoneNumber("+91-9876543213");
        registrar.setPermissions(Arrays.asList(
            "MANAGE_APPLICATIONS", "VIEW_STUDENTS", "EDIT_ACADEMIC_RECORDS"
        ));
        registrar.setActive(true);
        registrar.setCreatedAt(now);
        registrar.setUpdatedAt(now);
        
        // HOD Computer Science (STAFF_ADMIN)
        Admin hodCS = new Admin();
        hodCS.setName("Dr. Neha Singh");
        hodCS.setUsername("hod_cs");
        hodCS.setEmail("neha.singh@university.edu");
        hodCS.setPassword(passwordEncoder.encode("hod_cs123"));
        hodCS.setRole("STAFF_ADMIN");
        hodCS.setDepartment("Computer Science");
        hodCS.setPhoneNumber("+91-9876543214");
        hodCS.setPermissions(Arrays.asList(
            "VIEW_STUDENTS", "EDIT_ACADEMIC_RECORDS", "MANAGE_COURSES"
        ));
        hodCS.setActive(true);
        hodCS.setCreatedAt(now);
        hodCS.setUpdatedAt(now);
        
        return Arrays.asList(academicHead, financeManager, deanStudent, registrar, hodCS);
    }
    
    private String getPasswordFromUsername(String username) {
        return username + "123";
    }
}
