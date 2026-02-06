# ========== Stage 1: BUILD ==========
# Берём образ с Gradle и JDK 21 — он умеет собирать наш проект
FROM gradle:8-jdk21 AS builder

# Рабочая директория внутри контейнера (как cd /app)
WORKDIR /app

# Копируем ВСЕ файлы проекта внутрь контейнера
# (gradle файлы, src, config-repo — всё)
COPY . .

# Аргумент сборки — какой модуль собирать
# Передаётся при сборке: docker build --build-arg SERVICE_NAME=api-gateway
ARG SERVICE_NAME

# Собираем JAR для конкретного модуля
# bootJar — задача Spring Boot, создаёт исполняемый JAR со всеми зависимостями
# --no-daemon — не запускать Gradle daemon (в контейнере он не нужен)
RUN gradle :${SERVICE_NAME}:bootJar --no-daemon

# ========== Stage 2: RUN ==========
# Берём лёгкий образ только с JRE (без компилятора, без Gradle)
# eclipse-temurin — официальный OpenJDK образ
FROM eclipse-temurin:21-jre

# Устанавливаем curl — он нужен для healthcheck в docker-compose
# (Docker проверяет готовность сервиса через curl http://localhost/actuator/health)
RUN apt-get update && apt-get install -y curl && rm -rf /var/lib/apt/lists/*

WORKDIR /app

# Снова объявляем ARG — в каждом stage аргументы сбрасываются
ARG SERVICE_NAME

# Копируем собранный JAR из первого stage (builder)
# Всё остальное (исходники, Gradle) — выбрасывается
COPY --from=builder /app/${SERVICE_NAME}/build/libs/*.jar app.jar

# Запускаем приложение
# ENTRYPOINT — команда, которая выполнится при старте контейнера
ENTRYPOINT ["java", "-jar", "app.jar"]
