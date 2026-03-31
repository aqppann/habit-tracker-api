# Habit Tracker API

A REST API for tracking daily habits with streak functionality, built with Spring Boot and PostgreSQL.

## Tech Stack

![Java](https://img.shields.io/badge/Java-17-orange)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-4.0.5-green)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-17-blue)
![JWT](https://img.shields.io/badge/JWT-Auth-yellow)
![Docker](https://img.shields.io/badge/Docker-ready-blue)

## Features

- User registration and login with JWT authentication
- Create, update and delete habits
- Mark habits as completed for the day
- Streak tracking — counts consecutive days of completion
- Swagger UI documentation

## Getting Started

### Prerequisites

- Java 17
- PostgreSQL 17
- Maven

### Run locally

1. Clone the repository
```bash
git clone https://github.com/aqppann/habit-tracker-api.git
cd habit-tracker-api
```

2. Create PostgreSQL database
```sql
CREATE DATABASE habit_tracker;
```

3. Set environment variables
```
DB_USERNAME=postgres
DB_PASSWORD=your_password
JWT_SECRET=your_secret_key
```

4. Run the application
```bash
./mvnw spring-boot:run
```

5. Open Swagger UI
```
http://localhost:8080/swagger-ui/index.html
```

### Run with Docker
```bash
docker-compose up --build
```

## API Endpoints

### Auth
| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | /api/v1/auth/register | Register new user |
| POST | /api/v1/auth/login | Login, returns JWT token |

### Habits
| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | /api/v1/habits | Get all habits |
| GET | /api/v1/habits/{id} | Get habit by id |
| POST | /api/v1/habits | Create habit |
| PUT | /api/v1/habits/{id} | Update habit |
| DELETE | /api/v1/habits/{id} | Delete habit |
| POST | /api/v1/habits/{id}/complete | Mark as completed today |
| GET | /api/v1/habits/{id}/streak | Get current streak |

## Running Tests
```bash
./mvnw test
```