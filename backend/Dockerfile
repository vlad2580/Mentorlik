# Stage 1: Build the application
FROM maven:3.9.8-eclipse-temurin-21-alpine AS build

# Set working directory in the build stage
WORKDIR /app

# Copy only the pom.xml initially to leverage Docker cache for dependencies
COPY pom.xml .

# Install dependencies
RUN mvn dependency:go-offline

# Copy the rest of your project files
COPY src ./src

# Build the application
RUN mvn clean package -DskipTests

# Stage 2: Run the application
FROM amazoncorretto:21-alpine-jdk

# Set working directory in the final stage
WORKDIR /app

# Copy only the built JAR from the build stage
COPY --from=build /app/target/mentorlik-backend-0.0.1-SNAPSHOT.jar /app/mentorlik-backend.jar

# Expose the port the app will run on
EXPOSE 8080

# Set environment variables for the application (optional, can be overridden)
ENV JAVA_OPTS="-Xms512m -Xmx1024m"

# Run the application with the specified JAVA_OPTS
CMD ["sh", "-c", "java $JAVA_OPTS -jar /app/mentorlik-backend.jar"]