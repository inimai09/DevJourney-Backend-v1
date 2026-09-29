[![CI](https://github.com/inimai09/DevJourney-Backend-v1/actions/workflows/ci.yml/badge.svg)](https://github.com/inimai09/DevJourney-Backend-v1/actions/workflows/ci.yml)


# DevJourney API

A secure REST API for managing personal journals. Users register, log in with JWT authentication, and manage their own journal entries. Each user can only see and change their own data.

**Tech:** Java 21 · Spring Boot · Spring Security · JWT · PostgreSQL · Spring Data JPA · Maven · Swagger/OpenAPI

---

## Features

- **Authentication:** register and log in, with JWT tokens and BCrypt password hashing
- **Authorization:** users can only access their own journals. Ownership comes from the authenticated user, never from a client-sent user ID
- **Journal CRUD:** create, read, update and delete entries
- **Pagination and sorting:** `GET /api/journals?page=0&size=10&sort=createdAt,desc`
- **Validation:** Bean Validation (`@NotBlank`, `@Email`, `@Size`) with clean error responses
- **Error handling:** global handler using `@ControllerAdvice`
- **API docs:** interactive Swagger UI
- **CI:** GitHub Actions builds and tests every push

---

## Getting Started

### Prerequisites

- Java 21
- PostgreSQL running locally

### 1. Clone the repo

```bash
git clone https://github.com/inimai09/DevJourney-Backend-v1.git
cd DevJourney-Backend-v1
```

### 2. Create the database

```sql
CREATE DATABASE developer_journal;
```

### 3. Configure (optional)

The app reads its secrets from environment variables and falls back to local dev defaults:

| Variable | Purpose | Default (dev only) |
|---|---|---|
| `DB_PASSWORD` | PostgreSQL password | `root` |
| `JWT_SECRET` | Key used to sign JWTs | dev key in `application.properties` |

For any real deployment, set your own values for both.

### 4. Run

```bash
./mvnw spring-boot:run
```

On Windows PowerShell:

```powershell
.\mvnw.cmd spring-boot:run
```

The API starts at `http://localhost:8080`.

---

## API Documentation

Once the app is running:

- Swagger UI: http://localhost:8080/swagger-ui/index.html
- OpenAPI spec: http://localhost:8080/v3/api-docs

---

## API Endpoints

### Authentication

| Method | Endpoint | Description |
|---|---|---|
| POST | `/users` | Register a new user |
| POST | `/users/login` | Log in and receive a JWT |

### Users

| Method | Endpoint | Description |
|---|---|---|
| GET | `/users` | Get all users |
| GET | `/users/{id}` | Get a user by ID |

### Journals (requires JWT)

| Method | Endpoint | Description |
|---|---|---|
| GET | `/api/journals` | List your journals (paginated, sortable) |
| GET | `/api/journals/{id}` | Get one journal |
| POST | `/api/journals` | Create a journal |
| PUT | `/api/journals/{id}` | Update a journal |
| DELETE | `/api/journals/{id}` | Delete a journal |

Send the token on protected routes:

```
Authorization: Bearer <your-token>
```

### Examples

**Login response**

```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9...",
  "message": "Login successful"
}
```

**Create journal request**

```json
{
  "title": "My first journal",
  "content": "Today I worked on my Spring Boot project."
}
```

**Pagination parameters** (defaults: `page=0`, `size=10`, `sort=createdAt,desc`)

| Parameter | Meaning |
|---|---|
| `page` | Page number, starting at 0 |
| `size` | Journals per page |
| `sort` | Field and direction, e.g. `createdAt,desc` |

---

## Architecture

Layered design:

```
Controller → Service → Repository → PostgreSQL
```

Request security flow:

```
Client → JWT → JWT Filter → SecurityContext → Controller → Service → Repository → PostgreSQL
```

---

## Testing and CI

Run the tests locally:

```bash
./mvnw verify
```

Tests use an in-memory **H2** database (configured in `src/test/resources/application.properties`), so they don't need PostgreSQL.

Every push triggers the GitHub Actions workflow in `.github/workflows/ci.yml`, which sets up Java 21 and runs `mvn verify`.

---

## Author

**Inimai S** · [GitHub](https://github.com/inimai09)
