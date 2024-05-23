# Use an official OpenJDK runtime as a parent image
FROM openjdk:17-jdk-alpine

# Set the working directory in the container
WORKDIR /app

# Copy the Maven executable and project files to the container
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .
COPY src ./src

# Install Maven dependencies and build the application
RUN ./mvnw clean package -DskipTests

# Copy test files into the container
COPY src/test ./src/test

# Run tests
RUN ./mvnw test

# Expose the port the application runs on
EXPOSE 8080

# Run the application
CMD ["java", "-jar", "target/app-0.0.1-SNAPSHOT-1.0.0.jar"]
