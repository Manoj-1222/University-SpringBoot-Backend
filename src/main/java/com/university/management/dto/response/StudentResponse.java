package com.university.management.dto.response;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.university.management.model.Student;

public class StudentResponse {
    
    private String id;
    private String name;
    private String rollNo;
    private String email;
    private String phone;
    private LocalDate dateOfBirth;
    private String bloodGroup;
    private String department;
    private Integer year;
    private Integer semester;
    private Double currentCGPA;
    private Integer totalCredits;
    private Double totalFee;
    private Double paidAmount;
    private String placementStatus;
    private String company;
    private Double packageAmount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Student.Attendance attendance;
    
    // Constructors
    public StudentResponse() {}
    
    public StudentResponse(Student student) {
        this.id = student.getId();
        this.name = student.getName();
        this.rollNo = student.getRollNo();
        this.email = student.getEmail();
        this.phone = student.getPhone();
        this.dateOfBirth = student.getDateOfBirth();
        this.bloodGroup = student.getBloodGroup();
        this.department = student.getDepartment();
        this.year = student.getYear();
        this.semester = student.getSemester();
        this.currentCGPA = student.getCurrentCGPA();
        this.totalCredits = student.getTotalCredits();
        this.totalFee = student.getTotalFee();
        this.paidAmount = student.getPaidAmount();
        this.placementStatus = student.getPlacementStatus();
        this.company = student.getCompany();
        this.packageAmount = student.getPackageAmount();
        this.createdAt = student.getCreatedAt();
        this.updatedAt = student.getUpdatedAt();
        this.attendance = student.getAttendance();
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
    
    public Student.Attendance getAttendance() {
        return attendance;
    }
    
    public void setAttendance(Student.Attendance attendance) {
        this.attendance = attendance;
    }
    
    // Helper methods
    public Double getPendingAmount() {
        Double total = (totalFee != null) ? totalFee : 0.0;
        Double paid = (paidAmount != null) ? paidAmount : 0.0;
        return Math.max(0.0, total - paid);
    }
    
    public String getFeeStatus() {
        return getPendingAmount() <= 0.0 ? "Paid" : "Pending";
    }
}
