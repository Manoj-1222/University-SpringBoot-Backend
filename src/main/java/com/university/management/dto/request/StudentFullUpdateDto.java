package com.university.management.dto.request;

import java.time.LocalDate;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

public class StudentFullUpdateDto {
    
    @NotBlank(message = "Name is required")
    @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters")
    private String name;
    
    @Email(message = "Invalid email format")
    private String email;
    
    @Size(min = 10, max = 15, message = "Phone number must be between 10 and 15 characters")
    private String phone;
    
    private LocalDate dateOfBirth;
    
    @Pattern(regexp = "^(A\\+|A-|B\\+|B-|AB\\+|AB-|O\\+|O-)$", message = "Please provide a valid blood group")
    private String bloodGroup;
    
    private String department;
    
    @Min(value = 1, message = "Year must be at least 1")
    @Max(value = 4, message = "Year must not exceed 4")
    private Integer year;
    
    @Min(value = 1, message = "Semester must be at least 1")
    @Max(value = 8, message = "Semester must not exceed 8")
    private Integer semester;
    
    @DecimalMin(value = "0.0", message = "CGPA must be at least 0.0")
    @DecimalMax(value = "10.0", message = "CGPA must not exceed 10.0")
    private Double currentCGPA;
    
    @PositiveOrZero(message = "Total credits must be non-negative")
    private Integer totalCredits;
    
    // Attendance percentage
    @DecimalMin(value = "0.0", message = "Attendance must be at least 0%")
    @DecimalMax(value = "100.0", message = "Attendance must not exceed 100%")
    private Double attendancePercentage;
    
    // Fee Management
    @PositiveOrZero(message = "Total fee must be non-negative")
    private Double totalFee;
    
    @PositiveOrZero(message = "Paid amount must be non-negative")
    private Double paidAmount;
    
    // Placement Information
    @Pattern(regexp = "Not Placed|Placed|Higher Studies", message = "Invalid placement status")
    private String placementStatus;
    
    private String company;
    
    @PositiveOrZero(message = "Package amount must be non-negative")
    private Double packageAmount;
    
    // Constructors
    public StudentFullUpdateDto() {}
    
    // Getters and Setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    
    public LocalDate getDateOfBirth() { return dateOfBirth; }
    public void setDateOfBirth(LocalDate dateOfBirth) { this.dateOfBirth = dateOfBirth; }
    
    public String getBloodGroup() { return bloodGroup; }
    public void setBloodGroup(String bloodGroup) { this.bloodGroup = bloodGroup; }
    
    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }
    
    public Integer getYear() { return year; }
    public void setYear(Integer year) { this.year = year; }
    
    public Integer getSemester() { return semester; }
    public void setSemester(Integer semester) { this.semester = semester; }
    
    public Double getCurrentCGPA() { return currentCGPA; }
    public void setCurrentCGPA(Double currentCGPA) { this.currentCGPA = currentCGPA; }
    
    public Integer getTotalCredits() { return totalCredits; }
    public void setTotalCredits(Integer totalCredits) { this.totalCredits = totalCredits; }
    
    public Double getAttendancePercentage() { return attendancePercentage; }
    public void setAttendancePercentage(Double attendancePercentage) { this.attendancePercentage = attendancePercentage; }
    
    public Double getTotalFee() { return totalFee; }
    public void setTotalFee(Double totalFee) { this.totalFee = totalFee; }
    
    public Double getPaidAmount() { return paidAmount; }
    public void setPaidAmount(Double paidAmount) { this.paidAmount = paidAmount; }
    
    public String getPlacementStatus() { return placementStatus; }
    public void setPlacementStatus(String placementStatus) { this.placementStatus = placementStatus; }
    
    public String getCompany() { return company; }
    public void setCompany(String company) { this.company = company; }
    
    public Double getPackageAmount() { return packageAmount; }  
    public void setPackageAmount(Double packageAmount) { this.packageAmount = packageAmount; }
}
