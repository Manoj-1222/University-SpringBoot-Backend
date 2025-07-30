package com.university.management.dto.request;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.PositiveOrZero;

/**
 * DTO for updating only academic information of students
 * Used by STAFF_ADMIN who have permission only for academic operations
 */
public class StudentAcademicUpdateDto {
    
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
    
    // Constructors
    public StudentAcademicUpdateDto() {}
    
    public StudentAcademicUpdateDto(Integer year, Integer semester, Double currentCGPA, 
                                  Integer totalCredits, Double attendancePercentage) {
        this.year = year;
        this.semester = semester;
        this.currentCGPA = currentCGPA;
        this.totalCredits = totalCredits;
        this.attendancePercentage = attendancePercentage;
    }
    
    // Getters and Setters
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
    
    public Double getAttendancePercentage() {
        return attendancePercentage;
    }
    
    public void setAttendancePercentage(Double attendancePercentage) {
        this.attendancePercentage = attendancePercentage;
    }
}
