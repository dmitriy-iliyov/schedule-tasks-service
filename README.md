[![CodeFactor](https://www.codefactor.io/repository/github/dmitriy-iliyov/schedule-tasks-service/badge)](https://www.codefactor.io/repository/github/dmitriy-iliyov/schedule-tasks-service)
![Java](https://img.shields.io/badge/Java-17-007396?logo=openjdk)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.4.4-6DB33F?logo=springboot)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-16.1-336791?logo=postgresql)

## Overview

This repository contains the system task scheduler and orchestrator backend for the **Aid Compass** platform.  
It is responsible for orchestrating scheduled tasks and interacting with the core backend service.

Project components:
- core backend – https://github.com/dmitriy-iliyov/aid-compass-backend  

## Functions

- persist and manage scheduled tasks  
- trigger task execution via the core backend  
- measure and record task execution times  
- runtime configuration of scheduling and execution parameters via API  

## Non-functional requirements

- API for configuring schedule and execution properties in runtime;
- HTTPS connection with core backend;

## Run
1. **Configure environment variables (`.docker_env `):**
```properties
DATABASE_URL=jdbc:postgresql://aid-compass-scheduled-tasks-postgresql:5432/aid_compass_scheduled_tasks_database
DATABASE_USERNAME=admin
DATABASE_PASSWORD=root

API_PROTOCOL=http
API_HOST=localhost
API_PORT=8080

SERVICE_NAME=schedule_task_service_name
SERVICE_KEY=secret_service_key
```
2. Build and run
```bash
docker compose build
docker compose up -d
```
