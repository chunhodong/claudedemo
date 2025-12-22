# Stage 1: Build
FROM gradle:8.5-jdk17 AS builder

WORKDIR /app

# Copy Gradle files
COPY settings.gradle build.gradle ./
COPY gradle ./gradle
COPY gradlew ./

# Copy source code
COPY common ./common
COPY coupon-module ./coupon-module

# Build the application
RUN ./gradlew :coupon-module:bootJar --no-daemon

# Stage 2: Runtime
FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

# Copy the built jar from builder stage
COPY --from=builder /app/coupon-module/build/libs/*.jar app.jar

# Expose port
EXPOSE 8082

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
