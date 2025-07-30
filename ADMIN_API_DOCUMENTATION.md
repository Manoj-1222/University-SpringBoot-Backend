# Admin System API Documentation

## Overview
The admin system provides separate authentication and comprehensive student data management capabilities for administrators with role-based access control.

## Admin Roles
- **SUPER_ADMIN**: Full system access including admin management
- **ADMIN**: Complete student data management
- **ACADEMIC_ADMIN**: Academic data management (grades, attendance, academic info)
- **FINANCE_ADMIN**: Financial data management (fees, financial records)

## Authentication Endpoints

### Admin Login
**POST** `/api/admin/auth/login`
```json
{
  "username": "admin@university.com",
  "password": "admin123"
}
```

Response:
```json
{
  "success": true,
  "message": "Admin authenticated successfully",
  "data": {
    "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "type": "Bearer",
    "username": "admin",
    "email": "admin@university.com",
    "role": "ADMIN",
    "permissions": ["READ_STUDENTS", "WRITE_STUDENTS", "DELETE_STUDENTS"]
  }
}
```

### Admin Registration
**POST** `/api/admin/auth/register`
```json
{
  "username": "newadmin",
  "email": "newadmin@university.com",
  "password": "password123",
  "firstName": "John",
  "lastName": "Admin",
  "role": "ADMIN",
  "phone": "9876543210"
}
```

### Refresh Token
**POST** `/api/admin/auth/refresh-token`
Headers: `Authorization: Bearer <token>`

### Change Password
**POST** `/api/admin/auth/change-password`
```json
{
  "oldPassword": "oldpass123",
  "newPassword": "newpass123"
}
```

### Validate Token
**POST** `/api/admin/auth/validate-token`
Headers: `Authorization: Bearer <token>`

### Get Admin Profile
**GET** `/api/admin/auth/me`
Headers: `Authorization: Bearer <token>`

### Logout
**POST** `/api/admin/auth/logout`
Headers: `Authorization: Bearer <token>`

## Student Management Endpoints

### Get All Students
**GET** `/api/admin/students`
- Query Parameters:
  - `page` (default: 0): Page number for pagination
  - `size` (default: 20): Number of records per page
  - `sortBy` (default: "name"): Field to sort by
  - `sortDir` (default: "asc"): Sort direction (asc/desc)
  - Use `page=-1` to get all students without pagination

### Get Student by ID
**GET** `/api/admin/students/{id}`

### Get Student by Email
**GET** `/api/admin/students/email/{email}`

### Get Student by Roll Number
**GET** `/api/admin/students/rollno/{rollNo}`

### Get Students by Department
**GET** `/api/admin/students/department/{department}`

### Get Students by Year
**GET** `/api/admin/students/year/{year}`

### Get Placed Students
**GET** `/api/admin/students/placed`

### Create Student
**POST** `/api/admin/students`
```json
{
  "name": "John Doe",
  "email": "john.doe@student.university.com",
  "password": "student123",
  "rollNo": "CSE2024001",
  "department": "Computer Science",
  "year": 2,
  "semester": 4,
  "phone": "9876543210",
  "address": "123 Main St, City",
  "cgpa": 8.5,
  "credits": 120,
  "attendancePercentage": 85.0,
  "totalFee": 200000.0,
  "paidAmount": 150000.0,
  "placementStatus": "Not Placed"
}
```

### Update Student
**PUT** `/api/admin/students/{id}`
```json
{
  "name": "John Doe Updated",
  "email": "john.doe.updated@student.university.com",
  "department": "Computer Science Engineering",
  "year": 3,
  "semester": 5,
  "phone": "9876543211",
  "address": "456 New St, City",
  "cgpa": 8.7,
  "credits": 140,
  "attendancePercentage": 88.0,
  "totalFee": 250000.0,
  "paidAmount": 200000.0,
  "placementStatus": "Placed",
  "company": "Tech Corp",
  "packageAmount": 1200000.0
}
```

### Update Academic Information
**PUT** `/api/admin/students/{id}/academic`
```json
{
  "department": "Computer Science Engineering",
  "year": 3,
  "semester": 6
}
```

### Update CGPA
**PUT** `/api/admin/students/{id}/cgpa`
```json
{
  "cgpa": 8.8,
  "credits": 150
}
```

### Update Attendance
**PUT** `/api/admin/students/{id}/attendance`
```json
{
  "attendancePercentage": 90.0
}
```

### Update Fee Information
**PUT** `/api/admin/students/{id}/fee`
```json
{
  "totalFee": 300000.0,
  "paidAmount": 250000.0
}
```

### Update Placement Information
**PUT** `/api/admin/students/{id}/placement`
```json
{
  "placementStatus": "Placed",
  "company": "Google",
  "packageAmount": 2500000.0
}
```

### Delete Student (SUPER_ADMIN/ADMIN only)
**DELETE** `/api/admin/students/{id}`

## Statistics Endpoints

### Get Total Student Count
**GET** `/api/admin/students/count`

### Get Student Count by Department
**GET** `/api/admin/students/count/department/{department}`

### Get Placed Student Count
**GET** `/api/admin/students/count/placed`

## Admin Management Endpoints (SUPER_ADMIN only)

### Get All Admins
**GET** `/api/admin/admins`

### Create New Admin
**POST** `/api/admin/admins`
```json
{
  "username": "admin2",
  "email": "admin2@university.com",
  "password": "admin123",
  "firstName": "Jane",
  "lastName": "Smith",
  "role": "ACADEMIC_ADMIN",
  "phone": "9876543221"
}
```

### Update Admin Status
**PUT** `/api/admin/admins/{id}/status`
```json
{
  "isActive": true
}
```

## Error Responses

All endpoints return standardized error responses:
```json
{
  "success": false,
  "message": "Error description",
  "data": null,
  "error": "Detailed error message"
}
```

## Authentication
All admin endpoints (except auth endpoints) require the `Authorization` header:
```
Authorization: Bearer <jwt-token>
```

## Role-Based Access Control
- All `/api/admin/*` endpoints require admin role
- `/api/admin/students/{id}` DELETE requires SUPER_ADMIN or ADMIN role
- `/api/admin/admins/*` endpoints require SUPER_ADMIN role
- Academic operations can be performed by ACADEMIC_ADMIN
- Financial operations can be performed by FINANCE_ADMIN

## Sample Usage

1. **Admin Login:**
```bash
curl -X POST http://localhost:8080/api/admin/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"admin@university.com","password":"admin123"}'
```

2. **Get All Students:**
```bash
curl -X GET http://localhost:8080/api/admin/students \
  -H "Authorization: Bearer <token>"
```

3. **Create Student:**
```bash
curl -X POST http://localhost:8080/api/admin/students \
  -H "Authorization: Bearer <token>" \
  -H "Content-Type: application/json" \
  -d '{"name":"John Doe","email":"john@student.university.com","password":"student123","rollNo":"CSE2024001","department":"Computer Science","year":2,"semester":4}'
```

4. **Update Student CGPA:**
```bash
curl -X PUT http://localhost:8080/api/admin/students/12345/cgpa \
  -H "Authorization: Bearer <token>" \
  -H "Content-Type: application/json" \
  -d '{"cgpa":8.5,"credits":120}'
```

## Security Features
- JWT-based authentication with role information
- Password encryption using BCrypt
- Role-based method-level security
- CORS configuration for cross-origin requests
- Token validation and refresh mechanism
- Separate admin and student authentication flows
