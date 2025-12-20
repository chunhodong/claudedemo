# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Build & Test Commands

```bash
# Build the project
./gradlew build

# Run the application
./gradlew bootRun

# Run all tests
./gradlew test

# Run a specific test class
./gradlew test --tests "com.example.claudedemo.user.presentation.UserControllerTest"

# Run a specific test method
./gradlew test --tests "com.example.claudedemo.user.presentation.UserControllerTest.create"

# Clean build
./gradlew clean build
```

## Architecture

This is a Spring Boot 4.0 application following **DDD (Domain-Driven Design)** with a layered architecture.

### Layer Structure (per domain module)

```
user/
├── domain/           # Domain layer - pure business logic, no framework dependencies
│   ├── User.java             # Domain entity (POJO)
│   ├── UserRepository.java   # Repository interface (port)
│   └── *Exception.java       # Domain exceptions
├── application/      # Application layer - orchestrates use cases
│   ├── UserService.java      # Service with business logic
│   ├── UserRequest.java      # Input DTO
│   └── UserResponse.java     # Output DTO
├── presentation/     # Presentation layer - handles HTTP
│   ├── UserController.java   # REST API (/api/users)
│   └── UserViewController.java  # Thymeleaf views
└── infrastructure/   # Infrastructure layer - implements ports
    ├── UserJpaEntity.java    # JPA entity (separate from domain)
    ├── UserJpaRepository.java    # Spring Data JPA interface
    └── UserRepositoryImpl.java   # Repository implementation (adapter)
```

### Key Patterns

- **Domain entity isolation**: `User` (domain) is separate from `UserJpaEntity` (JPA). Conversion via `from()` and `toDomain()` methods
- **Repository abstraction**: Domain defines `UserRepository` interface; infrastructure provides `UserRepositoryImpl`
- **DTOs**: `UserRequest`/`UserResponse` for API boundaries; domain entities never exposed directly

## Database

- **Production**: Oracle DB (jdbc:oracle:thin:@localhost:1521/XEPDB1)
- **Test**: H2 in-memory (auto-configured via src/test/resources/application.yml)

Tests use `@SpringBootTest(webEnvironment = RANDOM_PORT)` with `WebTestClient`.
