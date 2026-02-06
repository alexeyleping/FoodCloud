# FoodCloud - Менторинг проект

## Цель проекта
Построить микросервисную систему доставки еды на Spring Cloud, чтобы на практике изучить:
- Spring Cloud Gateway (API Gateway)
- Spring Cloud Netflix Eureka (Service Discovery)
- Spring Cloud Config (Централизованная конфигурация)
- Spring Cloud OpenFeign (Декларативный REST клиент)
- Spring Cloud LoadBalancer (Балансировка нагрузки)
- Resilience4j (Circuit Breaker, Rate Limiter, Retry)
- Micrometer Tracing (Распределённая трассировка)
- Apache Kafka (Асинхронное взаимодействие)
- Prometheus + Grafana (Мониторинг и метрики)
- JUnit 5 + MockMvc + WireMock (Тестирование)
- Testcontainers (Интеграционные тесты)

## Процесс разработки

### Роли
- **Ментор (Claude)**: даёт задания, теорию, делает код-ревью
- **Разработчик (Alexey)**: пишет код самостоятельно

### Формат задания
1. **Теория** - зачем это нужно, как работает
2. **Задание** - конкретное описание что сделать
3. **Acceptance Criteria** - критерии приёмки
4. **Hints** - подсказки (опционально)

### Процесс
1. Ментор даёт теорию и задание
2. Разработчик пишет код
3. Разработчик говорит "готово" или задаёт вопросы
4. Ментор делает код-ревью
5. Если есть замечания - итерация
6. Задание закрыто - переход к следующему

### Принципы кода
- Код должен быть покрываем тестами (dependency injection, интерфейсы)
- Маленькие методы с одной ответственностью
- Понятные имена переменных и методов
- Без магических чисел и строк

---

## Архитектура

```
[Client] --> [API Gateway :8080] --> [restaurant-service :8081]
                                 --> [order-service :8082]
                                 --> [delivery-service :8083]

[Eureka Server :8761]  <-- все сервисы регистрируются
[Config Server :8888]  <-- централизованная конфигурация
```

### Микросервисы
- **restaurant-service** — рестораны и меню (CRUD)
- **order-service** — управление заказами
- **delivery-service** — отслеживание доставки
- **api-gateway** — Spring Cloud Gateway
- **discovery-server** — Eureka Server
- **config-server** — Spring Cloud Config Server

### Технологии
- **Java 21** — язык
- **Spring Boot 3.x** — фреймворк
- **Spring Cloud 2023.x+** — Gateway, Eureka, Config, OpenFeign, LoadBalancer
- **Resilience4j** — Circuit Breaker, Rate Limiter, Retry
- **Gradle** — система сборки
- **H2 / PostgreSQL** — база данных
- **JUnit 5 + MockMvc + WireMock** — тестирование

---

## План заданий

### Задание #1 - Restaurant Service (Spring Boot REST + JPA)
**Статус**: Выполнено
**Что изучаем**: Spring Boot, Spring Data JPA, REST API, H2 database
**Описание**: Создать первый микросервис — restaurant-service с CRUD для ресторанов и меню

---

### Задание #2 - Eureka Discovery Server
**Статус**: Выполнено
**Что изучаем**: Spring Cloud Netflix Eureka Server, Service Registry
**Описание**: Создать отдельный сервис Eureka Server для регистрации и обнаружения микросервисов

---

### Задание #3 - Регистрация restaurant-service в Eureka
**Статус**: Выполнено
**Что изучаем**: Eureka Client, Service Discovery, аннотация `@EnableDiscoveryClient`
**Описание**: Подключить restaurant-service как Eureka Client, чтобы он регистрировался в Discovery Server

---

### Задание #4 - API Gateway (Spring Cloud Gateway)
**Статус**: Выполнено
**Что изучаем**: Spring Cloud Gateway, основы маршрутизации
**Описание**: Создать сервис api-gateway на Spring Cloud Gateway, настроить базовую маршрутизацию

---

### Задание #5 - Настройка маршрутов в Gateway
**Статус**: Выполнено
**Что изучаем**: Route Predicates, Filters, `lb://` URI для интеграции с Eureka
**Описание**: Настроить маршруты через Gateway к restaurant-service с использованием Service Discovery

---

### Задание #6 - Order Service
**Статус**: Выполнено
**Что изучаем**: Второй микросервис, связь между сервисами, проектирование API
**Описание**: Создать order-service для управления заказами, зарегистрировать в Eureka, добавить маршрут в Gateway

---

### Задание #7 - Межсервисное взаимодействие (OpenFeign)
**Статус**: Выполнено
**Что изучаем**: Spring Cloud OpenFeign, декларативный REST клиент, `@FeignClient`
**Описание**: Из order-service вызывать restaurant-service через OpenFeign для проверки ресторана и меню при создании заказа

