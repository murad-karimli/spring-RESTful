
FROM openjdk:17-jdk-alpine


WORKDIR /app


COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .
COPY src ./src


RUN ./mvnw clean package -DskipTests


COPY src/test ./src/test

# Run tests
RUN ./mvnw test


EXPOSE 8080


CMD ["java", "-jar", "target/app-0.0.1-SNAPSHOT-1.0.0.jar"]
