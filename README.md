# Task Manager REST API

A secure, stateless RESTful API for managing tasks, built with Spring Boot, Spring Security, and JWT authentication. Each user can only access and manage their own tasks, with centralized exception handling and request validation baked in.

## Features

- 🔐 **JWT-based Authentication** — Stateless login/auth flow using a custom `OncePerRequestFilter`
- 👤 **User Data Isolation** — Every user can only view, update, or delete their own tasks
- ✅ **Request Validation** — Jakarta Bean Validation on all incoming DTOs
- ⚠️ **Centralized Exception Handling** — Consistent error responses via `@RestControllerAdvice`
- 🗄️ **Persistence** — Spring Data JPA + Hibernate + MySQL
- 📦 **Layered Architecture** — Clean separation of Controller, Service, Repository, and DTO layers

## Tech Stack

| Layer          | Technology                          |
|----------------|--------------------------------------|
| Language       | Java                                 |
| Framework      | Spring Boot                          |
| Security       | Spring Security, JWT (JSON Web Token)|
| Persistence    | Spring Data JPA, Hibernate           |
| Database       | MySQL                                |
| Validation     | Jakarta Bean Validation              |
| Build Tool     | Maven                                |

## Architecture

```
Client
  │
  ▼
JWT Filter (OncePerRequestFilter) → validates token, sets SecurityContext
  │
  ▼
Controller Layer → handles HTTP requests/responses
  │
  ▼
Service Layer → business logic, user data isolation
  │
  ▼
Repository Layer (Spring Data JPA) → database access
  │
  ▼
MySQL Database
```

Exceptions thrown at any layer are caught centrally by a `@RestControllerAdvice` class and converted into consistent, structured error responses.

## API Endpoints

| Method | Endpoint            | Description                     | Auth Required |
|--------|----------------------|----------------------------------|----------------|
| POST   | `/api/auth/register`| Register a new user             | No             |
| POST   | `/api/auth/login`   | Authenticate and receive a JWT  | No             |
| GET    | `/api/tasks`         | Get all tasks for the logged-in user | Yes       |
| GET    | `/api/tasks/{id}`    | Get a single task by ID         | Yes            |
| POST   | `/api/tasks`         | Create a new task               | Yes            |
| PUT    | `/api/tasks/{id}`    | Update an existing task         | Yes            |
| DELETE | `/api/tasks/{id}`    | Delete a task                   | Yes            |

> Update this table to match your actual endpoints, request/response bodies, and any query parameters you support (e.g. filtering by status, pagination).

## Authentication Flow

1. User registers or logs in via `/api/auth/register` or `/api/auth/login`.
2. On successful login, the server returns a signed JWT.
3. The client includes this token in the `Authorization` header on every subsequent request:
   ```
   Authorization: Bearer <your_jwt_token>
   ```
4. The custom JWT filter validates the token's signature and expiry on each request, then sets the authenticated user in Spring Security's `SecurityContextHolder`.
5. Service-layer methods use the authenticated user's ID to ensure users can only access their own tasks.

## Getting Started

### Prerequisites

- Java 17+
- Maven 3.6+
- MySQL 8+

### 1. Clone the repository

```bash
git clone https://github.com/SuyashSrivastava4TheW/task-manager-rest-api.git
cd task-manager-rest-api
```

### 2. Configure the database

Create a MySQL database and update `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/task_manager_db
spring.datasource.username=your_mysql_username
spring.datasource.password=your_mysql_password

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

jwt.secret=your_jwt_secret_key
jwt.expiration=86400000
```

### 3. Build and run

```bash
mvn clean install
mvn spring-boot:run
```

The API will be available at `http://localhost:8080`.

## Example Request

**Register:**
```bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{"username": "john_doe", "password": "securePass123"}'
```

**Create a task (authenticated):**
```bash
curl -X POST http://localhost:8080/api/tasks \
  -H "Authorization: Bearer <your_jwt_token>" \
  -H "Content-Type: application/json" \
  -d '{"title": "Finish README", "description": "Add setup instructions", "status": "IN_PROGRESS"}'
```

## Error Handling

All errors are returned in a consistent JSON structure via a global exception handler, for example:

```json
{
  "timestamp": "2026-08-22T10:15:30",
  "status": 400,
  "error": "Bad Request",
  "message": "Title must not be blank"
}
```

## Future Improvements

- Add refresh token support
- Add pagination and filtering for task listing
- Add role-based access control (admin vs. regular user)
- Add unit and integration tests
- Dockerize the application

## Author

**Suyash Srivastava**
- GitHub: [@SuyashSrivastava4TheW](https://github.com/SuyashSrivastava4TheW)
- LinkedIn: [Suyash Srivastava](https://www.linkedin.com/in/suyash-srivastava-4tw/)
- Email: suyashsrivastava063@gmail.com