---

### Задание #8 - Load Balancing (несколько инстансов)
**Статус**: Выполнено
**Что изучаем**: Spring Cloud LoadBalancer, запуск нескольких инстансов, балансировка через Eureka
**Описание**: Запустить несколько инстансов restaurant-service и убедиться, что Gateway и OpenFeign балансируют нагрузку

---

### Задание #9 - Spring Cloud Config Server
**Статус**: Выполнено
**Что изучаем**: Централизованная конфигурация, Config Server, Git-backed конфигурация
**Описание**: Создать config-server и перенести конфигурацию сервисов в централизованное хранилище

---

### Задание #10 - Circuit Breaker в Gateway (Resilience4j)
**Статус**: Выполнено
**Что изучаем**: Resilience4j CircuitBreaker, интеграция с Spring Cloud Gateway, fallback
**Описание**: Добавить Circuit Breaker в Gateway для защиты от каскадных сбоев при падении сервисов

---

### Задание #11 - Rate Limiting в Gateway
**Статус**: Выполнено
**Что изучаем**: RequestRateLimiter фильтр, Redis / in-memory rate limiting
**Описание**: Настроить Rate Limiting на уровне Gateway для ограничения количества запросов

---

### Задание #12 - Custom Gateway Filters (логирование, auth)
**Статус**: Выполнено
**Что изучаем**: GatewayFilter, GlobalFilter, Pre/Post фильтры, JWT авторизация
**Описание**: Написать кастомные фильтры для Gateway: логирование запросов, проверка JWT токена

---

### Задание #13 - Delivery Service + Distributed Tracing
**Статус**: Выполнено
**Что изучаем**: Micrometer Tracing, Zipkin, correlation ID, трассировка через несколько сервисов
**Описание**: Создать delivery-service и настроить распределённую трассировку для отслеживания запроса через всю цепочку сервисов

---

### Задание #14 - Retry и Fallback (Resilience4j)
**Статус**: Выполнено
**Что изучаем**: Resilience4j Retry, TimeLimiter, Fallback методы, `@CircuitBreaker` аннотация
**Описание**: Добавить Retry и Fallback логику в межсервисные вызовы для graceful degradation

---

### Задание #15 - Docker Compose для всего стека
**Статус**: Выполнено
**Что изучаем**: Docker, Dockerfile, docker-compose, Spring profiles, контейнеризация
**Описание**: Контейнеризировать все сервисы и поднять весь стек одной командой `docker-compose up`

---

### Задание #16 - Apache Kafka (асинхронное взаимодействие)
**Статус**: Не начато
**Что изучаем**: Apache Kafka, Spring Kafka, Producer/Consumer, Topics, асинхронные события
**Описание**: Перевести часть межсервисного взаимодействия на событийную модель. При создании заказа order-service публикует событие `OrderCreated` в Kafka, delivery-service подписывается и автоматически создаёт доставку. При смене статуса доставки delivery-service публикует `DeliveryStatusChanged`, order-service обновляет статус заказа.

---

### Задание #17 - Мониторинг (Prometheus + Grafana)
**Статус**: Не начато
**Что изучаем**: Micrometer Metrics, Prometheus, Grafana, Spring Boot Actuator, кастомные метрики, дашборды
**Описание**: Настроить сбор метрик со всех сервисов через Prometheus и визуализацию в Grafana. Добавить кастомные метрики (количество заказов, среднее время обработки). Создать дашборд с ключевыми показателями системы.

---

### Задание #18 - Unit и интеграционные тесты (JUnit 5 + MockMvc + WireMock)
**Статус**: Не начато
**Что изучаем**: JUnit 5, Mockito, MockMvc, WireMock, @WebMvcTest, @DataJpaTest, тестовые слайсы Spring Boot
**Описание**: Написать тесты для restaurant-service и order-service. Unit-тесты для сервисного слоя (Mockito), интеграционные тесты для контроллеров (MockMvc), мокирование внешних сервисов (WireMock для Feign-клиентов).

---

### Задание #19 - Testcontainers (интеграционные тесты с реальной инфраструктурой)
**Статус**: Не начато
**Что изучаем**: Testcontainers, PostgreSQL в тестах, Kafka в тестах, Redis в тестах, @DynamicPropertySource
**Описание**: Заменить H2 на PostgreSQL через Testcontainers для интеграционных тестов. Написать end-to-end тесты с реальными Kafka, Redis и PostgreSQL в Docker-контейнерах, которые поднимаются автоматически при запуске тестов.

---

## Текущий статус
- **Фаза**: Async & Observability
- **Последнее задание**: #15 - Docker Compose для всего стека
- **Следующий шаг**: Задание #16 - Apache Kafka (асинхронное взаимодействие)
