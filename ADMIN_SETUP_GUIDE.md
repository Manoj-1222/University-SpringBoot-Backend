# Admin System Setup & Usage Guide

## Quick Start

### 1. Prerequisites
- Java 17 or higher
- Maven 3.6+
- MongoDB Atlas account (or local MongoDB)
- IDE (VS Code, IntelliJ IDEA, Eclipse)

### 2. Configuration
The application is pre-configured with:
- **Database**: MongoDB Atlas connection
- **JWT Secret**: Pre-configured secret key
- **CORS**: Enabled for frontend development
- **Server Port**: 8080

### 3. Running the Application

#### Option 1: Using VS Code Task
1. Open the project in VS Code
2. Press `Ctrl+Shift+P` and select "Tasks: Run Task"
3. Choose "Run Java Spring Boot Backend"

#### Option 2: Using Maven Command
```bash
cd university-springboot-backend
mvn spring-boot:run
```

#### Option 3: Using Java (after building)
```bash
mvn clean package
java -jar target/university-management-0.0.1-SNAPSHOT.jar
```

### 4. Initial Admin Setup

#### Create First Super Admin (Manual Database Entry)
Since this is the first admin, you'll need to create one manually in the database:

1. **Password Hash Generation:**
   Use any online BCrypt generator or this Java code:
   ```java
   String password = "admin123";
   String hashedPassword = new BCryptPasswordEncoder().encode(password);
   System.out.println(hashedPassword); // Copy this hash
   ```

2. **MongoDB Document:**
   Insert this document into the `admins` collection:
   ```json
   {
     "_id": ObjectId(),
     "username": "superadmin",
     "email": "superadmin@university.com",
     "password": "$2a$10$[YOUR_BCRYPT_HASH_HERE]",
     "firstName": "Super",
     "lastName": "Admin",
     "role": "SUPER_ADMIN",
     "phone": "9999999999",
     "isActive": true,
     "permissions": ["READ_STUDENTS", "WRITE_STUDENTS", "DELETE_STUDENTS", "MANAGE_ADMINS"],
     "createdAt": new Date(),
     "updatedAt": new Date()
   }
   ```

#### Alternative: Use Registration Endpoint (Temporary)
For initial setup, you can temporarily remove authentication from the registration endpoint.

### 5. Testing the System

#### 1. Admin Login
```bash
curl -X POST http://localhost:8080/api/admin/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "superadmin@university.com",
    "password": "admin123"
  }'
```

#### 2. Get All Students
```bash
curl -X GET http://localhost:8080/api/admin/students \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

#### 3. Create a Student
```bash
curl -X POST http://localhost:8080/api/admin/students \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Test Student",
    "email": "test.student@university.com",
    "password": "student123",
    "rollNo": "CSE2024001",
    "department": "Computer Science",
    "year": 2,
    "semester": 4,
    "phone": "9876543210",
    "address": "Test Address",
    "cgpa": 8.0,
    "credits": 100,
    "attendancePercentage": 85.0,
    "totalFee": 200000.0,
    "paidAmount": 100000.0,
    "placementStatus": "Not Placed"
  }'
```

### 6. Available Admin Roles

| Role | Permissions | Description |
|------|-------------|-------------|
| **SUPER_ADMIN** | Full access | Can manage admins and all student data |
| **ADMIN** | Student CRUD | Complete student data management |
| **ACADEMIC_ADMIN** | Academic data | Grades, attendance, academic information |
| **FINANCE_ADMIN** | Financial data | Fees, payments, financial records |

### 7. Security Features

- **JWT Authentication**: Each admin gets a JWT token valid for 7 days
- **Role-Based Access**: Different roles have different permissions
- **Password Encryption**: All passwords are BCrypt encrypted
- **Method-Level Security**: `@PreAuthorize` annotations on sensitive operations
- **CORS Protection**: Configured for specific origins

### 8. Database Collections

The system uses these MongoDB collections:
- `admins`: Admin user data
- `students`: Student data (existing)
- Both collections are automatically created when first accessed

### 9. API Base URLs

- **Admin Authentication**: `http://localhost:8080/api/admin/auth/*`
- **Admin Student Management**: `http://localhost:8080/api/admin/students/*`
- **Admin Management**: `http://localhost:8080/api/admin/admins/*` (SUPER_ADMIN only)

### 10. Troubleshooting

#### Common Issues:

1. **"Access denied"**
   - Check JWT token is valid and not expired
   - Verify admin role has required permissions

2. **"Admin not found"**
   - Ensure admin exists in database
   - Check username/email spelling

3. **"Invalid credentials"**
   - Verify password is correct
   - Check if admin account is active

4. **Database connection issues**
   - Verify MongoDB Atlas connection string
   - Check network connectivity

#### Debug Mode:
Enable debug logging in `application.properties`:
```properties
logging.level.com.university.management=DEBUG
logging.level.org.springframework.security=DEBUG
```

### 11. Next Steps

1. **Create Additional Admins**: Use the SUPER_ADMIN account to create other admin accounts
2. **Import Student Data**: Use the student management endpoints to add existing students
3. **Frontend Integration**: Connect your React frontend to these admin APIs
4. **Customize Roles**: Modify the role system based on your requirements

## API Documentation

For complete API documentation, see `ADMIN_API_DOCUMENTATION.md`

## Support

If you encounter issues:
1. Check the console logs for error details
2. Verify JWT token format and expiration
3. Ensure database connectivity
4. Review role permissions for the operation
