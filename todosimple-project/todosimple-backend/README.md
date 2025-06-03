# ToDoSimple – Backend 🔧

This is the backend of the ToDoSimple application. Built using **Spring Boot**, it provides a secure and robust REST API for task and user management, with authentication powered by JWT.

## 🧩 Features

- ✅ User registration and login  
- ✅ JWT-based stateless authentication  
- ✅ Role-based access control (Admin, User)  
- ✅ Task CRUD operations per authenticated user  
- ✅ Global exception handling with custom error responses  
- ✅ DTOs for input validation and clean separation  
- ✅ Environment-based configuration profiles (dev/prod)  
- ✅ Dockerized application for deployment

## 🚀 Technologies

- Java 17  
- Spring Boot  
- Spring Security + JWT  
- Maven  
- MySQL  
- Docker  
- Lombok

## 📂 Folder Structure

todosimple-backend/
├── configs/ # Web & Security configurations
├── controllers/ # REST API controllers
├── services/ # Business logic
├── repositories/ # JPA repositories
├── models/ # Entities, DTOs, Enums, Projections
├── exceptions/ # Global exception handler and response
├── security/ # JWT filters, utilities and auth classes
├── resources/ # Application properties per environment
└── test/ # Unit & integration tests (1 placeholder)


## ⚙️ Running Locally

```bash
# Set environment variables or use .env file
MYSQL_USER=youruser
MYSQL_PASSWORD=yourpass

# Start MySQL and Spring Boot
docker-compose up --build

If running locally without Docker:

Make sure MySQL is running and configured in application.properties

Use mvn spring-boot:run to start

API Endpoints
POST /login – Authenticate and receive JWT

POST /users – Register new user

GET /tasks – List tasks (authenticated)

POST /tasks – Create new task

PUT /tasks/{id} – Update task

DELETE /tasks/{id} – Delete task

Protected routes require Authorization: Bearer <token> header.

Technical Notes
Uses BCrypt for password encryption

UserDetailsService is used to load users during authentication

Exception handling with @ControllerAdvice for clean API feedback

Separation of concerns respected via layered architecture

Goal Alignment
This backend demonstrates production-grade RESTful API design using Spring Boot and JWT security, proving readiness for remote backend or full-stack roles in international environments.