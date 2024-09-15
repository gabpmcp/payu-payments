# Stage 1: Build the Kotlin app
FROM gradle:8.9-jdk21-alpine AS build

WORKDIR /app
COPY build.gradle.kts /app/
COPY src /app/src

# Usar Gradle para compilar el proyecto Kotlin
RUN gradle build --no-daemon --no-watch-fs --refresh-dependencies

# Stage 2: Run the Kotlin app
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app
COPY --from=build /app/build/libs/payments-application.jar /app/payments-application.jar

# Especificar el puerto en el que la aplicación escucha
EXPOSE 8080

# Ejecutar la aplicación con ZGC para optimización de memoria
ENTRYPOINT ["java", "-XX:+UseZGC", "-Xms512m", "-Xmx512m", "-jar", "/app/payments-application.jar"]
