# Use an OpenJDK image as a base
FROM openjdk:21-jdk-slim

# Set working directory in the container
WORKDIR /app

# Copy the JAR file
COPY target/mentorlik-backend.jar mentorlik-backend.jar

# Expose the port the app runs on
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "mentorlik-backend.jar"]