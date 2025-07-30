package com.university.management.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public class ApplicationRequestDto {
    
    @NotBlank(message = "Full name is required")
    private String fullName;
    
    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
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
    
    // Constructors
    public ApplicationRequestDto() {}
    
    // Getters and Setters
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
}
