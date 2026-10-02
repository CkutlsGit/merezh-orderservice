# Order Service - Merezh

Microservice responsible for creating and managing orders.

📖 In Russian: [перевод на русский](https://github.com/CkutlsGit/merezh-orderservice/blob/main/README.ru.md)

## 📋 Overview

Order Service is a Spring Boot microservice that manages the order lifecycle: creation, storing items, calculating the total amount, and updating status.

The service participates in a **saga**: `order → payment → wallet → payment → order`. On order creation, Order Service saves it with the `PAYMENT_WAITING` status and synchronously calls Payment Service (`POST /place`). After payment, Payment Service notifies Order Service via `POST /update` with the new status. Order Service trusts `X-User-Id` from the Gateway.

## 🚀 Technology Stack

**Backend**

- Java 21 - core language
- Spring Boot 3 - application framework
- Spring Data JPA - database access and ORM
- RestTemplate - synchronous HTTP calls to Payment Service
  
**Database**

- PostgreSQL - production database

**DevOps**

- Docker - containerization
- Docker Compose - multi-container orchestration
- Spring Boot Actuator - health checks and monitoring

## ✨ Features

### 📦 Order Management

- Create an order with items (`OrderItem`)
- Calculate total amount (`totalAmount`) from items
- Store user order history
- Get order by ID
- Get all orders for a user
- Update payment status (called by Payment Service)

### 🔄 Saga with Payment Service

- **Order Service → Payment Service**: `POST /place` on order creation
- **Payment Service → Order Service**: `POST /update` with the new status (`SUCCESS` / `FAILED` / `WAITING`)
- Order is created with the `PAYMENT_WAITING` status
- If Payment Service is unavailable, the order stays in `PAYMENT_WAITING` (for retry)

### ✅ Data Validation

- Order item validation (`@NotBlank` on name and ID, `@Positive` on amount and quantity)
- Consistent error responses via `@RestControllerAdvice`
- Error propagation from Payment Service (forwards `message` from the response)

## 🛠️ Quick Start

### Prerequisites

- Docker
- Docker Compose

### Run with Docker Compose

```bash
docker compose up --build
```

The service will be available on port **8085**. 
Swagger path - `/swagger-ui.html`.

## 📚 API Endpoints

Base path: `/api/v1/orders`

| Method | Endpoint        | Description                               | Access                       |
|--------|-----------------|-------------------------------------------|------------------------------|
| GET    | `/get/{id}`     | Get order by ID                           | Authenticated                |
| GET    | `/get/user`     | Get all orders for the user               | Authenticated                |
| POST   | `/create`       | Create an order                           | Authenticated                |
| POST   | `/update`       | Update order status                       | Internal (payment-service)   |

**Note:** Protected endpoints expect the `X-User-Id` header, which is set by the Gateway.

## 📦 Project Structure

```
src/main/java/ru/merezh/orderservice/
├── config/                    # Spring configuration (RestTemplate)
├── controller/                # REST controllers
├── dto/                       # Data Transfer Objects
├── entity/                    # JPA entities (Order, OrderItem)
│   └── enums/                 # OrderStatus enum
├── exception/                 # Custom exceptions and handlers
│   ├── controller/            # @RestControllerAdvice
│   └── dto/                   # Error response DTOs
├── repository/                # Spring Data JPA repositories
└── service/                   # Business logic (OrderService)
```

## 🔒 Security and Reliability

- **Order is created on behalf of the authenticated user** (`X-User-Id` from the Gateway).
- **Ownership check** when fetching user orders.
- **Item validation** - you cannot create an order with zero quantity or negative amount.
- **The internal `/update` endpoint** must not be publicly exposed - only for Payment Service.
- **The service trusts the Gateway** for user identity.

## 🩺 Health Checks

The service exposes Spring Boot Actuator endpoints:

| Endpoint                     | Purpose                        |
|------------------------------|--------------------------------|
| `/actuator/health`           | Overall health                 |
| `/actuator/health/liveness`  | Liveness probe                 |
| `/actuator/health/readiness` | Readiness probe (includes DB)  |
| `/actuator/info`             | Service info                   |
