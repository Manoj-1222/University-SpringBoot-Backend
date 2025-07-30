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

import com.university.management.dto.request.StudentAcademicUpdateDto;
import com.university.management.dto.request.StudentFullUpdateDto;
import com.university.management.dto.request.StudentRegistrationRequest;
import com.university.management.dto.response.StudentResponse;
import com.university.management.model.Student;
import com.university.management.repository.StudentRepository;

@Service
public class StudentService {
    
    @Autowired
    private StudentRepository studentRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    // Get all students
    public List<StudentResponse> getAllStudents() {
        return studentRepository.findAll()
                .stream()
                .map(StudentResponse::new)
                .collect(Collectors.toList());
    }
    
    // Get student by ID
    public Optional<StudentResponse> getStudentById(String id) {
        return studentRepository.findById(id)
                .map(StudentResponse::new);
    }
    
    // Method to update student academic records (for STAFF_ADMIN)
    public StudentResponse updateStudentAcademicData(String id, StudentAcademicUpdateDto request) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Student not found with id: " + id));
        
        if (request.getCurrentCGPA() != null) {
            student.setCurrentCGPA(request.getCurrentCGPA());
        }
        if (request.getAttendancePercentage() != null) {
            // Update attendance percentage in the Attendance object
            if (student.getAttendance() != null) {
                student.getAttendance().setPercentage(request.getAttendancePercentage());
            }
        }
        if (request.getTotalCredits() != null) {
            student.setTotalCredits(request.getTotalCredits());
        }
        if (request.getSemester() != null) {
            student.setSemester(request.getSemester());
        }
        if (request.getYear() != null) {
            student.setYear(request.getYear());
        }
        
