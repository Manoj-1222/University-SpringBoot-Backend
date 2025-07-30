package com.university.management.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.university.management.dto.request.AdminLoginRequest;
import com.university.management.dto.request.AdminRegistrationRequest;
import com.university.management.dto.response.AdminAuthResponse;
import com.university.management.dto.response.AdminResponse;
import com.university.management.dto.response.ApiResponse;
import com.university.management.service.AdminAuthService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/admin/auth")
@CrossOrigin(origins = "*", maxAge = 3600)
public class AdminAuthController {
    
    @Autowired
    private AdminAuthService adminAuthService;
    
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AdminAuthResponse>> login(@Valid @RequestBody AdminLoginRequest loginRequest) {
        ApiResponse<AdminAuthResponse> response = adminAuthService.login(loginRequest);
        HttpStatus status = response.isSuccess() ? HttpStatus.OK : HttpStatus.UNAUTHORIZED;
        return new ResponseEntity<>(response, status);
    }
    
    @PostMapping("/register")
    public ResponseEntity<ApiResponse<AdminAuthResponse>> register(@Valid @RequestBody AdminRegistrationRequest registrationRequest) {
        ApiResponse<AdminAuthResponse> response = adminAuthService.register(registrationRequest);
        HttpStatus status = response.isSuccess() ? HttpStatus.CREATED : HttpStatus.BAD_REQUEST;
        return new ResponseEntity<>(response, status);
    }
    
    @PostMapping("/refresh-token")
    public ResponseEntity<ApiResponse<String>> refreshToken(@RequestHeader("Authorization") String token) {
        try {
            // Remove "Bearer " prefix
            String jwtToken = token.substring(7);
            ApiResponse<String> response = adminAuthService.refreshToken(jwtToken);
            HttpStatus status = response.isSuccess() ? HttpStatus.OK : HttpStatus.UNAUTHORIZED;
            return new ResponseEntity<>(response, status);
        } catch (Exception e) {
            ApiResponse<String> response = ApiResponse.error("Invalid token format");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }
    
    @PostMapping("/change-password")
    public ResponseEntity<ApiResponse<String>> changePassword(
            @RequestBody ChangePasswordRequest request) {
        
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String usernameOrEmail = authentication.getName();
        
        ApiResponse<String> response = adminAuthService.changePassword(usernameOrEmail, request.getOldPassword(), request.getNewPassword());
        HttpStatus status = response.isSuccess() ? HttpStatus.OK : HttpStatus.BAD_REQUEST;
        return new ResponseEntity<>(response, status);
    }
    
    @GetMapping("/validate-token")
    public ResponseEntity<ApiResponse<String>> validateToken(@RequestHeader("Authorization") String token) {
        try {
            // Remove "Bearer " prefix
            String jwtToken = token.substring(7);
            ApiResponse<String> response = adminAuthService.validateToken(jwtToken);
            HttpStatus status = response.isSuccess() ? HttpStatus.OK : HttpStatus.UNAUTHORIZED;
            return new ResponseEntity<>(response, status);
        } catch (Exception e) {
            ApiResponse<String> response = ApiResponse.error("Invalid token format");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }
    
    @GetMapping("/me")
    public ResponseEntity<ApiResponse<AdminResponse>> getCurrentAdmin() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String usernameOrEmail = authentication.getName();
        
        ApiResponse<AdminResponse> response = adminAuthService.getCurrentAdmin(usernameOrEmail);
        HttpStatus status = response.isSuccess() ? HttpStatus.OK : HttpStatus.NOT_FOUND;
        return new ResponseEntity<>(response, status);
    }
    
    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<String>> logout() {
        // In a real application, you might want to:
        // 1. Invalidate the token on the server side
        // 2. Add the token to a blacklist
        // 3. Clear any server-side session data
        
        // For now, just return success message
        ApiResponse<String> response = ApiResponse.success("Admin logged out successfully");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    
    // Inner class for request DTO
    public static class ChangePasswordRequest {
        private String oldPassword;
        private String newPassword;
        
        // Constructors
        public ChangePasswordRequest() {}
        
        public ChangePasswordRequest(String oldPassword, String newPassword) {
            this.oldPassword = oldPassword;
            this.newPassword = newPassword;
        }
        
        // Getters and Setters
        public String getOldPassword() {
            return oldPassword;
        }
        
        public void setOldPassword(String oldPassword) {
            this.oldPassword = oldPassword;
        }
        
        public String getNewPassword() {
            return newPassword;
        }
        
        public void setNewPassword(String newPassword) {
            this.newPassword = newPassword;
        }
    }
}
