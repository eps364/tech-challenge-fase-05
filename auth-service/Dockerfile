# Multi-stage build for Auth Service
FROM maven:3.9-eclipse-temurin-21 AS builder
WORKDIR /build
COPY pom.xml .
COPY auth-service/pom.xml ./auth-service/
RUN mvn dependency:go-offline -B
COPY . .
RUN mvn clean package -DskipTests -pl auth-service

# Runtime image
FROM openjdk:21-jdk-slim
WORKDIR /app
COPY --from=builder /build/auth-service/target/*.jar ./app.jar
EXPOSE 8080
HEALTHCHECK --interval=10s --timeout=3s --start-period=5s --retries=3 \
  CMD java -cp app.jar org.springframework.boot.loader.JarLauncher \
  -Dspring.profiles.active=health || exit 1
ENTRYPOINT ["java", "-jar", "app.jar"]

# Development stage
FROM openjdk:21-jdk-slim AS dev
WORKDIR /workspace
RUN apt-get update && apt-get install -y maven && rm -rf /var/lib/apt/lists/*
COPY . .
EXPOSE 8080
CMD ["mvn", "spring-boot:run", "-pl", "auth-service", "-Dspring-boot.devtools.restart.enabled=true"]
