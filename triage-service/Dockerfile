FROM openjdk:21-jdk-slim

WORKDIR /app

COPY target/triagem-service-1.0.0-SNAPSHOT.jar app.jar

EXPOSE 8201

ENTRYPOINT ["java", "-jar", "app.jar"]
