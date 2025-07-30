package com.university.management.model;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Document(collection = "courses")
public class Course {
    
    @Id
    private String id;
    
    @NotBlank(message = "Course code is required")
    private String courseCode; // e.g., "CSE101", "ECE201"
    
    @NotBlank(message = "Course name is required")
    private String courseName; // e.g., "Computer Science Engineering", "Electronics"
    
    @NotBlank(message = "Department is required")
    private String department; // e.g., "Computer Science", "Electronics"
    
    @NotBlank(message = "Program type is required")
    private String programType; // "Undergraduate", "Postgraduate", "Diploma"
    
    @NotNull(message = "Duration is required")
    private Integer durationYears; // Course duration in years
    
    @NotNull(message = "Total seats is required")
    private Integer totalSeats;
    
    private Integer availableSeats;
    
    private String description;
    
    private List<String> eligibilityCriteria; // e.g., ["12th Pass", "Minimum 60% marks"]
    
    private Double feeAmount;
    
    private String feeType; // "Per Year", "Total", "Per Semester"
    
    private Boolean isActive = true;
    
    private List<String> subjects; // Core subjects in the course
    
    @CreatedDate
    private LocalDateTime createdAt;
    
    @LastModifiedDate
    private LocalDateTime updatedAt;
    
    // Constructors
    public Course() {}
    
    public Course(String courseCode, String courseName, String department, String programType, Integer durationYears, Integer totalSeats) {
        this.courseCode = courseCode;
        this.courseName = courseName;
        this.department = department;
        this.programType = programType;
        this.durationYears = durationYears;
        this.totalSeats = totalSeats;
        this.availableSeats = totalSeats;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
    
    // Getters and Setters
    public String getId() {
        return id;
    }
    
    public void setId(String id) {
        this.id = id;
    }
    
    public String getCourseCode() {
        return courseCode;
    }
    
    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }
    
    public String getCourseName() {
        return courseName;
    }
    
    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }
    
    public String getDepartment() {
        return department;
    }
    
    public void setDepartment(String department) {
        this.department = department;
    }
    
    public String getProgramType() {
        return programType;
    }
    
    public void setProgramType(String programType) {
        this.programType = programType;
    }
    
    public Integer getDurationYears() {
        return durationYears;
    }
    
    public void setDurationYears(Integer durationYears) {
        this.durationYears = durationYears;
    }
    
    public Integer getTotalSeats() {
        return totalSeats;
    }
    
    public void setTotalSeats(Integer totalSeats) {
        this.totalSeats = totalSeats;
    }
    
    public Integer getAvailableSeats() {
        return availableSeats;
    }
    
    public void setAvailableSeats(Integer availableSeats) {
        this.availableSeats = availableSeats;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public List<String> getEligibilityCriteria() {
        return eligibilityCriteria;
    }
    
    public void setEligibilityCriteria(List<String> eligibilityCriteria) {
        this.eligibilityCriteria = eligibilityCriteria;
    }
    
    public Double getFeeAmount() {
        return feeAmount;
    }
    
    public void setFeeAmount(Double feeAmount) {
        this.feeAmount = feeAmount;
    }
    
    public String getFeeType() {
        return feeType;
    }
    
    public void setFeeType(String feeType) {
        this.feeType = feeType;
    }
    
    public Boolean getIsActive() {
        return isActive;
    }
    
    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }
    
    public List<String> getSubjects() {
        return subjects;
    }
    
    public void setSubjects(List<String> subjects) {
        this.subjects = subjects;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
    
    // Helper methods
    public boolean hasAvailableSeats() {
        return this.availableSeats != null && this.availableSeats > 0;
    }
    
    public void decreaseAvailableSeats() {
        if (this.availableSeats != null && this.availableSeats > 0) {
            this.availableSeats--;
        }
    }
    
    public void increaseAvailableSeats() {
        if (this.availableSeats != null && this.totalSeats != null && this.availableSeats < this.totalSeats) {
            this.availableSeats++;
        }
    }
}
