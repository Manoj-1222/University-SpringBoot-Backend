package com.university.management.model;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

@Document(collection = "applications")
public class Application {
    
    @Id
    private String id;
    
    @NotBlank(message = "Full name is required")
    private String fullName;
    
    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    @Indexed(unique = true)
    private String email;
    
    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = "^[+]?[0-9]{10,15}$", message = "Please provide a valid phone number")
    private String phoneNumber;
    
    @NotBlank(message = "Desired course is required")
    private String desiredCourse;
    
    // Previous Education - simplified to just qualification and grade
    @NotBlank(message = "Previous qualification is required")
    private String previousQualification; // e.g., "12th Grade", "Diploma", "Bachelor's"
    
    @NotBlank(message = "Previous grade/percentage is required")
    private String previousGrade; // Grade or percentage
    
    // Application Status
    private String applicationStatus = "APPLIED"; // APPLIED, APPROVED, REJECTED
    
    private String rejectionReason;
    
    // Admin Review Details
    private String reviewedBy; // Admin ID who reviewed
    private LocalDateTime reviewedAt;
    private String reviewComments;
    
    // Student Account Details (set when approved)
    private String generatedStudentId;
    private String generatedRollNumber;
    
    @CreatedDate
    private LocalDateTime applicationDate;
    
    @LastModifiedDate
    private LocalDateTime updatedAt;
    
    // Constructors
    public Application() {}
    
    public Application(String fullName, String email, String phoneNumber, String desiredCourse) {
        this.fullName = fullName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.desiredCourse = desiredCourse;
        this.applicationDate = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
    
    // Getters and Setters
    public String getId() {
        return id;
    }
    
    public void setId(String id) {
        this.id = id;
    }
    
    public String getFullName() {
        return fullName;
    }
    
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getPhoneNumber() {
        return phoneNumber;
    }
    
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
    
    public String getDesiredCourse() {
        return desiredCourse;
    }
    
    public void setDesiredCourse(String desiredCourse) {
        this.desiredCourse = desiredCourse;
    }
    
    public String getPreviousQualification() {
        return previousQualification;
    }
    
    public void setPreviousQualification(String previousQualification) {
        this.previousQualification = previousQualification;
    }
    
    public String getPreviousGrade() {
        return previousGrade;
    }
    
    public void setPreviousGrade(String previousGrade) {
        this.previousGrade = previousGrade;
    }
    
    public String getApplicationStatus() {
        return applicationStatus;
    }
    
    public void setApplicationStatus(String applicationStatus) {
        this.applicationStatus = applicationStatus;
    }
    
    public String getRejectionReason() {
        return rejectionReason;
    }
    
    public void setRejectionReason(String rejectionReason) {
        this.rejectionReason = rejectionReason;
    }
    
    public String getReviewedBy() {
        return reviewedBy;
    }
    
    public void setReviewedBy(String reviewedBy) {
        this.reviewedBy = reviewedBy;
    }
    
    public LocalDateTime getReviewedAt() {
        return reviewedAt;
    }
    
    public void setReviewedAt(LocalDateTime reviewedAt) {
        this.reviewedAt = reviewedAt;
    }
    
    public String getReviewComments() {
        return reviewComments;
    }
    
    public void setReviewComments(String reviewComments) {
        this.reviewComments = reviewComments;
    }
    
    public String getGeneratedStudentId() {
        return generatedStudentId;
    }
    
    public void setGeneratedStudentId(String generatedStudentId) {
        this.generatedStudentId = generatedStudentId;
    }
    
    public String getGeneratedRollNumber() {
        return generatedRollNumber;
    }
    
    public void setGeneratedRollNumber(String generatedRollNumber) {
        this.generatedRollNumber = generatedRollNumber;
    }
    
    public LocalDateTime getApplicationDate() {
        return applicationDate;
    }
    
    public void setApplicationDate(LocalDateTime applicationDate) {
        this.applicationDate = applicationDate;
    }
    
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
    
    // Helper methods
    public boolean isApplied() {
        return "APPLIED".equals(this.applicationStatus);
    }
    
    public boolean isApproved() {
        return "APPROVED".equals(this.applicationStatus);
    }
    
    public boolean isRejected() {
        return "REJECTED".equals(this.applicationStatus);
    }
}
