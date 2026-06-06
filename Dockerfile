# --- Stage 1: Build the application ---
FROM maven:3.9.6-eclipse-temurin-17-alpine AS builder
WORKDIR /app

# Copy the pom.xml and download dependencies (cached layer)
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copy the source code and build the application package
COPY src ./src
RUN mvn package -DskipTests

# --- Stage 2: Create the secure runtime image ---
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

# Copy the compiled jar from the builder stage
COPY --from=builder /app/target/*.jar app.jar

# Expose port 8080 for your Spring Boot application
EXPOSE 8080

# Run the application with optimized memory settings
ENTRYPOINT ["java", "-jar", "app.jar"]