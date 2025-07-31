package com.university.management.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

@Document(collection = "students-SB")
public class Student {

    @Id
    private String id;

    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "Roll number is required")
    @Indexed(unique = true)
    private String rollNo;

    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    @Indexed(unique = true)
    private String email;

    @NotBlank(message = "Password is required")
    @JsonIgnore
    private String password;

    @NotBlank(message = "Department is required")
    private String department;

    @NotNull(message = "Year is required")
    @Min(value = 1, message = "Year must be at least 1")
    @Max(value = 4, message = "Year must be at most 4")
    private Integer year;

    @NotNull(message = "Semester is required")
    @Min(value = 1, message = "Semester must be at least 1")
    @Max(value = 8, message = "Semester must be at most 8")
    private Integer semester;

    private String phone;
    private LocalDate dateOfBirth;
    private String bloodGroup;

    // Academic Information
    @DecimalMin(value = "0.0", message = "CGPA must be at least 0.0")
    @DecimalMax(value = "10.0", message = "CGPA must be at most 10.0")
    private Double currentCGPA = 0.0;

    @Min(value = 0, message = "Total credits must be at least 0")
    private Integer totalCredits = 0;

    // Attendance
    private Attendance attendance = new Attendance();

    // Fee Structure
    @Min(value = 0, message = "Total fee must be at least 0")
    private Double totalFee = 0.0;

    @Min(value = 0, message = "Paid amount must be at least 0")
    private Double paidAmount = 0.0;

    // Placement Status
    @Pattern(regexp = "Not Placed|Placed|Higher Studies", message = "Invalid placement status")
    private String placementStatus = "Not Placed";

    private String company;
    private Double packageAmount;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    // Inner class for Attendance
    public static class Attendance {

        @DecimalMin(value = "0.0", message = "Attendance percentage must be at least 0.0")
        @DecimalMax(value = "100.0", message = "Attendance percentage must be at most 100.0")
        private Double percentage = 0.0;

        // Constructors
        public Attendance() {
        }

        public Attendance(Double percentage) {
            this.percentage = percentage;
        }

        // Getters and Setters
        public Double getPercentage() {
            return percentage;
        }

        public void setPercentage(Double percentage) {
            this.percentage = percentage;
        }
    }

    // Constructors
    public Student() {
    }

    public Student(String name, String rollNo, String email, String password,
            String department, Integer year, Integer semester) {
        this.name = name;
        this.rollNo = rollNo;
        this.email = email;
        this.password = password;
        this.department = department;
        this.year = year;
        this.semester = semester;
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRollNo() {
        return rollNo;
    }

    public void setRollNo(String rollNo) {
        this.rollNo = rollNo;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Integer getSemester() {
        return semester;
    }

    public void setSemester(Integer semester) {
        this.semester = semester;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getBloodGroup() {
        return bloodGroup;
    }

    public void setBloodGroup(String bloodGroup) {
        this.bloodGroup = bloodGroup;
    }

    public Double getCurrentCGPA() {
        return currentCGPA;
    }

    public void setCurrentCGPA(Double currentCGPA) {
        this.currentCGPA = currentCGPA;
    }

    public Integer getTotalCredits() {
        return totalCredits;
    }

    public void setTotalCredits(Integer totalCredits) {
        this.totalCredits = totalCredits;
    }

    public Attendance getAttendance() {
        return attendance;
    }

    public void setAttendance(Attendance attendance) {
        this.attendance = attendance;
    }

    public Double getTotalFee() {
        return totalFee;
    }

    public void setTotalFee(Double totalFee) {
        this.totalFee = totalFee;
    }

    public Double getPaidAmount() {
        return paidAmount;
    }

    public void setPaidAmount(Double paidAmount) {
        this.paidAmount = paidAmount;
    }

    public String getPlacementStatus() {
        return placementStatus;
    }

    public void setPlacementStatus(String placementStatus) {
        this.placementStatus = placementStatus;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public Double getPackageAmount() {
        return packageAmount;
    }

    public void setPackageAmount(Double packageAmount) {
        this.packageAmount = packageAmount;
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
    public Double getPendingAmount() {
        double total = (totalFee != null) ? totalFee.doubleValue() : 0.0;
        double paid = (paidAmount != null) ? paidAmount.doubleValue() : 0.0;
        return Math.max(0.0, total - paid);
    }

    public String getFeeStatus() {
        return getPendingAmount() <= 0.0 ? "Paid" : "Pending";
    }
}
