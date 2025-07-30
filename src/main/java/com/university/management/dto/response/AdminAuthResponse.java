package com.university.management.dto.response;

public class AdminAuthResponse {
    
    private String token;
    private String type = "Bearer";
    private AdminResponse admin;
    
    // Constructors
    public AdminAuthResponse() {}
    
    public AdminAuthResponse(String token, AdminResponse admin) {
        this.token = token;
        this.admin = admin;
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
    
    public AdminResponse getAdmin() {
        return admin;
    }
    
    public void setAdmin(AdminResponse admin) {
        this.admin = admin;
    }
}
