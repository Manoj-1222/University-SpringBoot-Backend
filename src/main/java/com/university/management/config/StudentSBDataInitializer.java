package com.university.management.config;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.university.management.model.Student;

/**
 * Data Initializer for students-SB collection - DEVELOPMENT ONLY
 * Populates the students-SB collection with 5 sample students (2 female, 3 male)
 * Only runs in development profile to avoid production data issues
 */
@Component
@Profile({"dev", "development", "local"}) // Only run in development
public class StudentSBDataInitializer implements CommandLineRunner {
    
    @Autowired
    private MongoTemplate mongoTemplate;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    private static final String COLLECTION_NAME = "students-SB";
    
    @Override
    public void run(String... args) throws Exception {
        // Check if students-SB collection already has data
        if (mongoTemplate.getCollection(COLLECTION_NAME).countDocuments() > 0) {
            System.out.println("ℹ️  students-SB collection already has data, skipping initialization");
            return;
        }
        
        System.out.println("🚀 Initializing students-SB collection...");
        
        // Create 5 students: 2 female, 3 male
        List<Student> studentsSB = createStudentsSBData();
        
        // Save students to the students-SB collection
        for (Student student : studentsSB) {
            mongoTemplate.save(student, COLLECTION_NAME);
        }
        
        System.out.println("✅ Successfully created " + studentsSB.size() + " students in students-SB collection");
        System.out.println("\n📋 Student Login Credentials for students-SB:");
        System.out.println("================================================");
        
        for (Student student : studentsSB) {
            System.out.println("👤 " + student.getName() + " (" + getGenderFromName(student.getName()) + ")");
            System.out.println("   📧 Email: " + student.getEmail());
            System.out.println("   🔑 Password: " + getPasswordFromEmail(student.getEmail()));
            System.out.println("   🎓 Roll No: " + student.getRollNo());
            System.out.println("   🏢 Department: " + student.getDepartment());
            System.out.println("   📅 Year: " + student.getYear() + ", Semester: " + student.getSemester());
            System.out.println("   ─────────────────────────────────────────────");
        }
        
        System.out.println("\n🎯 Collection Summary:");
        System.out.println("   📍 Collection Name: students-SB");
        System.out.println("   👥 Total Students: " + studentsSB.size());
        System.out.println("   👩 Female Students: 2");
        System.out.println("   👨 Male Students: 3");
        System.out.println("   💡 All passwords follow the pattern: firstname123");
    }
    
    private List<Student> createStudentsSBData() {
        LocalDateTime now = LocalDateTime.now();
        
        // Female Student 1
        Student student1 = new Student();
        student1.setName("Priya Sharma");
        student1.setRollNo("SB2025001");
        student1.setEmail("priya.sharma@university.edu");
        student1.setPassword(passwordEncoder.encode("priya123"));
        student1.setDepartment("Computer Science");
        student1.setYear(2);
        student1.setSemester(4);
        student1.setPhone("9876543201");
        student1.setDateOfBirth(LocalDate.of(2003, 5, 15));
        student1.setBloodGroup("B+");
        student1.setCurrentCGPA(8.5);
        student1.setTotalCredits(60);
        student1.setTotalFee(50000.0);
        student1.setPaidAmount(50000.0);
        student1.setPlacementStatus("Not Placed");
        student1.setCreatedAt(now);
        student1.setUpdatedAt(now);
        
        // Female Student 2
        Student student2 = new Student();
        student2.setName("Anita Patel");
        student2.setRollNo("SB2025002");
        student2.setEmail("anita.patel@university.edu");
        student2.setPassword(passwordEncoder.encode("anita123"));
        student2.setDepartment("Electronics");
        student2.setYear(3);
        student2.setSemester(6);
        student2.setPhone("9876543202");
        student2.setDateOfBirth(LocalDate.of(2002, 8, 22));
        student2.setBloodGroup("A+");
        student2.setCurrentCGPA(9.2);
        student2.setTotalCredits(90);
        student2.setTotalFee(45000.0);
        student2.setPaidAmount(45000.0);
        student2.setPlacementStatus("Placed");
        student2.setCompany("TCS");
        student2.setPackageAmount(600000.0);
        student2.setCreatedAt(now);
        student2.setUpdatedAt(now);
        
        // Male Student 1
        Student student3 = new Student();
        student3.setName("Rahul Kumar");
        student3.setRollNo("SB2025003");
        student3.setEmail("rahul.kumar@university.edu");
        student3.setPassword(passwordEncoder.encode("rahul123"));
        student3.setDepartment("Computer Science");
        student3.setYear(2);
        student3.setSemester(4);
        student3.setPhone("9876543203");
        student3.setDateOfBirth(LocalDate.of(2003, 2, 10));
        student3.setBloodGroup("O+");
        student3.setCurrentCGPA(7.8);
        student3.setTotalCredits(58);
        student3.setTotalFee(50000.0);
        student3.setPaidAmount(30000.0);
        student3.setPlacementStatus("Not Placed");
        student3.setCreatedAt(now);
        student3.setUpdatedAt(now);
        
        // Male Student 2
        Student student4 = new Student();
        student4.setName("Vikash Singh");
        student4.setRollNo("SB2025004");
        student4.setEmail("vikash.singh@university.edu");
        student4.setPassword(passwordEncoder.encode("vikash123"));
        student4.setDepartment("Management");
        student4.setYear(1);
        student4.setSemester(2);
        student4.setPhone("9876543204");
        student4.setDateOfBirth(LocalDate.of(2004, 11, 5));
        student4.setBloodGroup("AB+");
        student4.setCurrentCGPA(8.0);
        student4.setTotalCredits(30);
        student4.setTotalFee(35000.0);
        student4.setPaidAmount(35000.0);
        student4.setPlacementStatus("Not Placed");
        student4.setCreatedAt(now);
        student4.setUpdatedAt(now);
        
        // Male Student 3
        Student student5 = new Student();
        student5.setName("Amit Gupta");
        student5.setRollNo("SB2025005");
        student5.setEmail("amit.gupta@university.edu");
        student5.setPassword(passwordEncoder.encode("amit123"));
        student5.setDepartment("Computer Science");
        student5.setYear(4);
        student5.setSemester(8);
        student5.setPhone("9876543205");
        student5.setDateOfBirth(LocalDate.of(2001, 7, 18));
        student5.setBloodGroup("A-");
        student5.setCurrentCGPA(9.5);
        student5.setTotalCredits(120);
        student5.setTotalFee(50000.0);
        student5.setPaidAmount(50000.0);
        student5.setPlacementStatus("Placed");
        student5.setCompany("Infosys");
        student5.setPackageAmount(800000.0);
        student5.setCreatedAt(now);
        student5.setUpdatedAt(now);
        
        return Arrays.asList(student1, student2, student3, student4, student5);
    }
    
    private String getGenderFromName(String name) {
        // Simple gender identification based on common Indian names
        String firstName = name.split(" ")[0].toLowerCase();
        if (firstName.equals("priya") || firstName.equals("anita")) {
            return "Female";
        } else {
            return "Male";
        }
    }
    
    private String getPasswordFromEmail(String email) {
        // Extract first name from email and add 123
        String firstName = email.split("\\.")[0];
        return firstName + "123";
    }
}
