# User Management System API

A comprehensive RESTful API for user management built with **Spring Boot**. This system provides enterprise-level user management with authentication, authorization, role-based access control, and automated testing with GitHub Actions.

## Features

- **Complete User CRUD Operations** - Create, read, update, and delete users
- **Authentication & Authorization** - Spring Security with role-based access
- **Role Management** - ADMIN, MANAGER, USER, GUEST roles
- **User Status Tracking** - ACTIVE, INACTIVE, SUSPENDED, PENDING_VERIFICATION
- **Advanced Search & Filtering** - Search by name, filter by role/status
- **Pagination Support** - Efficient data retrieval for large datasets
- **User Statistics** - Comprehensive user analytics
- **PostgreSQL Database** - Production-ready database
- **Automated Testing** - GitHub Actions with external test repository
- **Security Best Practices** - Password encryption, method-level security
- **API Documentation** - Comprehensive endpoint documentation

## Technologies Used

- **Java 17** - Latest LTS version
- **Spring Boot 3.2.0** - Latest Spring Boot
- **Spring Security** - Authentication & Authorization
- **Spring Data JPA** - Database operations
- **PostgreSQL** - Production database
- **JWT** - Token-based authentication
- **BCrypt** - Password encryption
- **Docker** - Database containerization
- **GitHub Actions** - CI/CD pipeline
- **Maven** - Dependency management

## Project Architecture

```
src/
├── main/
│   ├── java/com/usermanagement/
│   │   ├── UserManagementSystemApplication.java  # Main application
│   │   ├── entity/
│   │   │   └── User.java                          # User entity with security
│   │   ├── repository/
│   │   │   └── UserRepository.java                # Advanced queries
│   │   ├── service/
│   │   │   └── UserService.java                   # Business logic
│   │   ├── controller/
│   │   │   └── UserController.java                # REST endpoints
│   │   ├── dto/
│   │   │   ├── UserDto.java                       # Data transfer object
│   │   │   └── CreateUserRequest.java             # Creation request DTO
│   │   └── config/
│   │       └── SecurityConfig.java                # Security configuration
│   └── resources/
│       └── application.properties                 # Configuration
├── .github/
│   └── workflows/
│       └── pr-tests.yml                          # GitHub Actions workflow
├── docker-compose.yml                            # PostgreSQL setup
├── init.sql                                      # Database initialization
└── README.md                                     # This file
```

## Quick Start

### Prerequisites

- **Java 17** or higher
- **Maven 3.6** or higher
- **Docker & Docker Compose**
- **Git**

### 1. Clone the Repository

```bash
git clone <your-repository-url>
cd user-management-system
```

### 2. Start PostgreSQL Database

```bash
# Start PostgreSQL
docker-compose up -d postgres

# Verify database is running
docker ps

# Optional: Start pgAdmin for database management
docker-compose up -d pgadmin
```

**Database Access:**
- **Host:** localhost:5432
- **Database:** user_management_db
- **Username:** user_admin
- **Password:** admin123

**pgAdmin Access:**
- **URL:** http://localhost:5050
- **Email:** admin@usermanagement.com
- **Password:** admin123

### 3. Build and Run the Application

```bash
# Install dependencies
mvn clean install

# Run the application
mvn spring-boot:run
```

### 4. Verify Application is Running

```bash
# Health check
curl http://localhost:8080/actuator/health

# Expected response:
{"status":"UP"}
```

## API Documentation

### Base URL
```
http://localhost:8080/api/users
```

### Authentication
The API uses HTTP Basic Authentication for testing. In production, implement JWT tokens.

**Default Credentials:**
- **Username:** admin
- **Password:** admin123

### Role-Based Access Control

| Role | Permissions |
|------|-------------|
| **ADMIN** | Full access to all operations, user management, system configuration |
| **MANAGER** | User management, view operations, user status changes |
| **USER** | View and update own profile only |
| **GUEST** | Read-only access to own profile |

### API Endpoints

#### User Management

