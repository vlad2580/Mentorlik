# Mentorlik Backend Architecture

This document describes the architecture of the Mentorlik backend application following best practices and clean architecture principles.

## Package Structure

The application is organized into the following main packages:

### 1. Domain Layer

The core business logic and entities:
- `domain/entity`: Domain entities representing business objects
- `domain/repository`: Repository interfaces (no implementation)
- `domain/service`: Domain services implementing business logic
- `domain/mapper`: Mappers between entities and DTOs

### 2. Application Layer

The application services and use cases:
- `application`: Contains use cases and application services that orchestrate domain objects

### 3. API Layer

The presentation layer exposing REST endpoints:
- `api/controller`: REST controllers
- `api/dto`: Data Transfer Objects for API requests/responses
- `api/exception`: API-specific exception handling

### 4. Infrastructure Layer

External tools, adapters, and configurations:
- `infrastructure/config`: Application configuration
- `infrastructure/security`: Security-related components
- `infrastructure/persistence`: Database & repository implementations

## Design Principles

- **Dependency Rule**: Dependencies always point inward (API -> Application -> Domain)
- **Separation of Concerns**: Each layer has specific responsibilities
- **Single Responsibility**: Each class does one thing well
- **Interface Segregation**: Small, client-specific interfaces
- **Dependency Inversion**: High-level modules don't depend on low-level modules

## Benefits

- Improved testability
- Better separation of concerns
- More maintainable and modular code
- Easier to understand and navigate
- Flexibility to change implementation details without affecting business logic 