        student.setUpdatedAt(LocalDateTime.now());
        Student updatedStudent = studentRepository.save(student);
        return new StudentResponse(updatedStudent);
    }
    
    // Method to update complete student profile (for SUPER_ADMIN)
    public StudentResponse updateStudentFullData(String id, StudentFullUpdateDto request) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Student not found with id: " + id));
        
        if (request.getName() != null) {
            student.setName(request.getName());
        }
        if (request.getEmail() != null) {
            student.setEmail(request.getEmail());
        }
        if (request.getPhone() != null) {
            student.setPhone(request.getPhone());
        }
        if (request.getDepartment() != null) {
            student.setDepartment(request.getDepartment());
        }
        if (request.getCurrentCGPA() != null) {
            student.setCurrentCGPA(request.getCurrentCGPA());
        }
        if (request.getAttendancePercentage() != null) {
            if (student.getAttendance() != null) {
                student.getAttendance().setPercentage(request.getAttendancePercentage());
            }
        }
        if (request.getTotalCredits() != null) {
            student.setTotalCredits(request.getTotalCredits());
        }
        if (request.getSemester() != null) {
            student.setSemester(request.getSemester());
        }
        if (request.getYear() != null) {
            student.setYear(request.getYear());
        }
        if (request.getPaidAmount() != null) {
            student.setPaidAmount(request.getPaidAmount());
        }
        if (request.getTotalFee() != null) {
            student.setTotalFee(request.getTotalFee());
        }
        
        student.setUpdatedAt(LocalDateTime.now());
        Student updatedStudent = studentRepository.save(student);
        return new StudentResponse(updatedStudent);
    }
    
    // Create student (existing functionality)
    public StudentResponse createStudent(StudentRegistrationRequest request) {
        // Check if student already exists
        if (studentRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Student with this email already exists");
        }
        
        Student student = new Student();
        student.setName(request.getName());
        student.setEmail(request.getEmail());
        student.setPassword(passwordEncoder.encode(request.getPassword()));
        student.setPhone(request.getPhone());
        student.setDateOfBirth(request.getDateOfBirth());
        student.setCreatedAt(LocalDateTime.now());
        student.setUpdatedAt(LocalDateTime.now());
        
        Student savedStudent = studentRepository.save(student);
        return new StudentResponse(savedStudent);
    }
    
    // Additional methods required by StudentController
    public Page<StudentResponse> getStudentsPaginated(Pageable pageable) {
        return studentRepository.findAll(pageable)
                .map(StudentResponse::new);
    }
    
    public Optional<StudentResponse> getStudentByEmail(String email) {
        return studentRepository.findByEmail(email)
                .map(StudentResponse::new);
    }
    
    public Optional<StudentResponse> getStudentByRollNo(String rollNo) {
        return studentRepository.findByRollNo(rollNo)
                .map(StudentResponse::new);
    }
    
    public List<StudentResponse> getStudentsByDepartment(String department) {
        return studentRepository.findByDepartment(department)
                .stream()
                .map(StudentResponse::new)
                .collect(Collectors.toList());
    }
    
    public List<StudentResponse> getStudentsByYear(Integer year) {
        return studentRepository.findByYear(year)
                .stream()
                .map(StudentResponse::new)
                .collect(Collectors.toList());
    }
    
    public List<StudentResponse> getStudentsByDepartmentAndYear(String department, Integer year) {
        return studentRepository.findByDepartmentAndYear(department, year)
                .stream()
                .map(StudentResponse::new)
                .collect(Collectors.toList());
    }
    
    public List<StudentResponse> getPlacedStudents() {
        return studentRepository.findByPlacementStatus("Placed")
                .stream()
                .map(StudentResponse::new)
                .collect(Collectors.toList());
    }
    
    public StudentResponse updateStudent(String id, StudentRegistrationRequest request) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Student not found with id: " + id));
        
        student.setName(request.getName());
        student.setEmail(request.getEmail());
        if (request.getPassword() != null && !request.getPassword().isEmpty()) {
            student.setPassword(passwordEncoder.encode(request.getPassword()));
        }
        student.setPhone(request.getPhone());
        student.setDateOfBirth(request.getDateOfBirth());
        student.setUpdatedAt(LocalDateTime.now());
        
        Student savedStudent = studentRepository.save(student);
        return new StudentResponse(savedStudent);
    }
    
    public StudentResponse updateStudentAcademicInfo(String id, String department, Integer year, Integer semester) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Student not found with id: " + id));
        
        student.setDepartment(department);
        student.setYear(year);
        student.setSemester(semester);
        student.setUpdatedAt(LocalDateTime.now());
        
        Student savedStudent = studentRepository.save(student);
        return new StudentResponse(savedStudent);
    }
    
    public StudentResponse updateStudentCGPA(String id, Double cgpa, Integer semester) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Student not found with id: " + id));
        
        student.setCurrentCGPA(cgpa);
        student.setSemester(semester);
        student.setUpdatedAt(LocalDateTime.now());
        
        Student savedStudent = studentRepository.save(student);
        return new StudentResponse(savedStudent);
    }
    
    public StudentResponse updateStudentAttendance(String id, Double attendancePercentage) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Student not found with id: " + id));
        
        if (student.getAttendance() != null) {
            student.getAttendance().setPercentage(attendancePercentage);
        }
        student.setUpdatedAt(LocalDateTime.now());
        
        Student savedStudent = studentRepository.save(student);
        return new StudentResponse(savedStudent);
    }
    
    public StudentResponse updateStudentFee(String id, Double paidAmount, Double totalFee) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Student not found with id: " + id));
        
        student.setPaidAmount(paidAmount);
        student.setTotalFee(totalFee);
        student.setUpdatedAt(LocalDateTime.now());
        
        Student savedStudent = studentRepository.save(student);
        return new StudentResponse(savedStudent);
    }
    
    public StudentResponse updateStudentPlacement(String id, String companyName, String jobRole, Double salary) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Student not found with id: " + id));
        
        student.setCompany(companyName);
        student.setPackageAmount(salary);
        student.setPlacementStatus("Placed");
        student.setUpdatedAt(LocalDateTime.now());
        
        Student savedStudent = studentRepository.save(student);
        return new StudentResponse(savedStudent);
    }
    
    public void deleteStudent(String id) {
        if (!studentRepository.existsById(id)) {
            throw new IllegalArgumentException("Student not found with id: " + id);
        }
        studentRepository.deleteById(id);
    }
    
    public Long getTotalStudentCount() {
        return studentRepository.count();
    }
    
    public Long getStudentCountByDepartment(String department) {
        return studentRepository.countByDepartment(department);
    }
    
    public Long getPlacedStudentCount() {
        return studentRepository.countByPlacementStatus("Placed");
    }
}
