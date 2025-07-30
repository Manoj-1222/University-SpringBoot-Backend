# University Management System - Spring Boot Backend

A comprehensive university management system backend built with Java Spring Boot, providing RESTful APIs for student management, authentication, and administrative functions.

## 🚀 Features

### Core Features
- **Student Management**: CRUD operations for student records
- **JWT Authentication**: Secure login/logout with token-based authentication
- **Role-based Access Control**: Admin and student role management
- **Academic Information Management**: CGPA, credits, attendance tracking
- **Fee Management**: Track fee payments and pending amounts
- **Placement Tracking**: Monitor student placement status and company details

### Technical Features
- **Spring Boot 3.2.0**: Latest Spring Boot framework
- **Spring Security 6**: Advanced security configuration
- **MongoDB Integration**: NoSQL database with Spring Data MongoDB
- **JWT Tokens**: Secure authentication with custom claims
- **Input Validation**: Jakarta Bean Validation for request validation
- **CORS Support**: Configurable cross-origin resource sharing
- **Error Handling**: Comprehensive error responses with ApiResponse wrapper
- **Pagination Support**: Efficient data retrieval with Spring Data pagination

## 🛠 Tech Stack

- **Framework**: Spring Boot 3.2.0
- **Language**: Java 17+
- **Database**: MongoDB Atlas
- **Security**: Spring Security 6 + JWT
- **Build Tool**: Maven
- **Validation**: Jakarta Bean Validation
- **Documentation**: Built-in Swagger/OpenAPI support

## 📦 Project Structure

```
src/main/java/com/university/management/
├── UniversityManagementApplication.java     # Main application class
├── config/
│   └── SecurityConfig.java                  # Security configuration
├── controller/
│   ├── AuthController.java                  # Authentication endpoints
│   ├── StudentController.java               # Student management endpoints
│   └── HealthController.java                # Health check endpoint
├── dto/
│   ├── request/
│   │   ├── LoginRequest.java                # Login request DTO
│   │   └── StudentRegistrationRequest.java  # Registration request DTO
│   └── response/
│       ├── ApiResponse.java                 # Generic API response wrapper
│       ├── AuthResponse.java                # Authentication response DTO
│       └── StudentResponse.java             # Student response DTO
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

### Student Collection
```javascript
{
  "_id": "ObjectId",
  "name": "String",
  "rollNo": "String (unique)",
  "email": "String (unique)",
  "password": "String (encrypted)",
  "department": "String",
  "year": "Integer",
  "semester": "Integer",
  "phone": "String",
  "dateOfBirth": "Date",
  "bloodGroup": "String",
  "currentCGPA": "Double",
  "totalCredits": "Integer",
  "attendance": {
    "percentage": "Double"
  },
  "totalFee": "Double",
  "paidAmount": "Double",
  "placementStatus": "String",
  "company": "String",
  "packageAmount": "Double",
  "createdAt": "DateTime",
  "updatedAt": "DateTime"
}
```

## 🧪 Testing

### Manual Testing
1. Start the application
2. Use Postman or curl to test endpoints
3. Check the health endpoint: `GET /health`
4. Test authentication flow: register → login → access protected endpoints

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

## 🚀 Deployment

### Local Development
```bash
mvn spring-boot:run
```

### Production Build
```bash
mvn clean package
java -jar target/university-management-0.0.1-SNAPSHOT.jar
```

### Environment Variables
Set the following environment variables for production:
- `MONGODB_URI`: MongoDB connection string
- `JWT_SECRET`: JWT secret key
- `JWT_EXPIRATION`: Token expiration time
- `CORS_ALLOWED_ORIGINS`: Allowed CORS origins

## 📈 Performance Features

- **Database Indexing**: Indexed fields for fast queries (email, rollNo)
- **Pagination**: Efficient data retrieval with Spring Data pagination
- **Connection Pooling**: MongoDB connection pooling for scalability
- **Lazy Loading**: Optimized data loading strategies

## 🔄 Integration

This Spring Boot backend can be integrated with:
- **React Frontend**: CORS configured for React development servers
- **Node.js Backend**: Can run alongside existing Node.js backend
- **Mobile Apps**: RESTful APIs suitable for mobile app integration
- **Third-party Services**: Extensible architecture for external integrations

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## 📄 License

This project is licensed under the MIT License - see the LICENSE file for details.

## 👥 Team

Developed as part of the University Management System project by the development team.

## 📞 Support

For support and queries, please contact the development team or create an issue in the repository.

---

**Note**: This Spring Boot backend serves as an alternative/complementary implementation to the existing Node.js backend, providing enterprise-grade features and Spring ecosystem benefits.
