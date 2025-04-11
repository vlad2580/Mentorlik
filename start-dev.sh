#!/bin/bash

# Check if Docker is running
if ! docker info > /dev/null 2>&1; then
    echo "Docker is not running. Please start Docker and try again."
    exit 1
fi

# Function to check if port is in use
check_port() {
    local port=$1
    if lsof -i :$port > /dev/null 2>&1; then
        return 0
    else
        return 1
    fi
}

# Start database and pgAdmin
echo "Starting database and pgAdmin..."
cd docker
docker-compose -f docker-compose.dev.yml up -d
cd ..

# Check if ports are available
if check_port 8080; then
    echo "Port 8080 is already in use. Please free the port and try again."
    exit 1
fi

if check_port 4200; then
    echo "Port 4200 is already in use. Please free the port and try again."
    exit 1
fi

# Start Spring Boot application in background
echo "Starting Spring Boot application..."
cd backend
SPRING_PROFILES_ACTIVE=dev \
SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5434/mentorlik_db \
SPRING_DATASOURCE_USERNAME=mentorlik_user \
SPRING_DATASOURCE_PASSWORD=mentorlik_password \
./mvnw spring-boot:run &
SPRING_PID=$!
cd ..

# Wait for Spring Boot to start
sleep 10

# Start Angular application in background
echo "Starting Angular application..."
cd frontend-angular
npm install
ng serve &
ANGULAR_PID=$!
cd ..

echo "Local development environment is running!"
echo "Backend is available at: http://localhost:8080"
echo "Frontend is available at: http://localhost:4200"
echo "pgAdmin is available at: http://localhost:5050"
echo "Database is available at: localhost:5434"

# Handle Ctrl+C for graceful shutdown
trap "echo 'Stopping applications...'; kill $SPRING_PID $ANGULAR_PID; cd docker; docker-compose -f docker-compose.dev.yml down; exit 0" INT

# Wait for completion
wait 