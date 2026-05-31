FROM maven:3.9-eclipse-temurin-21 AS builder
WORKDIR /workspace
COPY . .
RUN --mount=type=cache,target=/root/.m2 mvn -pl auth-service -am -B -DskipTests clean package

FROM eclipse-temurin:21-jre-noble AS runtime
WORKDIR /app
RUN apt-get update \
    && apt-get install -y --no-install-recommends curl \
    && groupadd -r app \
    && useradd -r -g app -u 2000 -s /usr/sbin/nologin -d /nonexistent app \
    && mkdir -p /app \
    && chown -R app:app /app \
    && rm -rf /var/lib/apt/lists/*
COPY --from=builder --chown=2000:2000 /workspace/auth-service/target/*.jar /app/app.jar
USER 2000:2000

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app/app.jar"]