| Method | Endpoint | Description | Required Role |
|--------|----------|-------------|---------------|
| `GET` | `/api/users` | Get all users (paginated) | ADMIN, MANAGER |
| `GET` | `/api/users/all` | Get all users (no pagination) | ADMIN |
| `GET` | `/api/users/{id}` | Get user by ID | ADMIN, MANAGER, Own User |
| `GET` | `/api/users/username/{username}` | Get user by username | ADMIN, MANAGER |
| `GET` | `/api/users/email/{email}` | Get user by email | ADMIN, MANAGER |
| `POST` | `/api/users` | Create new user | ADMIN, MANAGER |
| `PUT` | `/api/users/{id}` | Update user | ADMIN, MANAGER, Own User |
| `DELETE` | `/api/users/{id}` | Delete user | ADMIN |

#### Filtering & Search

| Method | Endpoint | Description | Required Role |
|--------|----------|-------------|---------------|
| `GET` | `/api/users/role/{role}` | Get users by role | ADMIN, MANAGER |
| `GET` | `/api/users/status/{status}` | Get users by status | ADMIN, MANAGER |
| `GET` | `/api/users/search?name={name}` | Search users by name | ADMIN, MANAGER |

#### User Status Management

| Method | Endpoint | Description | Required Role |
|--------|----------|-------------|---------------|
| `PATCH` | `/api/users/{id}/activate` | Activate user | ADMIN, MANAGER |
| `PATCH` | `/api/users/{id}/deactivate` | Deactivate user | ADMIN, MANAGER |
| `PATCH` | `/api/users/{id}/suspend` | Suspend user | ADMIN |
| `PATCH` | `/api/users/{id}/role/{role}` | Update user role | ADMIN |

#### Statistics

| Method | Endpoint | Description | Required Role |
|--------|----------|-------------|---------------|
| `GET` | `/api/users/statistics` | Get user statistics | ADMIN, MANAGER |

### Request/Response Examples

#### Create User
```bash
curl -X POST http://localhost:8080/api/users \
  -H "Content-Type: application/json" \
  -H "Authorization: Basic YWRtaW46YWRtaW4xMjM=" \
  -d '{
    "username": "john_doe",
    "email": "john@example.com",
    "password": "password123",
    "firstName": "John",
    "lastName": "Doe",
    "phoneNumber": "+1234567890",
    "role": "USER"
  }'
```

#### Get Users with Pagination
```bash
curl -X GET "http://localhost:8080/api/users?page=0&size=10&sortBy=createdAt&sortDir=desc" \
  -H "Authorization: Basic YWRtaW46YWRtaW4xMjM="
```

#### Search Users
```bash
curl -X GET "http://localhost:8080/api/users/search?name=john" \
  -H "Authorization: Basic YWRtaW46YWRtaW4xMjM="
```

#### Update User Role
```bash
curl -X PATCH http://localhost:8080/api/users/1/role/MANAGER \
  -H "Authorization: Basic YWRtaW46YWRtaW4xMjM="
```

### Sample Response Formats

#### User Object
```json
{
  "id": 1,
  "username": "john_doe",
  "email": "john@example.com",
  "firstName": "John",
  "lastName": "Doe",
  "phoneNumber": "+1234567890",
  "role": "USER",
  "status": "ACTIVE",
  "createdAt": "2024-01-15T10:30:00",
  "updatedAt": "2024-01-15T10:30:00",
  "lastLogin": null
}
```

#### Paginated Response
```json
{
  "content": [/* user objects */],
  "pageable": {
    "sort": {"sorted": true, "direction": "DESC"},
    "pageNumber": 0,
    "pageSize": 10
  },
  "totalElements": 25,
  "totalPages": 3,
  "first": true,
  "last": false,
  "size": 10,
  "numberOfElements": 10
}
```

#### Statistics Response
```json
{
  "totalUsers": 100,
  "activeUsers": 85,
  "inactiveUsers": 10,
  "suspendedUsers": 5,
  "adminUsers": 2,
  "managerUsers": 8,
  "regularUsers": 90,
  "activeUserPercentage": 85.0
}
```

## GitHub Actions Automated Testing

This repository includes a sophisticated GitHub Actions workflow that automatically runs backend tests from an external repository when pull requests are created.

### Workflow Triggers

The automation triggers when:
- **Pull Request Created** to `main` branch
- **Pull Request Updated** (new commits pushed)
- **Pull Request Reopened**

### Workflow Process

1. **Environment Setup**
   - Ubuntu latest runner
   - PostgreSQL 15 test database
   - Java 17 (Temurin distribution)
   - Maven dependency caching

2. **Application Deployment**
   - Builds User Management System
   - Starts application with test database
   - Waits for health check confirmation
   - Configures test environment variables

