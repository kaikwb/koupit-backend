FROM maven:3-eclipse-temurin-21 AS builder

WORKDIR /app

COPY . .

RUN mvn package

FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

COPY --from=builder /app/target/*.jar /app/app.jar

ENV KEYCLOAK_HOST=localhost:8081

EXPOSE 8080

CMD ["java", "-jar", "app.jar"]
