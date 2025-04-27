# Full-Stack Application with Java & Angular

This project is a full-stack web application built using Java, Spring Boot, and Angular. 
The backend is developed with Spring Boot, featuring a REST API, authentication with JWT, and database management with PostgreSQL, JPA, and Flyway. The frontend, built with Angular, includes UI components styled with SCSS and HTML, along with data validation for improved user experience.

## Key Features
- User Authentication - Secure login and access control using JWT
- REST API - Backend endpoints for managing application data
- Database Integration - PostgreSQL with schema migrations via Flyway
- UI - Styled with SCSS and HTML for an optimal experience

## Technologies Used
- Backend: Java, Spring Boot, JPA, Flyway, PostgreSQL
- Frontend: Angular, HTML, SCSS, JavaScript
- Containerization: Docker

## Prerequisites
- Java JDK 17 or higher
- Node.js 16 or higher
- PostgreSQL 14 (if running without Docker)
- Docker and Docker Compose (if running with Docker)
- Maven
- Angular CLI

## Running without Docker

### Database Setup
1. Install PostgreSQL 14
2. Create a database named `shop`
3. Update database credentials in `shop/src/main/resources/application.properties` if needed

### Backend Setup
1. Navigate to the backend directory:
   ```bash
   cd shop
   ```
2. Build the project:
   ```bash
   mvn clean install
   ```
3. Run the Spring Boot application:
   ```bash
   mvn spring-boot:run
   ```
The backend will be available at `http://localhost:8080`

### Frontend Setup
1. Navigate to the frontend directory:
   ```bash
   cd online-shop
   ```
2. Install dependencies:
   ```bash
   npm install
   ```
3. Start the development server:
   ```bash
   ng serve
   ```
The frontend will be available at `http://localhost:4200`

## Running with Docker

1. Make sure Docker Desktop and Docker Compose are installed
2. Navigate to the docker configuration directory:
   ```bash
   cd docker-config
   ```
3. Build and start all services:
   ```bash
   docker-compose up -d --build
   ```

This will start:
- PostgreSQL database at port 5432
- Backend service at `http://localhost:8080`
- Frontend service at `http://localhost:4200`

To stop the services:
```bash
docker-compose down
```
