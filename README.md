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
- 9 Unit Tests and 7 Integration Tests
- PostgreSQL persistence with versioned schema migrations via Flyway
- Swagger UI for interactive API documentation
- Docker container for DB

## Database Initialization

This application uses PostgreSQL with Flyway-managed migrations.

On startup:
- Flyway applies SQL migrations from `src/main/resources/db/migration`
- Hibernate validates entity mappings against the migrated schema (`ddl-auto=validate`)

## Tech Stack

- **Java 25**
- **Spring Boot 4.0.5**
- **Spring Web**
- **Spring Data JPA**
- **PostgreSQL**
- **Flyway**
- **Lombok**
- **Maven**
- **Jakarta Validation**
- **Docker**

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
- PostgreSQL 15+ running locally
- Docker

### Setting and configuring the environment variables
Copy the `.env.example` to `.env` and edit.
```.properties
POSTGRES_PORT=5432      #Set to free port for listening
POSTGRES_DB=task_manager      #Set to exisiting data base name
POSTGRES_USER=postgres      #Set to postgres username
POSTGRES_PASSWORD=postgres      #Set to postgres username password
```

Export environment variables to Spring Application.

### Running the Application

```bash
# 1) Run the postgres docker container
docker-compose up -d

# 2) Verify it's running
docker ps

# 3) Build and run the application
./mvnw clean install
./mvnw spring-boot:run
```

The application will start on `http://localhost:8080`.

Database connection is configured in `src/main/resources/application.properties`:

If Flyway reports `Found non-empty schema(s) "public" but no schema history table`, use a clean schema in dev:

```sql
DROP SCHEMA public CASCADE;
CREATE SCHEMA public;
GRANT ALL ON SCHEMA public TO va1err;
GRANT ALL ON SCHEMA public TO public;
```

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
└── exceptions/      # Custom exception handlers
```

## License

This project is open source.
