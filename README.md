# 🎓 University Management System - Spring Boot Backend

[![Java](https://img.shields.io/badge/Java-17+-blue.svg)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2.0-green.svg)](https://spring.io/projects/spring-boot)
[![MongoDB](https://img.shields.io/badge/MongoDB-Atlas-green.svg)](https://www.mongodb.com/atlas)

A comprehensive Spring Boot backend for University Management System with JWT authentication, role-based access control, and complete CRUD operations.

## 🚀 Features

- **🔐 JWT Authentication** - Secure token-based authentication for admin and students
- **👥 Student Management** - Complete CRUD operations with role-based access control
- **🛡️ Admin Dashboard** - Full administrative control over student records
- **📊 MongoDB Integration** - Cloud database with Spring Data MongoDB
- **🔒 Security First** - Spring Security 6 with BCrypt password encryption
- **⚡ Production Ready** - Optimized for cloud deployment with Railway/Render support

## 🛠 Tech Stack

- **Backend**: Spring Boot 3.2.0, Spring Security 6, Spring Data MongoDB
- **Language**: Java 17+
- **Database**: MongoDB Atlas
- **Authentication**: JWT with role-based access (SUPER_ADMIN, STAFF_ADMIN, STUDENT)
- **Build**: Maven

## ⚡ Quick Start

### Prerequisites
- Java 17+
- Maven 3.6+
- MongoDB Atlas account

### Run Locally
```bash
git clone https://github.com/Manoj-1222/University-SpringBoot-Backend.git
cd University-SpringBoot-Backend
mvn spring-boot:run
│       ├── AuthResponse.java                # Authentication response DTO
│       ```

Application runs on: `http://localhost:8080/api`

## 🌐 API Endpoints

### Authentication
```
POST /api/admin/auth/login    # Admin login
POST /api/auth/login          # Student login
POST /api/auth/register       # Student registration
```

### Admin - Student Management
```
GET    /api/students          # Get all students
POST   /api/students          # Create new student
GET    /api/students/{id}     # Get student by ID
PUT    /api/students/{id}     # Update student
DELETE /api/students/{id}     # Delete student
```

### Health Check
```
GET /api/health               # Application status
```

## 🔐 Default Credentials

**Admin Account:**
- Username: `admin`
- Password: `admin123`
- Role: SUPER_ADMIN

## 🚀 Deployment

### Railway (Recommended)
1. Create account at [railway.app](https://railway.app)
2. Connect GitHub repository
3. Set environment variables:
```env
MONGODB_URI=your_mongodb_connection_string
JWT_SECRET=your_jwt_secret_key
CORS_ORIGINS=your_frontend_domains
PORT=8080
```
4. Deploy automatically

## 🧪 Testing

### Test Admin Login
```bash
curl -X POST http://localhost:8080/api/admin/auth/login \
  -H "Content-Type: application/json" \
  -d '{"usernameOrEmail":"admin","password":"admin123"}'
```

### Test Student Operations (use JWT token from login)
```bash
curl -X GET http://localhost:8080/api/students \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

## 📁 Project Structure
```
src/main/java/com/university/management/
├── controller/     # REST API endpoints
├── service/        # Business logic
├── entity/         # Database models
├── repository/     # Data access layer
├── security/       # JWT & Spring Security
├── dto/           # Data transfer objects
└── config/        # Application configuration
```

## 🔒 Security Features

- JWT token authentication
- BCrypt password encryption
- Role-based access control
- CORS configuration
- Input validation
- Secure error handling

## 🤝 Contributing

1. Fork the repository
2. Create feature branch
3. Commit changes
4. Submit pull request

## 📄 License

MIT License - see LICENSE file for details.

---

**🚀 Ready for production deployment!**
├── model/
│   └── Student.java                         # Student entity model
├── repository/
│   └── StudentRepository.java               # Student data repository
├── security/
│   ├── JwtUtil.java                         # JWT utility service
│   ├── JwtAuthenticationFilter.java         # JWT authentication filter
│   └── CustomUserDetailsService.java       # Custom user details service
└── service/
    ├── AuthService.java                     # Authentication service
    └── StudentService.java                  # Student service layer
```

## 🔧 Configuration

### Database Configuration
The application uses MongoDB Atlas. Update the connection string in `application.properties`:

```properties
spring.data.mongodb.uri=mongodb+srv://username:password@cluster.mongodb.net/database
spring.data.mongodb.database=university_management
```

### JWT Configuration
```properties
jwt.secret=Your_Secret_Key_Here
jwt.expiration=604800000  # 7 days in milliseconds
```

### Server Configuration
```properties
server.port=8080
server.servlet.context-path=/api
```

## 🚀 Getting Started

### Prerequisites
- Java 17 or higher
- Maven 3.6+
- MongoDB Atlas account (or local MongoDB instance)

### Installation & Setup

1. **Clone the repository**
```bash
git clone <repository-url>
cd university-springboot-backend
```

2. **Configure the database**
   - Update `application.properties` with your MongoDB connection string
   - Ensure your MongoDB cluster allows connections from your IP

3. **Build the application**
```bash
mvn clean package
```

4. **Run the application**
```bash
mvn spring-boot:run
```

The application will start on `http://localhost:8080`

## 📚 API Documentation

### Base URL
```
http://localhost:8080/api
```

### Authentication Endpoints

#### Login
```http
POST /auth/login
Content-Type: application/json

{
  "email": "student@example.com",
  "password": "password123"
}
```

#### Register
```http
POST /auth/register
Content-Type: application/json

{
  "fullName": "John Doe",
  "email": "john.doe@example.com",
  "password": "password123",
  "phoneNumber": "1234567890",
  "dateOfBirth": "2000-01-01",
  "gender": "Male",
  "address": "123 Main St, City"
}
```

#### Get Current User
```http
GET /auth/me
Authorization: Bearer <jwt_token>
```

### Student Management Endpoints

#### Get All Students
```http
GET /students?page=0&size=10&sortBy=name&sortDir=asc
Authorization: Bearer <jwt_token>
```

#### Get Student by ID
```http
GET /students/{id}
Authorization: Bearer <jwt_token>
```

#### Get Students by Department
```http
GET /students/department/{department}
Authorization: Bearer <jwt_token>
```

#### Update Student CGPA
```http
PUT /students/{id}/cgpa
Authorization: Bearer <jwt_token>
Content-Type: application/json

{
  "cgpa": 8.5,
  "credits": 120
}
```

#### Update Student Attendance
```http
PUT /students/{id}/attendance
Authorization: Bearer <jwt_token>
Content-Type: application/json

{
  "attendancePercentage": 85.5
}
```

#### Update Student Fee Information
```http
PUT /students/{id}/fee
Authorization: Bearer <jwt_token>
Content-Type: application/json

{
  "totalFee": 50000.0,
  "paidAmount": 30000.0
}
```

#### Update Student Placement
```http
PUT /students/{id}/placement
Authorization: Bearer <jwt_token>
Content-Type: application/json

{
  "placementStatus": "Placed",
  "company": "Tech Corp",
  "packageAmount": 600000.0
}
```

### Health Check
```http
GET /health
```

## 🔐 Security Features

### JWT Authentication
- **Token Generation**: Custom JWT tokens with student information
- **Token Validation**: Automatic validation on protected endpoints
- **Token Refresh**: Refresh expired tokens
- **Custom Claims**: Student ID, name, department included in tokens

### Password Security
- **BCrypt Encryption**: All passwords are encrypted using BCrypt
- **Password Change**: Secure password change with old password verification

### CORS Configuration
- Configurable allowed origins
- Support for multiple frontend domains
- Secure cookie and credential handling

## 📊 Database Schema

### Sample Test Data
```json
{
  "fullName": "Test Student",
  "email": "test@student.com",
  "password": "test123",
  "phoneNumber": "1234567890",
  "dateOfBirth": "2000-01-01",
  "gender": "Male",
  "address": "Test Address"
}
```





