# 🐾 Petstore Monolith

> A modern, open-source Petstore built with **Spring Boot 3 + Angular 14**, featuring JWT Auth + Refresh, Redis token allow-list, Flyway migrations, and Docker orchestration.

## Architecture
```
Angular 14 SPA  →  Spring Boot 3 Monolith (REST API)
                       ↓
                 PostgreSQL + Redis
```

## Quick Start
```bash
# Backend
cd backend
./mvnw -DskipTests package || mvn -DskipTests package
cd ..

# Frontend
cd frontend
npm ci
npm run build
cd ..

# Docker stack
cd docker
docker compose up --build
```

### URLs
- Angular: http://localhost:4200
- API: http://localhost:8080
- Swagger UI: http://localhost:8080/swagger-ui/index.html
- Postgres: localhost:5432 (pet/pet), DB: petdb
- Redis: localhost:6379

## Features
- **JWT + Refresh** with Redis allow-list (whitelist) of `jti`
- **RSA password encryption** during login (browser RSA-OAEP)
- **Flyway** migrations (schema + seed users/roles/pets)
- **Hibernate/JPA**, **stateless** Spring Security
- **Angular 14** with **JWT interceptor**, **AuthGuard**, and lazy modules
- **Global Exception Handler** (`@RestControllerAdvice`)
- **Docker Compose** for full stack

## Env Vars
| Name | Default | Purpose |
|---|---|---|
| SPRING_DATASOURCE_URL | jdbc:postgresql://postgres:5432/petdb | DB URL |
| SPRING_DATASOURCE_USERNAME | pet | DB user |
| SPRING_DATASOURCE_PASSWORD | pet | DB pass |
| SPRING_REDIS_HOST | redis | Redis host |
| JWT_SECRET | in `application.yml` | HMAC key (change in prod) |

## Test
```bash
cd backend
./mvnw test || mvn test
```

## License
MIT © 2025 Petstore Contributors
