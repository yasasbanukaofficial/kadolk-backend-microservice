# Smart Parking Management System (SPMS)

A cloud-native, microservice-based application for real-time management and monitoring of parking spaces. Users can locate, reserve, and pay for parking; owners can monitor and manage their spaces dynamically.

## Table of Contents

- [Architecture](#architecture)
- [Tech Stack](#tech-stack)
- [Services](#services)
  - [parking-service](#parking-service)
  - [vehicle-service](#vehicle-service)
  - [user-service](#user-service)
  - [payment-service](#payment-service)
- [Service-to-Service Communication (Planned)](#service-to-service-communication-planned)
- [Getting Started](#getting-started)
- [Project Structure](#project-structure)
- [Resources](#resources)

---

## Architecture

Microservice architecture. Each service is independently deployable, owns its own database, and exposes a JSON REST API behind a consistent `ApiResponse` envelope. Spring Cloud Eureka is used as the service registry & discovery so services can locate each other by name (e.g. `parking-service`, `vehicle-service`) instead of hard-coded addresses, which enables the service-to-service communication described below.

```
                         ┌────────────────────┐
                         │   API Gateway       │   (planned - Spring Cloud Gateway)
                         │   localhost:8080    │
                         └──────────┬─────────┘
                                    │
              ┌─────────────────────┼─────────────────────┐
              │                     │                     │
    ┌─────────▼─────────┐ ┌────────▼──────────┐ ┌────────▼─────────┐
    │   parking-service │ │   vehicle-service │ │   user-service  │
    │   (Spring Boot)   │ │   (Spring Boot)   │ │ (Node/Express)  │
    │      :8081        │ │      :8082        │ │      :8083      │
    └─────────┬─────────┘ └────────┬──────────┘ └────────┬─────────┘
              │                    │                     │
              │           ┌───────▼───────┐              │
              └──────────▶│ payment-service│◀────────────┘
                          │  (Spring Boot) │
                          │     :8084      │
                          └───────┬───────┘
                                  │
                    ┌─────────────┼─────────────┐
                    │             │             │
                PostgreSQL     PostgreSQL     MongoDB
                (parking)    (vehicle+payment) (user)

                      ┌───────────────────────────┐
                      │   Eureka Registry :8761    │  (infra - not yet in repo)
                      └───────────────────────────┘
```

## Tech Stack

| Layer | Choice |
| --- | --- |
| Java services | Java 21, Spring Boot 4.1.0, Spring Data JPA (Hibernate 7), Bean Validation, ModelMapper, Spring Cloud Eureka Client |
| Node service | Node.js, TypeScript, Express 5, Mongoose (MongoDB), Zod (validation), JWT + bcryptjs (auth) |
| Databases | PostgreSQL (parking-service, vehicle-service, payment-service), MongoDB (user-service) |
| Communication (planned) | Service-to-service via **Spring WebFlux `WebClient`** in Java services and **axios** in the Node service |
| Tooling | Maven wrapper per Java service, `npm`/`tsx` for the Node service, Postman collections |

## Services

| Service | Stack | Port | Description |
| --- | --- | --- | --- |
| `services/parking-service` | Spring Boot + PostgreSQL | 8081 | Manages parking spaces: list, manage, reserve, release, update status, and filter by location/availability. |
| `services/vehicle-service` | Spring Boot + PostgreSQL | 8082 | Handles vehicle operations: register, update, retrieve vehicle details, link vehicles to users, and simulate entry/exit tracking. |
| `services/user-service` | Node.js (Express) + MongoDB | 8083 | Handles user operations: register/authenticate (JWT), view/update profiles, and access booking history/logs. |
| `services/payment-service` | Spring Boot + PostgreSQL | 8084 | Handles payment operations: create payments, validate mock card data, simulate transaction flow/status, generate digital receipts, and process refunds. |

All responses use a consistent envelope:

```json
{
  "statusCode": 201,
  "message": "Payment Created Successfully",
  "data": { }
}
```

---

### parking-service

Responsible for parking spaces and reservations. A parking spot has a `city`, `zone`, `location`, `address`, coordinates (`lat`/`lng`), a `status` (`AVAILABLE` / `OCCUPIED`), and optionally a linked `vehicleId` once reserved.

**Endpoints**

| Method | Path | Description |
| --- | --- | --- |
| GET | `/parking` | Get all parking spots |
| GET | `/parking/available` | Get only available spots |
| GET | `/parking/location/{location}` | Search spots by location (case-insensitive) |
| GET | `/parking/vehicle/{vehicleId}` | Get the spot currently occupied by a given vehicle |
| GET | `/parking/{id}` | Get a spot by id |
| POST | `/parking` | Create a parking spot |
| PUT | `/parking/{id}` | Update a parking spot |
| POST | `/parking/{parkingId}/reserve/{vehicleId}` | Reserve a spot for a vehicle |
| POST | `/parking/{parkingId}/release` | Release an occupied spot |
| DELETE | `/parking/{id}` | Delete a parking spot |

**Notable business rules**

- Reservation flow (`reserveParking`): a vehicle cannot reserve a second spot (`VehicleAlreadyReservedException`), and a spot must be `AVAILABLE` (`ParkingNotAvailable`). The spot row is locked with a **pessimistic lock** (`findByIdForUpdate`) so two concurrent reservations cannot both succeed.
- Release flow (`releaseParking`): only `OCCUPIED` spots can be released; the vehicle link is cleared and status returns to `AVAILABLE`.
- The `vehicleId` on a reservation is currently accepted as-is — validating it against `vehicle-service` is the next step (see [Service-to-Service Communication](#service-to-service-communication-planned)).

---

### vehicle-service

Responsible for vehicle registration and lifecycle. A vehicle belongs to a `userId`, has a unique `vehicleNumber`, a `type`, and a `status` (`INSIDE` / `OUTSIDE`) used to simulate entry/exit tracking.

**Endpoints**

| Method | Path | Description |
| --- | --- | --- |
| GET | `/vehicle` | Get all vehicles |
| GET | `/vehicle/user/{userId}` | Get all vehicles belonging to a user |
| GET | `/vehicle/number/{vehicleNumber}` | Get a vehicle by its plate number |
| GET | `/vehicle/{id}` | Get a vehicle by id |
| POST | `/vehicle` | Register a vehicle |
| PUT | `/vehicle/{id}` | Update a vehicle |
| POST | `/vehicle/{id}/entry` | Simulate the vehicle entering the lot |
| POST | `/vehicle/{id}/exit` | Simulate the vehicle leaving the lot |
| DELETE | `/vehicle/{id}` | Delete a vehicle |

**Notable business rules**

- `vehicleNumber` must be unique (`VehicleNumberAlreadyExistsException`).
- Entry/exit are guarded: a vehicle already `INSIDE` cannot enter again (`VehicleAlreadyInsideException`), and a vehicle that is not inside cannot exit (`VehicleNotInsideException`).
- This is the service other services will call to **validate that a `vehicleId` exists** and to fetch vehicle details.

---

### user-service

The only Node.js service. Handles users and their booking history using Express + MongoDB. Passwords are hashed with bcrypt and authentication is issued as a JWT.

**Endpoints**

| Method | Path | Description |
| --- | --- | --- |
| GET | `/user` | Get all users |
| GET | `/user/:id` | Get a user by id |
| POST | `/user/register` | Register a new user (returns JWT) |
| POST | `/user/login` | Authenticate (returns JWT) |
| PUT | `/user/:id` | Update profile |
| DELETE | `/user/:id` | Delete a user |
| GET | `/user/:id/bookings` | Get a user's booking history |
| POST | `/user/:id/bookings` | Add an entry to the booking history |

**Notable business rules**

- Input is validated with **Zod** schemas before reaching the service layer.
- Booking history is stored on the user document; currently it is a simple log. Enriching it with real data from `parking-service` / `payment-service` is part of the cross-service work.
- Uses `axios` for outbound HTTP calls to other services (e.g. to validate/fetch data) — see below.

---

### payment-service

Responsible for the mock payment flow. A payment references a `bookingId` and `userId`, holds an `amount`, a `paymentMethod`, the card's last 4 digits (full card is never stored), and a `status` (`PENDING` / `PAID` / `FAILED` / `REFUNDED`). Successful transactions get a digital `receiptNumber` (e.g. `RCP-8F8D4E5B`) and a `paidAt` timestamp.

**Endpoints**

| Method | Path | Description |
| --- | --- | --- |
| GET | `/payment` | Get all payments |
| GET | `/payment/booking/{bookingId}` | Get payments for a booking |
| GET | `/payment/user/{userId}` | Get payments made by a user |
| GET | `/payment/receipt/{id}` | Get the digital receipt for a payment |
| GET | `/payment/{id}` | Get a payment by id |
| POST | `/payment` | Create a payment with mock card details |
| PUT | `/payment/{id}` | Update amount/method/status |
| POST | `/payment/{id}/pay` | Process the transaction (simulate the gateway) |
| POST | `/payment/{id}/refund` | Refund a paid payment |
| DELETE | `/payment/{id}` | Delete a payment |

**Notable business rules**

- Card validation is mocked: the card number must pass the **Luhn algorithm** and the expiry must not be in the past.
- Only the **last 4 digits** of the card are persisted; the card is never stored or returned.
- `POST /payment/{id}/pay` simulates a payment gateway:
  - card ending in `0000` → `FAILED`
  - otherwise → `PAID`, with a receipt number and `paidAt`
- A `PAID` payment cannot be paid again (`PaymentAlreadyPaidException`); only `PAID` payments can be refunded (`PaymentNotRefundableException`); receipts are only available for processed payments (`PaymentNotProcessedException`).
- `pay`/`refund` lock the row with a pessimistic lock to prevent double-processing under concurrency.

---

## Service-to-Service Communication (Planned)

Right now every service is self-contained: `parking-service` accepts any `vehicleId`, `payment-service` accepts any `bookingId`/`userId`, and there is no verification that referenced records actually exist in the owning service. The plan is to add real inter-service calls:

| Caller | Calls | Purpose |
| --- | --- | --- |
| `parking-service` | `vehicle-service` | Validate that a `vehicleId` exists before reserving; fetch vehicle details |
| `payment-service` | `parking-service` | Validate that a `bookingId` exists before creating a payment |
| `payment-service` | `user-service` | Validate that a `userId` exists |
| `vehicle-service` | `user-service` | Validate that a `userId` exists when registering a vehicle |
| `user-service` | `parking-service` / `payment-service` | Enrich booking history with real reservation/payment data |

**How it will be implemented**

- **Java services** (parking, vehicle, payment) will use **Spring WebFlux `WebClient`** — a non-blocking HTTP client — to call sibling services. Since Spring Cloud Eureka is configured, calls will go through the service registry (load-balanced), but a direct URL fallback keeps local development working.
- **Node service** (user) will use **axios** for its outbound calls.

---

## Getting Started

### Prerequisites

- JDK 21+
- Node.js 18+
- PostgreSQL and MongoDB (or hosted equivalents, e.g. Neon / Atlas)
- A running Eureka server on `localhost:8761` (infrastructure module is planned — services will still boot without it but will not register/discover)

### Configure & run

Each service reads a `.env` file (values are imported via `spring.config.import: optional:file:.env[.properties]` in Java services, and `dotenv` in the Node service). Create a `.env` in each service folder with at least the database URL:

```bash
# e.g. services/parking-service/.env
DB_URL=jdbc:postgresql://localhost:5432/parking_db
```

Then run each service from its own directory:

```bash
# Java services
./mvnw spring-boot:run

# Node service
npm install
npm run dev
```

Services listen on: parking `8081`, vehicle `8082`, user `8083`, payment `8084`.

## Project Structure

```
├── infastructure/               # Eureka / gateway / config-server (planned)
├── config-server/               # centralized config (planned)
├── docs/                        # coursework PDF, screenshots
├── postman/
│   └── services/                # Postman collections per service
└── services/
    ├── parking-service/         # Spring Boot, PostgreSQL, :8081
    ├── vehicle-service/         # Spring Boot, PostgreSQL, :8082
    ├── user-service/            # Node.js/Express + MongoDB, :8083
    └── payment-service/         # Spring Boot, PostgreSQL, :8084
```

## Resources

- [Parking Service Postman Collection](./postman/services/parking-service.postman_collection.json)
- [Vehicle Service Postman Collection](./postman/services/vehicle-service.postman_collection.json)
- [User Service Postman Collection](./postman/services/user-service.postman_collection.json)
- [Payment Service Postman Collection](./postman/services/payment-service.postman_collection.json)
- ![Eureka Dashboard](./docs/screenshots/eureka_dashboard.png)
