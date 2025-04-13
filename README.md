# MentorLik

## Project Description

**MentorLik** is a web application designed to connect users with IT mentors. Users can search for mentors based on their specialization, experience, and price preferences, while mentors can share their knowledge with others.

## Technology Stack

### Backend
- **Spring Boot**: Java-based framework
- **PostgreSQL**: Main database for storing information about mentors, users, and consultation bookings
- **Spring Security**: For authentication and authorization
- **JPA/Hibernate**: For database interaction
- **JWT**: For secure authentication
- **Elasticsearch**: For advanced search capabilities

### Frontend
- **Angular**: Modern frontend framework
- **SCSS/CSS**: For styling
- **Nginx**: For serving the frontend application

### Infrastructure
- **Docker/Docker Compose**: For containerization and orchestration
- **Maven**: For backend dependency management
- **npm**: For frontend dependency management

## Project Structure

### 1. `/backend` Directory
- Java Spring Boot application
- Contains all server-side logic, database interactions, and API endpoints
- Key files:
  - `pom.xml`: Maven configuration with all dependencies
  - `src/main/java/`: Contains all Java source code
  - `src/main/resources/`: Contains configuration files
  - `Dockerfile`: For containerizing the backend

### 2. `/frontend-angular` Directory
- Angular-based frontend application
- Contains all UI components, services, and assets
- Key files:
  - `package.json`: npm dependencies and scripts
  - `src/app/`: Contains Angular components, services, and modules
  - `src/assets/`: Contains static assets like images
  - `src/styles/`: Contains global styles
  - `Dockerfile`: For containerizing the frontend

### 3. `/docker` Directory
- Contains Docker Compose configurations for development and production
- Key files:
  - `docker-compose.yml`: Main Docker Compose configuration
  - `docker-compose.dev.yml`: Development-specific configuration
  - `/postgres/`: Configuration files for PostgreSQL
  - `/pgadmin/`: Configuration files for pgAdmin

## Setup and Running

### Prerequisites
- Docker and Docker Compose
- Java 21
- Node.js and npm
- Maven

### Development Environment

#### Option 1: Using the Script
Run the development environment using the provided script:

```bash
# For Linux/macOS
./start-dev.sh

# For Windows
./start-dev.ps1
```

This will:
1. Start PostgreSQL database and pgAdmin
2. Run the Spring Boot backend on http://localhost:8080
3. Run the Angular frontend on http://localhost:4200

#### Option 2: Manual Setup

1. **Start the Database**:
   ```bash
   cd docker
   docker-compose -f docker-compose.dev.yml up -d
   ```

2. **Start the Backend**:
   ```bash
   cd backend
   SPRING_PROFILES_ACTIVE=dev \
   SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5434/mentorlik_db \
   SPRING_DATASOURCE_USERNAME=mentorlik_user \
   SPRING_DATASOURCE_PASSWORD=mentorlik_password \
   ./mvnw spring-boot:run
   ```

3. **Start the Frontend**:
   ```bash
   cd frontend-angular
   npm install
   ng serve
   ```

### Production Deployment

To deploy the entire application stack:

```bash
cd docker
docker-compose up -d
```

This will start:
- PostgreSQL database
- Spring Boot backend
- Angular frontend (served via Nginx)
- pgAdmin for database management

The application will be available at: http://localhost:80

## Development Guidelines

1. **Code Comments**:
   - All code comments should be written exclusively in **English** for better collaboration and readability of the project.
   - This allows all team members, including potential international collaborators, to easily understand and work with the code.

2. **Development Branches**:
   - Each development direction (frontend, backend, database, etc.) should be assigned a separate branch.
   - Example branches:
     - `frontend` – for user interface development (Angular, SCSS)
     - `backend` – for server-side logic, database interactions, or API changes
     - `devops` – for infrastructure, deployment, and CI/CD configuration
   - Changes should be incorporated into the main branch through **pull requests**, after thorough peer review.

## Available Services

When running the complete stack:
- **Frontend**: http://localhost (port 80)
- **Backend API**: http://localhost:8080
- **pgAdmin**: http://localhost:5050 (default credentials: admin@mentorlik.com / admin)
- **PostgreSQL**: localhost:5434 (default credentials: mentorlik_user / mentorlik_password)

## Additional Information

For more information on the specific components:
- Angular frontend: See `/frontend-angular/README.md`
- Backend API documentation: Available at http://localhost:8080/swagger-ui.html when the backend is running