3. **External Test Execution**
   - Clones your backend test repository
   - Installs test dependencies
   - Runs comprehensive test suite
   - Supports both Maven and Gradle projects

4. **Results & Reporting**
   - Uploads detailed test results
   - Publishes JUnit test reports
   - Comments on PR with success/failure status
   - Provides links to detailed logs

### Configuration Requirements

To use this automation, ensure your test repository has:

1. **Repository Access**: The workflow needs access to your test repository
2. **Test Configuration**: Environment variables for base URL and credentials
3. **Build System**: Either Maven (`pom.xml`) or Gradle (`build.gradle`)
4. **Test Structure**: Integration tests that can run against the live API

### Customization

Edit `.github/workflows/pr-tests.yml` to:
- Change test repository name
- Modify environment variables
- Add additional test steps
- Configure different database settings

## Docker Commands

```bash
# Start all services
docker-compose up -d

# Start only PostgreSQL
docker-compose up -d postgres

# View logs
docker-compose logs postgres
docker-compose logs pgadmin

# Stop all services
docker-compose down

# Reset database (removes all data)
docker-compose down -v
docker-compose up -d postgres
```

## Configuration

### Environment Variables

You can override default configurations using environment variables:

```bash
# Database Configuration
export SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/custom_db
export SPRING_DATASOURCE_USERNAME=custom_user
export SPRING_DATASOURCE_PASSWORD=custom_password

# Security Configuration
export APP_JWT_SECRET=your-secret-key-here
export APP_JWT_EXPIRATION=86400000

# Application Configuration
export SERVER_PORT=8081
```

### Production Configuration

For production deployment:

```properties
# Use 'validate' instead of 'update' for production
spring.jpa.hibernate.ddl-auto=validate

# Disable SQL logging
spring.jpa.show-sql=false

# Configure proper connection pool
spring.datasource.hikari.maximum-pool-size=20

# Enable production security
spring.profiles.active=production
```

## Testing

### Running Unit Tests

```bash
# Run all tests
mvn test

# Run specific test class
mvn test -Dtest=UserServiceTest

# Run tests with coverage
mvn test jacoco:report
```

### Integration Testing

The GitHub Actions workflow automatically runs integration tests from your external test repository. To test manually:

1. Start the application
2. Clone your test repository
3. Configure test environment variables
4. Run integration tests against `http://localhost:8080`

## Security Features

- **Password Encryption**: BCrypt with salt
- **Role-Based Access Control**: Method-level security
- **SQL Injection Prevention**: JPA parameterized queries
- **Input Validation**: Bean validation with custom messages
- **CORS Configuration**: Configurable cross-origin requests
- **Session Management**: Stateless (suitable for microservices)

## Monitoring & Health Checks

### Actuator Endpoints

| Endpoint | Description |
|----------|-------------|
| `/actuator/health` | Application health status |
| `/actuator/info` | Application information |
| `/actuator/metrics` | Application metrics |

### Database Monitoring

Use pgAdmin to monitor:
- Active connections
- Query performance
- Database size
- Index usage

## Next Steps & Enhancements

Consider implementing these additional features:

1. **Authentication Improvements**
   - JWT token-based authentication
   - Refresh token mechanism
   - OAuth2 integration

2. **Advanced Features**
   - Email verification system
   - Password reset functionality
   - User profile pictures
   - Audit logging

3. **Performance Optimizations**
   - Redis caching
   - Database connection pooling
   - Query optimization

4. **DevOps Enhancements**
   - Kubernetes deployment
   - Docker multi-stage builds
   - Monitoring with Prometheus
   - Centralized logging

5. **API Improvements**
   - OpenAPI/Swagger documentation
   - API versioning
   - Rate limiting
   - Response compression

## Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

The GitHub Actions workflow will automatically run tests on your PR!

## Additional Resources

- [Spring Boot Documentation](https://spring.io/projects/spring-boot)
- [Spring Security Guide](https://spring.io/guides/gs/securing-web/)
- [Spring Data JPA Reference](https://docs.spring.io/spring-data/jpa/docs/current/reference/html/)
- [PostgreSQL Documentation](https://www.postgresql.org/docs/)
- [GitHub Actions Documentation](https://docs.github.com/en/actions)

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

For questions or support, please open an issue in the repository.
