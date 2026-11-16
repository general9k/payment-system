# Payment System

Pet-проект: банковское приложение на микросервисной архитектуре. Переводы между счетами строятся на событийном взаимодействии через Apache Kafka (Saga-хореография), с резервированием средств, идемпотентностью и уведомлениями.

Цель проекта: показать на практике промышленный стек: Spring Boot, Kafka, Redis, PostgreSQL + Liquibase, Testcontainers, CI/CD, метрики.

## Стек

| Область | Технологии |
|---|---|
| Язык / фреймворк | Java 21, Spring Boot 3.4 |
| Сборка | Maven (multi-module) |
| БД | PostgreSQL 16, Liquibase, Spring Data JPA / Hibernate |
| Сообщения | Apache Kafka (KRaft), Spring Kafka |
| Кэш / координация | Redis 7 |
| Контракты | OpenAPI 3 (contract-first, генерация интерфейсов контроллеров), springdoc |
| Инфраструктура | Docker Compose, Redpanda Console |
| CI | GitHub Actions |

## Структура репозитория

```
payment-system/
├── api-contracts/        # OpenAPI-спецификации + кодогенерация API-интерфейсов и моделей
├── starter/              # общий стартер: глобальная обработка ошибок, LogicException/ErrorCode
├── account-service/      # счета, балансы, холды (резервирование), журнал операций
├── payment-service/      # платежи, оркестрация перевода (Saga)
├── notification-service/ # уведомления и настройки каналов
└── docker-compose.yml    # Kafka, Redpanda Console, Redis, 3 × PostgreSQL
```

## Сервисы

| Сервис | Порт | База данных | Назначение |
|---|---|---|---|
| account-service | 8082 | `localhost:5432` | Счета, баланс, холды, история операций |
| payment-service | 8083 | `localhost:5433` | Создание платежей, статусы, Saga |
| notification-service | 8084 | `localhost:5434` | История уведомлений, настройки каналов |

Каждый сервис владеет собственной базой (database-per-service), схема управляется Liquibase.

### REST API

Swagger UI доступен у каждого сервиса по адресу `/swagger-ui.html`.

**account-service**

| Метод | Путь | Описание |
|---|---|---|
| POST | `/api/v1/accounts` | Создать счёт |
| GET | `/api/v1/accounts` | Список счетов |
| GET | `/api/v1/accounts/{accountId}` | Получить счёт |
| PATCH | `/api/v1/accounts/{accountId}/balance` | Изменить баланс |
| POST | `/api/v1/accounts/{accountId}/holds` | Зарезервировать средства |
| PATCH | `/api/v1/holds/{holdId}/confirm` | Подтвердить холд (списание) |
| PATCH | `/api/v1/holds/{holdId}/release` | Снять холд |
| GET | `/api/v1/accounts/{accountId}/transactions` | История операций |

**payment-service**

| Метод | Путь | Описание |
|---|---|---|
| POST | `/api/v1/payments` | Создать платёж |
| GET | `/api/v1/payments/{paymentId}` | Статус платежа |

**notification-service**

| Метод | Путь | Описание |
|---|---|---|
| GET | `/api/v1/notifications` | История уведомлений |
| GET | `/api/v1/preferences/{clientId}` | Настройки каналов клиента |
| PUT | `/api/v1/preferences/{clientId}` | Обновить настройки каналов |

## Архитектура перевода (Saga)

```
payment-service                 account-service               notification-service
      │                               │                               │
 POST /payments                       │                               │
      │── payment.initiated ─────────▶│                               │
      │                        резерв средств (hold)                  │
      │◀──── funds.reserved ──────────│                               │
      │      funds.rejected           │                               │
      │── payment.completed ─────────▶│ подтверждение списания        │
      │── payment.completed / failed ─────────────────────────────────▶│
      │── payment.compensate ────────▶│ снятие холда (компенсация)    │
```

Ключевые решения:

- **Резервирование через холды**: доступный баланс = баланс минус активные холды.
- **Конкурентные списания**: пессимистичная блокировка строки счёта (`SELECT ... FOR UPDATE`).
- **Надёжная публикация событий**: Outbox pattern, событие пишется в БД в одной транзакции с сущностью.
- **Обработка ошибок Kafka**: manual ack, retry, error-топики (DLT).
- **Идемпотентность**: Redis (`SETNX` + TTL) по ключу идемпотентности.

## Статус реализации

- [x] Multi-module Maven, OpenAPI-контракты с кодогенерацией
- [x] Общий starter (обработка ошибок)
- [x] Docker Compose: Kafka (KRaft), Redpanda Console, Redis, 3 × PostgreSQL
- [x] Liquibase-миграции всех трёх сервисов
- [x] account-service: счета, балансы, холды, журнал операций
- [ ] Модуль `event-contracts` (DTO Kafka-событий)
- [ ] Hold expiration (автоосвобождение просроченных холдов)
- [ ] account-service: Kafka producer/consumer, DLT, Redis-идемпотентность
- [ ] payment-service: сущности, API, Outbox, Saga
- [ ] notification-service: API, consumers, batch-дайджест, rate limiting (Redis)
- [ ] Модуль `test-starter` (Testcontainers) + unit/integration-тесты
- [ ] Prometheus + Grafana (метрики сервисов, consumer lag, hit-rate кэша)

## Быстрый старт

Требования: JDK 21, Maven 3.9+, Docker.

```bash
# 1. Поднять инфраструктуру
docker compose up -d

# 2. Собрать проект (генерация API из OpenAPI + сборка всех модулей)
mvn clean install

# 3. Запустить нужный сервис
mvn -pl account-service spring-boot:run
mvn -pl payment-service spring-boot:run
mvn -pl notification-service spring-boot:run
```

Полезные адреса:

| Что | Адрес |
|---|---|
| Redpanda Console (просмотр топиков Kafka) | http://localhost:8081 |
| Swagger UI account-service | http://localhost:8082/swagger-ui.html |
| Swagger UI payment-service | http://localhost:8083/swagger-ui.html |
| Swagger UI notification-service | http://localhost:8084/swagger-ui.html |

Сервисы запускаются на хосте (из IDE или через Maven) и подключаются к инфраструктуре из Docker по `localhost`.

## Планы

- Метрики через Micrometer + Prometheus, дашборды в Grafana
- Интеграционные тесты на Testcontainers (Postgres, Kafka, Redis), общий базовый класс в `test-starter`
- Контейнеризация самих сервисов в Docker Compose
