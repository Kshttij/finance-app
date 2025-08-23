# Stage 1: Build the Spring Boot app
FROM maven:3.9.8-eclipse-temurin-17 AS build
WORKDIR /app

# Copy only necessary files for faster build
COPY pom.xml .
COPY src ./src

# Build the jar without running tests
RUN mvn clean package -DskipTests

# Stage 2: Run the app
FROM eclipse-temurin:17-jdk
WORKDIR /app

# Copy the jar from the build stage
COPY --from=build /app/target/*.jar app.jar

# Expose the port (can be overridden by env)
EXPOSE 8080

# Use environment variables from .env for database and other configs
# Spring Boot automatically reads them via ${VAR_NAME} in application.properties
ENTRYPOINT ["java","-jar","/app/app.jar"]
