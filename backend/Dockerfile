# Base image with Java Runtime
FROM eclipse-temurin:21-jre

# Set the working directory inside the container
WORKDIR /app

# Copy the Spring Boot JAR into the container
COPY target/*.jar app.jar

# Document the port used by the application
EXPOSE 8080

# Command to start the Spring Boot application
ENTRYPOINT ["java", "-jar", "app.jar"]
