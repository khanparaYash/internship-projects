# Employee Management REST API

A small Spring Boot service providing CRUD and search functionality for employee records.

- Language: Java 25
- Frameworks: Spring Boot, Spring Data JPA, Bean Validation
- Database: MySQL

---

## Quick start (development)

1. Clone the repository and open it in your IDE.
2. Create a MySQL database (example):

   ```sql
   CREATE DATABASE employee_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
   ```

3. Configure database credentials in `src/main/resources/application.properties`.

4. Run with Maven wrapper:

   Windows:
   ```powershell
   .\mvnw spring-boot:run
   ```

   macOS/Linux:
   ```bash
   ./mvnw spring-boot:run
   ```

   The app will start at: http://localhost:8000/api

---

## API Summary

Base URL: http://localhost:8000/api

Endpoints:

| Method | Path | Description |
|--------|------|-------------|
| POST | /employees | Create an employee |
| GET | /employees/{id} | Get employee by id |
| GET | /employees | List employees (paginated: page, size) |
| GET | /employees/search | Search employees by name and/or department |
| PUT | /employees/{id} | Update an employee |
| DELETE | /employees/{id} | Delete an employee |

Notes:
- Search semantics: when both `name` and `department` are provided, both must match (logical AND). `name` is partial, case-insensitive match against first or last name; `department` matches case-insensitively.
- Pagination for listing uses `page` (0-based) and `size` query params.

---

## Request / Response Examples

Create employee (curl):

```bash
curl -X POST "http://localhost:8000/api/employees" \
  -H "Content-Type: application/json" \
  -d '{
    "firstName": "Yash",
    "lastName": "Khanpara",
    "email": "yashkhanpara11@gmail.com",
    "phone": "8849930662",
    "department": "Engineering",
    "salary": 50000.0,
    "joiningDate": "2026-06-15"
  }'
```

Response (201 Created):

```json
{
  "id": 1,
  "firstName": "Yash",
  "lastName": "Khanpara",
  "email": "yashkhanpara11@gmail.com",
  "phone": "8849930662",
  "department": "Engineering",
  "salary": 50000.0,
  "joiningDate": "2026-06-15"
}
```

List employees (curl):

```bash
curl "http://localhost:8000/api/employees?page=0&size=10"
```

Response (200 OK):

```json
{
  "content": [ " Employee DTO objects " ],
  "page": 0,
  "size": 10,
  "totalElements": 42,
  "totalPages": 5
}
```

Search (curl):

```bash
curl "http://localhost:8000/api/employees/search?name=Yash&department=Engineering"
```

Response (200 OK):

```json
[
  {
    "id": 1,
    "firstName": "Yash",
    "lastName": "Khanpara",
    "email": "yashkhanpara11@gmail.com",
    "phone": "8849930662",
    "department": "Engineering",
    "salary": 50000.0,
    "joiningDate": "2026-06-15"
  }
]
```

---

## Data model & ER diagram

Entities:

- Employee
  - id (PK)
  - firstName
  - lastName
  - email (unique)
  - phone (unique)
  - department
  - salary
  - joiningDate

ER diagram (placeholder):

![ER Diagram](./er-diagram.svg)

To generate the ER diagram quickly:
1. Open MySQL Workbench and connect to `employee_db` → Database → Reverse Engineer.
2. Export PNG and save to `docs/er-diagram.png`.

Alternatively, use dbdiagram.io or draw.io and export a PNG.

---

## Validation & Error handling

- Requests are validated using javax validation annotations on DTO fields (e.g. @NotBlank, @Email, @Pattern).
- Errors return a structured JSON payload with fields: timestamp, status, error, message, path, fieldErrors.

Example validation error (400 Bad Request):

```json
{
  "timestamp": "2026-09-15T09:00:00Z",
  "status": 400,
  "error": "Bad Request",
  "message": "Validation failed",
  "path": "/api/employees",
  "fieldErrors": {
    "email": "Invalid email"
  }
}
```

---

## Development notes

- Hibernate DDL auto is set to `update` for convenience. Use Flyway/Liquibase for production.
- OpenAPI/Swagger UI: http://localhost:8000/api/swagger-ui.html (or `/api/swagger-ui/index.html`).

---

## Running tests

```bash
./mvnw test
```

---

If you'd like, I can generate the ER diagram image from the JPA entities and add it to `docs/er-diagram.png`. Let me know.