# Build stage
FROM maven:3.9.6-eclipse-temurin-21-alpine AS build
WORKDIR /app
COPY pom.xml .
# Download dependencies first (this will be cached unless pom.xml changes)
RUN mvn dependency:go-offline

COPY src ./src
# Build the application
RUN mvn clean package -DskipTests

# Run stage
FROM eclipse-temurin:21-jdk-alpine
WORKDIR /app
# Copy only the built jar from the build stage
COPY --from=build /app/target/mentorlik-backend-0.0.1-SNAPSHOT.jar /app/mentorlik-backend.jar

EXPOSE 8080

# Run the application with debug options
CMD ["java", "-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:5005", "-jar", "mentorlik-backend.jar", "--spring.profiles.active=dev"]