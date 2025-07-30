package com.university.management.security;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.university.management.model.Admin;
import com.university.management.model.Student;
import com.university.management.repository.AdminRepository;
import com.university.management.repository.StudentRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    
    @Autowired
    private StudentRepository studentRepository;
    
    @Autowired
    private AdminRepository adminRepository;
    
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // First try to find as admin (by username or email)
        var adminOptional = adminRepository.findByUsernameOrEmail(username, username);
        if (adminOptional.isPresent()) {
            Admin admin = adminOptional.get();
            return User.builder()
                    .username(admin.getUsername()) // Use username for admin
                    .password(admin.getPassword())
                    .authorities(Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + admin.getRole())))
                    .accountExpired(false)
                    .accountLocked(!admin.isActive())
                    .credentialsExpired(false)
                    .disabled(!admin.isActive())
                    .build();
        }
        
        // Then try to find as student (by email)
        var studentOptional = studentRepository.findByEmail(username);
        if (studentOptional.isPresent()) {
            Student student = studentOptional.get();
            return User.builder()
                    .username(student.getEmail()) // Use email for student
                    .password(student.getPassword())
                    .authorities(Collections.singletonList(new SimpleGrantedAuthority("ROLE_STUDENT")))
                    .build();
        }
        
        throw new UsernameNotFoundException("User not found: " + username);
    }
    
    public Student loadStudentByUsername(String username) throws UsernameNotFoundException {
        return studentRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Student not found with email: " + username));
    }
    
    public Admin loadAdminByUsernameOrEmail(String usernameOrEmail) throws UsernameNotFoundException {
        return adminRepository.findByUsernameOrEmail(usernameOrEmail, usernameOrEmail)
                .orElseThrow(() -> new UsernameNotFoundException("Admin not found: " + usernameOrEmail));
    }
}
