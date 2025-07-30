package com.university.management.dto.response;

public class AuthResponse {
    
    private String token;
    private String type = "Bearer";
    private StudentResponse student;
    
    // Constructors
    public AuthResponse() {}
    
    public AuthResponse(String token, StudentResponse student) {
        this.token = token;
        this.student = student;
    }
    
    // Getters and Setters
    public String getToken() {
        return token;
    }
    
    public void setToken(String token) {
        this.token = token;
    }
    
    public String getType() {
        return type;
    }
    
    public void setType(String type) {
        this.type = type;
    }
    
    public StudentResponse getStudent() {
        return student;
    }
    
    public void setStudent(StudentResponse student) {
        this.student = student;
    }
}
