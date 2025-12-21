# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Structure

This is a **multi-module Gradle project** with a domain-driven architecture.

### Modules

```
claudedemo/
├── common/                 # Common utilities and configuration
│   └── SwaggerConfig.java
├── product-module/         # Product domain module
├── coupon-module/          # Coupon domain module
└── app/                    # Main application (executable)
    ├── ClaudedemoApplication.java
    ├── GlobalExceptionHandler.java
    └── HomeController.java
```

### Module Dependencies

```
app → product-module, coupon-module, common
product-module → common
coupon-module → common
```

## Build & Test Commands

```bash
# Build all modules
./gradlew build

# Run the application (from app module)
./gradlew :app:bootRun

# Run all tests
./gradlew test

# Run tests for a specific module
./gradlew :product-module:test
./gradlew :coupon-module:test
./gradlew :app:test

# Run a specific test class
./gradlew :product-module:test --tests "com.example.claudedemo.product.presentation.ProductControllerTest"
./gradlew :coupon-module:test --tests "com.example.claudedemo.coupon.application.CouponServiceTest"

# Clean build
./gradlew clean build

# List all modules
./gradlew projects
```

## Architecture

This is a Spring Boot 4.0 application following **DDD (Domain-Driven Design)** with a **multi-module architecture**.

### Domain Module Structure

Each domain module (product-module, coupon-module) follows the same DDD layered architecture:

```
{domain}-module/
├── domain/           # Domain layer - pure business logic, no framework dependencies
│   ├── {Entity}.java          # Domain entity (POJO)
│   ├── {Entity}Repository.java   # Repository interface (port)
│   └── *Exception.java        # Domain exceptions
├── application/      # Application layer - orchestrates use cases
│   ├── {Entity}Service.java   # Service with business logic
│   ├── {Entity}Request.java   # Input DTO
│   └── {Entity}Response.java  # Output DTO
├── presentation/     # Presentation layer - handles HTTP
│   ├── {Entity}Controller.java      # REST API (/api/{entities})
│   └── {Entity}ViewController.java  # Thymeleaf views
├── infrastructure/   # Infrastructure layer - implements ports
│   ├── {Entity}JpaEntity.java       # JPA entity (separate from domain)
│   ├── {Entity}JpaRepository.java   # Spring Data JPA interface
│   └── {Entity}RepositoryImpl.java  # Repository implementation (adapter)
└── templates/        # Thymeleaf templates
    └── {entities}/
        ├── list.html
        └── form.html
```

### Key Patterns

- **Multi-module separation**: Each domain is isolated in its own module
- **Domain entity isolation**: Domain entities (e.g., `Product`) are separate from JPA entities (e.g., `ProductJpaEntity`)
  - Conversion via `from()` and `toDomain()` methods
- **Repository abstraction**: Domain defines repository interface; infrastructure provides implementation
- **DTOs**: Request/Response DTOs for API boundaries; domain entities never exposed directly
- **Global exception handling**: Centralized in app module's `GlobalExceptionHandler`

## Current Domains

### 1. Product Domain (product-module)
- REST API: `/api/products`
- View: `/products`
- Fields: id, name, price, sellerId

### 2. Coupon Domain (coupon-module)
- REST API: `/api/coupons`
- View: `/coupons`
- Fields: id, code, discountRate, maxDiscountAmount, expiryDate, isActive
- Special endpoint: `POST /api/coupons/validate` - validates coupon by code

## Database

- **Production**: Oracle DB (jdbc:oracle:thin:@localhost:1521/XEPDB1)
  - Tables: `products`, `coupons`
- **Test**: H2 in-memory (auto-configured via {module}/src/test/resources/application.yml)

Tests use `@SpringBootTest(webEnvironment = RANDOM_PORT)` with `WebTestClient`.

## API Documentation

- **Swagger UI**: http://localhost:8080/swagger-ui/index.html
- **Home Page**: http://localhost:8080

## Important Notes

- Each domain module has its own `TestApplication.java` and `TestExceptionHandler.java` for integration tests
- The `-parameters` compiler flag is enabled for all modules to preserve parameter names for Spring MVC
- DTOs use wrapper types (`Boolean`, `Integer`, `Long`) instead of primitives for proper JSON serialization
