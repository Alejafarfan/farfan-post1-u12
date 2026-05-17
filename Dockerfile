# ── Etapa 1: compilación con Maven ────────────────────────────────
FROM eclipse-temurin:21-jdk-alpine AS builder
WORKDIR /app

# pom.xml primero para aprovechar caché de capas
COPY pom.xml .
COPY mvnw .
COPY .mvn .mvn
RUN ./mvnw dependency:go-offline -q

COPY src ./src
RUN ./mvnw clean package -DskipTests -q

# ── Etapa 2: imagen de producción (solo JRE) ───────────────────────
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Usuario no root: buena práctica de seguridad
RUN addgroup -S spring && adduser -S spring -G spring
USER spring

COPY --from=builder /app/target/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]