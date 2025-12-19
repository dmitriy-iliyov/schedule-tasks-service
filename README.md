![Java](https://img.shields.io/badge/Java-17-007396?logo=openjdk)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.4.4-6DB33F?logo=springboot)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-16+-336791?logo=postgresql)

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

## Non-functional Requirements

- API for configuring schedule and execution properties in runtime;
- HTTPS connection with core backend;

## Run

```bash
docker compose build
docker compose up -d
```
