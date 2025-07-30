package com.university.management.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.university.management.dto.request.AdminRegistrationRequest;
import com.university.management.dto.response.AdminResponse;
import com.university.management.model.Admin;
import com.university.management.repository.AdminRepository;

@Service
public class AdminService {
    
    @Autowired
    private AdminRepository adminRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    public AdminResponse createAdmin(AdminRegistrationRequest request) {
        // Check if admin already exists
        if (adminRepository.existsByUsername(request.getUsername())) {
            throw new IllegalArgumentException("Admin with this username already exists");
        }
        
        if (adminRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Admin with this email already exists");
        }
        
        Admin admin = new Admin();
        admin.setName(request.getName());
        admin.setUsername(request.getUsername());
        admin.setEmail(request.getEmail());
        admin.setPassword(passwordEncoder.encode(request.getPassword()));
        admin.setRole(request.getRole());
        admin.setDepartment(request.getDepartment());
        admin.setPhoneNumber(request.getPhoneNumber());
        admin.setPermissions(request.getPermissions());
        admin.setActive(true);
        admin.setCreatedAt(LocalDateTime.now());
        admin.setUpdatedAt(LocalDateTime.now());
        
        Admin savedAdmin = adminRepository.save(admin);
        return new AdminResponse(savedAdmin);
    }
    
    // Helper method to check if admin is SUPER_ADMIN (for application management)
    public boolean isSuperAdmin(String username) {
        Optional<Admin> admin = adminRepository.findByUsername(username);
        return admin.isPresent() && admin.get().isSuperAdmin();
    }
    
    public Optional<AdminResponse> getAdminById(String id) {
        return adminRepository.findById(id)
                .map(AdminResponse::new);
    }
    
    public Optional<AdminResponse> getAdminByUsername(String username) {
        return adminRepository.findByUsername(username)
                .map(AdminResponse::new);
    }
    
    public Optional<AdminResponse> getAdminByEmail(String email) {
        return adminRepository.findByEmail(email)
                .map(AdminResponse::new);
    }
    
    public Optional<AdminResponse> getAdminByUsernameOrEmail(String usernameOrEmail) {
        return adminRepository.findByUsernameOrEmail(usernameOrEmail, usernameOrEmail)
                .map(AdminResponse::new);
    }
    
    public List<AdminResponse> getAllAdmins() {
        return adminRepository.findAll()
                .stream()
                .map(AdminResponse::new)
                .collect(Collectors.toList());
    }
    
    public Page<AdminResponse> getAdminsPaginated(Pageable pageable) {
        return adminRepository.findAll(pageable)
                .map(AdminResponse::new);
    }
    
    public List<AdminResponse> getAdminsByRole(String role) {
        return adminRepository.findByRole(role)
                .stream()
                .map(AdminResponse::new)
                .collect(Collectors.toList());
    }
    
    public List<AdminResponse> getActiveAdmins() {
        return adminRepository.findByIsActive(true)
                .stream()
                .map(AdminResponse::new)
                .collect(Collectors.toList());
    }
    
    public List<AdminResponse> getAdminsByDepartment(String department) {
        return adminRepository.findByDepartment(department)
                .stream()
                .map(AdminResponse::new)
                .collect(Collectors.toList());
    }
    
    public AdminResponse updateAdmin(String id, AdminRegistrationRequest request) {
        Admin admin = adminRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Admin not found with id: " + id));
        
        // Check if username is being changed and if it's already taken
        if (!admin.getUsername().equals(request.getUsername())) {
            if (adminRepository.existsByUsername(request.getUsername())) {
                throw new IllegalArgumentException("Another admin with this username already exists");
            }
            admin.setUsername(request.getUsername());
        }
        
        // Check if email is being changed and if it's already taken
        if (!admin.getEmail().equals(request.getEmail())) {
            if (adminRepository.existsByEmail(request.getEmail())) {
                throw new IllegalArgumentException("Another admin with this email already exists");
            }
            admin.setEmail(request.getEmail());
        }
        
        admin.setName(request.getName());
        admin.setRole(request.getRole());
        admin.setDepartment(request.getDepartment());
        admin.setPhoneNumber(request.getPhoneNumber());
        admin.setPermissions(request.getPermissions());
        admin.setUpdatedAt(LocalDateTime.now());
        
        // Only update password if provided
        if (request.getPassword() != null && !request.getPassword().trim().isEmpty()) {
            admin.setPassword(passwordEncoder.encode(request.getPassword()));
        }
        
        Admin updatedAdmin = adminRepository.save(admin);
        return new AdminResponse(updatedAdmin);
    }
    
    public AdminResponse updateAdminStatus(String id, boolean isActive) {
        Admin admin = adminRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Admin not found with id: " + id));
        
        admin.setActive(isActive);
        admin.setUpdatedAt(LocalDateTime.now());
        
        Admin updatedAdmin = adminRepository.save(admin);
        return new AdminResponse(updatedAdmin);
    }
    
    public AdminResponse updateLastLogin(String usernameOrEmail) {
        Admin admin = adminRepository.findByUsernameOrEmail(usernameOrEmail, usernameOrEmail)
                .orElseThrow(() -> new IllegalArgumentException("Admin not found"));
        
        admin.setLastLogin(LocalDateTime.now());
        admin.setUpdatedAt(LocalDateTime.now());
        
        Admin updatedAdmin = adminRepository.save(admin);
        return new AdminResponse(updatedAdmin);
    }
    
    public void deleteAdmin(String id) {
        if (!adminRepository.existsById(id)) {
            throw new IllegalArgumentException("Admin not found with id: " + id);
        }
        adminRepository.deleteById(id);
    }
    
    public boolean existsByUsername(String username) {
        return adminRepository.existsByUsername(username);
    }
    
    public boolean existsByEmail(String email) {
        return adminRepository.existsByEmail(email);
    }
    
    public long getTotalAdminCount() {
        return adminRepository.count();
    }
    
    public long getAdminCountByRole(String role) {
        return adminRepository.countByRole(role);
    }
    
    public long getActiveAdminCount() {
        return adminRepository.countByIsActive(true);
    }
    
    // Method to find admin entity (for authentication service)
    public Optional<Admin> findAdminEntityByUsernameOrEmail(String usernameOrEmail) {
        return adminRepository.findByUsernameOrEmail(usernameOrEmail, usernameOrEmail);
    }
}
