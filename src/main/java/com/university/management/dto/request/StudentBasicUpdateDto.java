package com.university.management.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class StudentBasicUpdateDto {
    
    @Size(max = 100, message = "Name cannot exceed 100 characters")
    private String name;
    
    @Email(message = "Please provide a valid email")
    private String email;
    
    @Pattern(regexp = "^[+]?[0-9]{10,15}$", message = "Please provide a valid phone number")
    private String phone;
    
    // Note: The following fields are not in the current Student model
    // They would need to be added to the Student model if required:
    // - address, guardianName, guardianPhone, emergencyContactName, emergencyContactPhone
    
    // Constructors
    public StudentBasicUpdateDto() {}
    
    public StudentBasicUpdateDto(String name, String email, String phone) {
        this.name = name;
        this.email = email;
        this.phone = phone;
    }
    
    // Getters and Setters
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
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
}
