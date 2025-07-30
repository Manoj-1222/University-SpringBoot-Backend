package com.university.management.service;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.university.management.dto.request.LoginRequest;
import com.university.management.dto.request.StudentRegistrationRequest;
import com.university.management.dto.response.ApiResponse;
import com.university.management.dto.response.AuthResponse;
import com.university.management.dto.response.StudentResponse;
import com.university.management.model.Student;
import com.university.management.repository.StudentRepository;
import com.university.management.security.JwtUtil;

@Service
public class AuthService {
    
    @Autowired
    private AuthenticationManager authenticationManager;
    
    @Autowired
    private StudentRepository studentRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private JwtUtil jwtUtil;
    
    @Autowired
    private StudentService studentService;
    
    public ApiResponse<AuthResponse> login(LoginRequest loginRequest) {
        try {
            // Authenticate user
            authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    loginRequest.getEmail(),
                    loginRequest.getPassword()
                )
            );
            
            // Get student details
            Student student = studentRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new BadCredentialsException("Invalid credentials"));
            
            // Generate JWT token
            String token = jwtUtil.generateTokenForStudent(
                student.getEmail(),
                student.getId(),
                student.getName(),
                student.getRollNo(),
                student.getDepartment()
            );
            
            // Create response
            StudentResponse studentResponse = new StudentResponse(student);
            AuthResponse authResponse = new AuthResponse(token, studentResponse);
            
            return ApiResponse.success("Login successful", authResponse);
            
        } catch (AuthenticationException e) {
            return ApiResponse.error("Invalid email or password", e.getMessage());
        } catch (Exception e) {
            return ApiResponse.error("Login failed", e.getMessage());
        }
    }
    
    public ApiResponse<AuthResponse> studentLogin(LoginRequest loginRequest) {
        try {
            // Try to find student by email or roll number
            Student student = null;
            String identifier = loginRequest.getEmail();
            
            if (identifier.contains("@")) {
                student = studentRepository.findByEmail(identifier).orElse(null);
            } else {
                student = studentRepository.findByRollNo(identifier).orElse(null);
            }
            
            if (student == null) {
                return ApiResponse.error("Student not found with provided credentials");
            }
            
            // Verify password
            if (!passwordEncoder.matches(loginRequest.getPassword(), student.getPassword())) {
                return ApiResponse.error("Invalid credentials");
            }
            
            // Generate JWT token with STUDENT role
            String token = jwtUtil.generateTokenForStudent(
                student.getEmail(),
                student.getId(),
                student.getName(),
                student.getRollNo(),
                student.getDepartment()
            );
            
            // Create response
            StudentResponse studentResponse = new StudentResponse(student);
            AuthResponse authResponse = new AuthResponse(token, studentResponse);
            
            return ApiResponse.success("Student login successful", authResponse);
            
        } catch (Exception e) {
            return ApiResponse.error("Login failed", e.getMessage());
        }
    }
    
    public ApiResponse<AuthResponse> register(StudentRegistrationRequest registrationRequest) {
        try {
            // Check if user already exists
            if (studentRepository.existsByEmail(registrationRequest.getEmail())) {
                return ApiResponse.error("User already exists with this email");
            }
            
            // Create new student
            StudentResponse studentResponse = studentService.createStudent(registrationRequest);
            
            // Get the actual student entity to generate token
            Student student = studentRepository.findByEmail(studentResponse.getEmail())
                .orElseThrow(() -> new RuntimeException("Failed to retrieve created student"));
            
            // Generate JWT token
            String token = jwtUtil.generateTokenForStudent(
                student.getEmail(),
                student.getId(),
                student.getName(),
                student.getRollNo(),
                student.getDepartment()
            );
            
            // Create response
            AuthResponse authResponse = new AuthResponse(token, studentResponse);
            
            return ApiResponse.success("Registration successful", authResponse);
            
        } catch (Exception e) {
            return ApiResponse.error("Registration failed", e.getMessage());
        }
    }
    
    public ApiResponse<String> refreshToken(String oldToken) {
        try {
            // Extract email from old token
            String email = jwtUtil.extractUsername(oldToken);
            
            // Verify student exists
            Student student = studentRepository.findByEmail(email)
                .orElseThrow(() -> new BadCredentialsException("User not found"));
            
            // Generate new token
            String newToken = jwtUtil.generateTokenForStudent(
                student.getEmail(),
                student.getId(),
                student.getName(),
                student.getRollNo(),
                student.getDepartment()
            );
            
            return ApiResponse.success("Token refreshed successfully", newToken);
            
        } catch (Exception e) {
            return ApiResponse.error("Token refresh failed", e.getMessage());
        }
    }
    
    public ApiResponse<String> changePassword(String email, String oldPassword, String newPassword) {
        try {
            // Authenticate with old password
            authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, oldPassword)
            );
            
            // Get student
            Student student = studentRepository.findByEmail(email)
                .orElseThrow(() -> new BadCredentialsException("User not found"));
            
            // Update password
            student.setPassword(passwordEncoder.encode(newPassword));
            student.setUpdatedAt(LocalDateTime.now());
            studentRepository.save(student);
            
            return ApiResponse.success("Password changed successfully");
            
        } catch (AuthenticationException e) {
            return ApiResponse.error("Current password is incorrect");
        } catch (Exception e) {
            return ApiResponse.error("Password change failed", e.getMessage());
        }
    }
    
    public ApiResponse<String> forgotPassword(String email) {
        try {
            // Check if user exists
            if (!studentRepository.existsByEmail(email)) {
                throw new IllegalArgumentException("User not found with this email");
            }
            
            // In a real application, you would:
            // 1. Generate a password reset token
            // 2. Send an email with the reset link
            // 3. Store the token with expiration time
            
            // For now, just return success message
            return ApiResponse.success("Password reset instructions sent to your email");
            
        } catch (Exception e) {
            return ApiResponse.error("Password reset request failed", e.getMessage());
        }
    }
    
    public ApiResponse<String> validateToken(String token) {
        try {
            String email = jwtUtil.extractUsername(token);
            studentRepository.findByEmail(email)
                .orElseThrow(() -> new BadCredentialsException("User not found"));
            
            if (jwtUtil.validateToken(token)) {
                return ApiResponse.success("Token is valid", email);
            } else {
                return ApiResponse.error("Token is invalid or expired");
            }
            
        } catch (Exception e) {
            return ApiResponse.error("Token validation failed", e.getMessage());
        }
    }
    
    public ApiResponse<StudentResponse> getCurrentUser(String email) {
        try {
            Student student = studentRepository.findByEmail(email)
                .orElseThrow(() -> new BadCredentialsException("User not found"));
            
            StudentResponse studentResponse = new StudentResponse(student);
            return ApiResponse.success("User details retrieved successfully", studentResponse);
            
        } catch (Exception e) {
            return ApiResponse.error("Failed to get user details", e.getMessage());
        }
    }
}
