# Build stage
FROM maven:3.9.6-eclipse-temurin-21-alpine AS build
WORKDIR /app

# Copy only the files needed for dependency resolution
COPY pom.xml .
COPY src/main/resources/application.properties src/main/resources/application.properties

# Download dependencies first (this will be cached unless pom.xml changes)
RUN mvn dependency:go-offline -B

# Copy source code
COPY src ./src

# Build the application
RUN mvn clean package -DskipTests -Dmaven.test.skip=true

# Run stage
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Install curl for healthcheck
RUN apk add --no-cache curl

# Create a non-root user
RUN addgroup -S spring && adduser -S spring -G spring
USER spring

# Copy only the built jar from the build stage
COPY --from=build --chown=spring:spring /app/target/mentorlik-backend-0.0.1-SNAPSHOT.jar /app/mentorlik-backend.jar

EXPOSE 8080

# Health check configuration
HEALTHCHECK --interval=30s --timeout=3s --start-period=15s --retries=3 \
    CMD curl -f http://localhost:8080/actuator/health || exit 1

# Run the application
ENTRYPOINT ["java", "-jar", "mentorlik-backend.jar"]
CMD ["--spring.profiles.active=dev"]