# Smart Parking Management System

A cloud-native, microservice-based application for real-time management and monitoring of parking spaces. Users can locate, reserve, and pay for parking; owners can monitor and manage their spaces dynamically.

## Architecture

Microservice architecture built primarily with Spring Boot, Spring Cloud Eureka (service registry & discovery), Spring Cloud Config (centralized configuration), and Spring Cloud Gateway (API gateway). See the proposed structure in [Coursework.pdf](./docs/Coursework.pdf).

## Services

| Service | Description |
| --- | --- |
| `services/parking-service` | Manages parking spaces: list, manage, reserve, release, update status, and filter by location/availability. |

## Getting Started

Each service is a Spring Boot application. Configure the environment variables (e.g. `DB_URL`) via a `.env` file and start a Eureka server, then run each service with:

```bash
./mvnw spring-boot:run
```

## Resources

- [Postman Collection](./postman/services/parking-service.postman_collection.json)
- ![Eureka Dashboard](./docs/screenshots/eureka_dashboard.png)
