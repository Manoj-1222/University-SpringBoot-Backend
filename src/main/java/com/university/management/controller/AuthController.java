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

import com.university.management.dto.request.LoginRequest;
import com.university.management.dto.request.StudentRegistrationRequest;
import com.university.management.dto.response.ApiResponse;
import com.university.management.dto.response.AuthResponse;
import com.university.management.dto.response.StudentResponse;
import com.university.management.service.AuthService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "*", maxAge = 3600)
public class AuthController {
    
    @Autowired
    private AuthService authService;
    
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponse>> login(@Valid @RequestBody LoginRequest loginRequest) {
        ApiResponse<AuthResponse> response = authService.login(loginRequest);
        HttpStatus status = response.isSuccess() ? HttpStatus.OK : HttpStatus.UNAUTHORIZED;
        return new ResponseEntity<>(response, status);
    }
    
    @PostMapping("/student-login")
    public ResponseEntity<ApiResponse<AuthResponse>> studentLogin(@Valid @RequestBody LoginRequest loginRequest) {
        ApiResponse<AuthResponse> response = authService.studentLogin(loginRequest);
        HttpStatus status = response.isSuccess() ? HttpStatus.OK : HttpStatus.UNAUTHORIZED;
        return new ResponseEntity<>(response, status);
    }
    
    @PostMapping("/register")
    public ResponseEntity<ApiResponse<AuthResponse>> register(@Valid @RequestBody StudentRegistrationRequest registrationRequest) {
        ApiResponse<AuthResponse> response = authService.register(registrationRequest);
        HttpStatus status = response.isSuccess() ? HttpStatus.CREATED : HttpStatus.BAD_REQUEST;
        return new ResponseEntity<>(response, status);
    }
    
    @PostMapping("/refresh-token")
    public ResponseEntity<ApiResponse<String>> refreshToken(@RequestHeader("Authorization") String token) {
        try {
            // Remove "Bearer " prefix
            String jwtToken = token.substring(7);
            ApiResponse<String> response = authService.refreshToken(jwtToken);
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
        String email = authentication.getName();
        
        ApiResponse<String> response = authService.changePassword(email, request.getOldPassword(), request.getNewPassword());
        HttpStatus status = response.isSuccess() ? HttpStatus.OK : HttpStatus.BAD_REQUEST;
        return new ResponseEntity<>(response, status);
    }
    
    @PostMapping("/forgot-password")
    public ResponseEntity<ApiResponse<String>> forgotPassword(@RequestBody ForgotPasswordRequest request) {
        ApiResponse<String> response = authService.forgotPassword(request.getEmail());
        HttpStatus status = response.isSuccess() ? HttpStatus.OK : HttpStatus.BAD_REQUEST;
        return new ResponseEntity<>(response, status);
    }
    
    @GetMapping("/validate-token")
    public ResponseEntity<ApiResponse<String>> validateToken(@RequestHeader("Authorization") String token) {
        try {
            // Remove "Bearer " prefix
            String jwtToken = token.substring(7);
            ApiResponse<String> response = authService.validateToken(jwtToken);
            HttpStatus status = response.isSuccess() ? HttpStatus.OK : HttpStatus.UNAUTHORIZED;
            return new ResponseEntity<>(response, status);
        } catch (Exception e) {
            ApiResponse<String> response = ApiResponse.error("Invalid token format");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }
    
    @GetMapping("/me")
    public ResponseEntity<ApiResponse<StudentResponse>> getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        
        ApiResponse<StudentResponse> response = authService.getCurrentUser(email);
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
        ApiResponse<String> response = ApiResponse.success("Logged out successfully");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    
    // Inner classes for request DTOs
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
    
    public static class ForgotPasswordRequest {
        private String email;
        
        // Constructors
        public ForgotPasswordRequest() {}
        
        public ForgotPasswordRequest(String email) {
            this.email = email;
        }
        
        // Getters and Setters
        public String getEmail() {
            return email;
        }
        
        public void setEmail(String email) {
            this.email = email;
        }
    }
}
