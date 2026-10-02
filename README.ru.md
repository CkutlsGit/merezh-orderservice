# Order Service - Merezh

Микросервис, отвечающий за создание и управление заказами.

## 📋 Обзор

Order Service - это микросервис на Spring Boot, который управляет жизненным циклом заказа: создание, хранение позиций, расчёт итоговой суммы, обновление статуса.

Сервис участвует в **саге**: `order → payment → wallet → payment → order`. При создании заказа Order Service сохраняет его в статусе `PAYMENT_WAITING` и синхронно вызывает Payment Service (`POST /place`). После оплаты Payment Service уведомляет Order Service через `POST /update` с новым статусом. Order Service доверяет `X-User-Id` от Gateway.

## 🚀 Технологический стек

**Backend**

- Java 21 - основной язык
- Spring Boot 3 - фреймворк приложения
- Spring Data JPA - доступ к БД и ORM
- RestTemplate - синхронные HTTP-вызовы к Payment Service

**База данных**

- PostgreSQL - основная БД

**DevOps**

- Docker - контейнеризация
- Docker Compose - оркестрация нескольких контейнеров
- Spring Boot Actuator - healthcheck и мониторинг

## ✨ Возможности

### 📦 Управление заказами

- Создание заказа с позициями (`OrderItem`)
- Расчёт итоговой суммы (`totalAmount`) на основе позиций
- Хранение истории заказов пользователя
- Получение заказа по ID
- Получение всех заказов пользователя
- Обновление статуса оплаты (вызывается Payment Service)

### 🔄 Сага с Payment Service

- **Order Service → Payment Service**: `POST /place` при создании заказа
- **Payment Service → Order Service**: `POST /update` с новым статусом (`SUCCESS` / `FAILED` / `WAITING`)
- Заказ создаётся в статусе `PAYMENT_WAITING`
- Если Payment Service недоступен, заказ остаётся в `PAYMENT_WAITING` (для повторной попытки)

### ✅ Валидация данных

- Валидация позиций заказа (`@NotBlank` на имя и ID, `@Positive` на цену и количество)
- Единый формат ошибок через `@RestControllerAdvice`
- Обработка ошибок от Payment Service (проброс `message` из ответа)

## 🛠️ Быстрый старт

### Требования

- Docker
- Docker Compose

### Запуск через Docker Compose

```bash
docker compose up --build
```

Сервис будет доступен на порту **8085**.
Swagger path - `/swagger-ui.html`.

## 📚 Эндпоинты API

Базовый путь: `/api/v1/orders`

| Метод | Эндпоинт        | Описание                                  | Доступ                       |
|-------|-----------------|-------------------------------------------|------------------------------|
| GET   | `/get/{id}`     | Получить заказ по ID                      | Authenticated                |
| GET   | `/get/user`     | Получить все заказы пользователя          | Authenticated                |
| POST  | `/create`       | Создать заказ                             | Authenticated                |
| POST  | `/update`       | Обновить статус заказа                    | Internal (payment-service)   |

**Примечание:** Защищённые эндпоинты ожидают заголовок `X-User-Id`, который устанавливает Gateway.

## 📦 Структура проекта

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

## 🔒 Безопасность и надёжность

- **Заказ создаётся от имени аутентифицированного пользователя** (`X-User-Id` от Gateway).
- **Проверка владельца** при получении заказов пользователя.
- **Валидация позиций** - нельзя создать заказ с нулевым количеством или отрицательной ценой.
- **Внутренний эндпоинт `/update`** не должен публиковаться напрямую - только для Payment Service.
- **Сервис доверяет Gateway** в вопросе идентификации пользователя.

## 🩺 Health Checks

Сервис предоставляет эндпоинты Spring Boot Actuator:

| Эндпоинт                     | Назначение                   |
|------------------------------|------------------------------|
| `/actuator/health`           | Общий статус                 |
| `/actuator/health/liveness`  | Liveness probe               |
| `/actuator/health/readiness` | Readiness probe (включая БД) |
| `/actuator/info`             | Информация о сервисе         |

Готово. Теперь у тебя есть **русская версия** README для **order-service**.

Дальше по плану - **gateway** и **общий README**. Скажи, когда делать следующий.
