# Habit Tracker API

A REST API for tracking daily habits with streak functionality.
Users register, log in, create habits, mark them as completed, and track consecutive day streaks.

**Live Demo:** https://habit-tracker-api-wisb.onrender.com/swagger-ui/index.html
> Hosted on Render free tier — first request may take ~30 sec to wake the server.

---

## What It Does

Building consistent habits requires tracking — this API provides a backend for habit management apps:

- **Register and authenticate** users securely with JWT tokens
- **Create and manage** personal habits
- **Track completions** — mark a habit as done for today
- **Streak counting** — automatically counts consecutive days of completion
- **Secure endpoints** — all habit operations require a valid JWT token

---

## Tech Stack

![Java](https://img.shields.io/badge/Java-17-orange)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.x-green)
![Spring Security](https://img.shields.io/badge/Spring%20Security-JWT-brightgreen)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-17-blue)
![Docker](https://img.shields.io/badge/Docker-ready-blue)

- Java 17
- Spring Boot + Spring MVC
- Spring Security + JWT
- Spring Data JPA / Hibernate
- PostgreSQL
- Lombok
- SpringDoc OpenAPI (Swagger)
- JUnit 5 + Mockito
- Docker + docker-compose

---

## API Endpoints

### Auth

| Method | Endpoint | Description |
|--------|----------|-------------|
| `POST` | `/api/v1/auth/register` | Register new user |
| `POST` | `/api/v1/auth/login` | Login, returns JWT token |

### Habits (require Authorization header)

| Method | Endpoint | Description |
|--------|----------|-------------|
| `GET` | `/api/v1/habits` | Get all habits |
| `GET` | `/api/v1/habits/{id}` | Get habit by ID |
| `POST` | `/api/v1/habits` | Create habit |
| `PUT` | `/api/v1/habits/{id}` | Update habit |
| `DELETE` | `/api/v1/habits/{id}` | Delete habit |
| `POST` | `/api/v1/habits/{id}/complete` | Mark as completed today |
| `GET` | `/api/v1/habits/{id}/streak` | Get current streak count |

### Example Flow

**1. Register:**
```json
POST /api/v1/auth/register
{
  "email": "user@example.com",
  "password": "secret123"
}
```

**2. Login → get token:**
```json
POST /api/v1/auth/login
{
  "email": "user@example.com",
  "password": "secret123"
}

Response:
{
  "token": "eyJhbGciOiJIUzI1NiJ9..."
}
```

**3. Use token for habits:**
```
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9...
```

---

## Project Structure

```
src/main/java/com/habittracker/
├── controller/
│   ├── AuthController.java         # Registration and login
│   └── HabitController.java        # Habit CRUD and completion
├── service/
│   ├── AuthService.java            # Auth business logic
│   └── HabitService.java           # Habit business logic + streak
├── security/
│   ├── SecurityConfig.java         # Spring Security configuration
│   ├── JwtAuthFilter.java          # JWT filter for every request
│   └── JwtService.java             # Token generation and validation
├── entity/
│   ├── User.java                   # User JPA entity
│   └── Habit.java                  # Habit JPA entity
├── dto/
│   ├── RegisterRequest.java
│   ├── LoginRequest.java
│   ├── HabitRequest.java
│   └── HabitResponse.java
├── repository/
│   ├── UserRepository.java
│   └── HabitRepository.java
└── HabitTrackerApiApplication.java
```

---

## Getting Started

### Run Locally

**Prerequisites:** Java 17, PostgreSQL, Maven

```sql
CREATE DATABASE habit_tracker;
```

Set environment variables:
```
SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/habit_tracker
SPRING_DATASOURCE_USERNAME=postgres
SPRING_DATASOURCE_PASSWORD=your_password
JWT_SECRET=your_secret_key
```

```bash
./mvnw spring-boot:run
```

Open Swagger UI: `http://localhost:8080/swagger-ui/index.html`

### Run with Docker

```bash
docker-compose up --build
```

---

## Running Tests

```bash
./mvnw test
```

---

## Summary

This project covers the full cycle of building a secure REST API:
JWT-based authentication, protected endpoints, business logic with streak tracking,
JPA data management, and containerization with Docker.