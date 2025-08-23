# Stage 1: Build the Spring Boot app
FROM maven:3.9.8-eclipse-temurin-17 AS build

# Set working directory
WORKDIR /app

# Copy all files
COPY pom.xml .
COPY src ./src

# Build the app (skip tests for faster build)
RUN mvn clean package -DskipTests

# Stage 2: Run the app
FROM eclipse-temurin:17-jdk

WORKDIR /app

# Copy the built jar from the build stage
COPY --from=build /app/target/*.jar app.jar

# Expose port 8080
EXPOSE 8080

# Run the app
ENTRYPOINT ["java", "-jar", "app.jar"]
