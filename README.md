# Task Manager

A simple RESTful Task Manager application built with **Spring Boot** and **Java 25**.

## Features

- Create, read, update, and delete tasks
- Clean architecture with DTO layer
- Task status tracking (TODO, IN_PROGRESS, DONE)
- Pagination and sorting support
- Filtering by task status
- Unified API response structure
- Global exception handling with consistent error format
- In-memory H2 database with preloaded schema and sample data for easy testing
- Swagger UI for interactive API documentation

## Database Initialization

This application uses in-memory H2 database with automatic schema generation and data seeding. \
On startup:
- Hibernate creates the database schema
- `data.sql` is executed to preload sample tasks

## Tech Stack

- **Java 25**
- **Spring Boot 4.0.5**
- **Spring Web**
- **Spring Data JPA**
- **H2 Database**
- **Lombok**
- **Maven**
- **Jakarta Validation**

## Architecture

1. **Client** → Makes requests
2. **Controller** → Handles HTTP requests/responses
3. **DTO** → Data Transfer Object
4. **Service** → Business logic layer
5. **Entity** → Domain model
6. **Repository** → Data access layer
7. **Database** → Persistence storage

## API design

### Success response

```json
{
  "status": 200,
  "success": true,
  "message": "<Relevant Message>",
  "data": ["<Relevant data>"],
  "timestamp": "<Timestamp>"
}
```

### Error response
```json
  "status": "<Relevant status code>",
  "success": false,
  "message": "<Relevant Message>",
  "details": ["<Relevant information about an error>"],
  "timestamp": "<Timestamp>"
```

## API documentation

Swagger UI is available for testing and exploring endpoints: \
http://localhost:8080/swagger-ui/index.html

## Getting Started

### Prerequisites

- Java 25 or higher
- Maven

### Running the Application

```bash
# Build the project
./mvnw clean install

# Run the application
./mvnw spring-boot:run
```

The application will start on `http://localhost:8080`.

### H2 Console

Access the H2 database console at `http://localhost:8080/h2-console` with the following settings:

- **JDBC URL:** `jdbc:h2:mem:testdb`
- **Username:** `sa`
- **Password:** (leave blank)

## API Endpoints


| Method | Endpoint      | Description       |
| ------ | ------------- | ----------------- |
| POST   | `/tasks`      | Create a new task |
| GET    | `/tasks`      | Get all tasks     |
| GET    | `/tasks/{id}` | Get task by ID    |
| PUT    | `/tasks/{id}` | Update a task     |
| DELETE | `/tasks/{id}` | Delete a task     |

### Example: Create a Task

```bash
curl -X POST http://localhost:8080/tasks \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Complete project",
    "description": "Finish the Task Manager project"
  }'
```

## Project Structure

```
src/main/java/com/va1err/TaskManager/
├── controllers/     # REST controllers
├── services/        # Business logic
├── repositories/    # Data access layer
├── models/          # JPA entities
├── dto/             # Data transfer objects
├── enums/           # Enumerations
├── exceptions/      # Custom exception handlers
└── config/          # Application configuration
```

## License

This project is open source.
