package com.university.management.service;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.university.management.dto.request.AdminLoginRequest;
import com.university.management.dto.request.AdminRegistrationRequest;
import com.university.management.dto.response.AdminAuthResponse;
import com.university.management.dto.response.AdminResponse;
import com.university.management.dto.response.ApiResponse;
import com.university.management.model.Admin;
import com.university.management.repository.AdminRepository;
import com.university.management.security.JwtUtil;

@Service
public class AdminAuthService {
    
    @Autowired
    private AuthenticationManager authenticationManager;
    
    @Autowired
    private AdminRepository adminRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private JwtUtil jwtUtil;
    
    @Autowired
    private AdminService adminService;
    
    public ApiResponse<AdminAuthResponse> login(AdminLoginRequest loginRequest) {
        try {
            // Authenticate admin
            authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    loginRequest.getUsernameOrEmail(),
                    loginRequest.getPassword()
                )
            );
            
            // Get admin details
            Admin admin = adminRepository.findByUsernameOrEmail(
                loginRequest.getUsernameOrEmail(), 
                loginRequest.getUsernameOrEmail()
            ).orElseThrow(() -> new BadCredentialsException("Invalid credentials"));
            
            // Check if admin is active
            if (!admin.isActive()) {
                return ApiResponse.error("Admin account is deactivated");
            }
            
            // Update last login
            adminService.updateLastLogin(loginRequest.getUsernameOrEmail());
            
            // Generate JWT token
            String token = jwtUtil.generateTokenForAdmin(
                admin.getUsername(), // Use username as subject for admin
                admin.getId(),
                admin.getName(),
                admin.getRole(),
                admin.getDepartment()
            );
            
            // Create response
            AdminResponse adminResponse = new AdminResponse(admin);
            AdminAuthResponse authResponse = new AdminAuthResponse(token, adminResponse);
            
            return ApiResponse.success("Admin login successful", authResponse);
            
        } catch (AuthenticationException e) {
            return ApiResponse.error("Invalid username/email or password", e.getMessage());
        } catch (Exception e) {
            return ApiResponse.error("Admin login failed", e.getMessage());
        }
    }
    
    public ApiResponse<AdminAuthResponse> register(AdminRegistrationRequest registrationRequest) {
        try {
            // Check if admin already exists
            if (adminRepository.existsByUsername(registrationRequest.getUsername())) {
                return ApiResponse.error("Admin already exists with this username");
            }
            
            if (adminRepository.existsByEmail(registrationRequest.getEmail())) {
                return ApiResponse.error("Admin already exists with this email");
            }
            
            // Create new admin
            AdminResponse adminResponse = adminService.createAdmin(registrationRequest);
            
            // Get the actual admin entity to generate token
            Admin admin = adminRepository.findById(adminResponse.getId())
                    .orElseThrow(() -> new RuntimeException("Failed to retrieve created admin"));
            
            // Generate JWT token
            String token = jwtUtil.generateTokenForAdmin(
                admin.getUsername(),
                admin.getId(),
                admin.getName(),
                admin.getRole(),
                admin.getDepartment()
            );
            
            // Create response
            AdminAuthResponse authResponse = new AdminAuthResponse(token, adminResponse);
            
            return ApiResponse.success("Admin registration successful", authResponse);
            
        } catch (Exception e) {
            return ApiResponse.error("Admin registration failed", e.getMessage());
        }
    }
    
    public ApiResponse<String> refreshToken(String oldToken) {
        try {
            // Extract username from old token
            String usernameOrEmail = jwtUtil.extractUsername(oldToken);
            
            // Verify admin exists and is active
            Admin admin = adminRepository.findByUsernameOrEmail(usernameOrEmail, usernameOrEmail)
                .orElseThrow(() -> new BadCredentialsException("Admin not found"));
            
            if (!admin.isActive()) {
                throw new BadCredentialsException("Admin account is deactivated");
            }
            
            // Generate new token
            String newToken = jwtUtil.generateTokenForAdmin(
                admin.getUsername(),
                admin.getId(),
                admin.getName(),
                admin.getRole(),
                admin.getDepartment()
            );
            
            return ApiResponse.success("Admin token refreshed successfully", newToken);
            
        } catch (Exception e) {
            return ApiResponse.error("Admin token refresh failed", e.getMessage());
        }
    }
    
    public ApiResponse<String> changePassword(String usernameOrEmail, String oldPassword, String newPassword) {
        try {
            // Authenticate with old password
            authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(usernameOrEmail, oldPassword)
            );
            
            // Get admin
            Admin admin = adminRepository.findByUsernameOrEmail(usernameOrEmail, usernameOrEmail)
                .orElseThrow(() -> new BadCredentialsException("Admin not found"));
            
            // Update password
            admin.setPassword(passwordEncoder.encode(newPassword));
            admin.setUpdatedAt(LocalDateTime.now());
            adminRepository.save(admin);
            
            return ApiResponse.success("Admin password changed successfully");
            
        } catch (AuthenticationException e) {
            return ApiResponse.error("Current password is incorrect");
        } catch (Exception e) {
            return ApiResponse.error("Admin password change failed", e.getMessage());
        }
    }
    
    public ApiResponse<String> validateToken(String token) {
        try {
            String usernameOrEmail = jwtUtil.extractUsername(token);
            String userType = jwtUtil.extractUserType(token);
            
            if (!"admin".equals(userType)) {
                return ApiResponse.error("Invalid token type for admin");
            }
            
            Admin admin = adminRepository.findByUsernameOrEmail(usernameOrEmail, usernameOrEmail)
                .orElseThrow(() -> new BadCredentialsException("Admin not found"));
            
            if (!admin.isActive()) {
                return ApiResponse.error("Admin account is deactivated");
            }
            
            if (jwtUtil.validateToken(token)) {
                return ApiResponse.success("Admin token is valid", admin.getUsername());
            } else {
                return ApiResponse.error("Admin token is invalid or expired");
            }
            
        } catch (Exception e) {
            return ApiResponse.error("Admin token validation failed", e.getMessage());
        }
    }
    
    public ApiResponse<AdminResponse> getCurrentAdmin(String usernameOrEmail) {
        try {
            Admin admin = adminRepository.findByUsernameOrEmail(usernameOrEmail, usernameOrEmail)
                .orElseThrow(() -> new BadCredentialsException("Admin not found"));
            
            if (!admin.isActive()) {
                return ApiResponse.error("Admin account is deactivated");
            }
            
            AdminResponse adminResponse = new AdminResponse(admin);
            return ApiResponse.success("Admin details retrieved successfully", adminResponse);
            
        } catch (Exception e) {
            return ApiResponse.error("Failed to get admin details", e.getMessage());
        }
    }
}
