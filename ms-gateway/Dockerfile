FROM maven:3.9-eclipse-temurin-17 AS builder
WORKDIR /build
COPY pom.xml ./
COPY ms-gateway/pom.xml ms-gateway/
RUN mvn -f ms-gateway/pom.xml dependency:go-offline -q
COPY ms-gateway/src ms-gateway/src
RUN mvn -f ms-gateway/pom.xml package -DskipTests -q

FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
RUN addgroup -S appgroup && adduser -S appuser -G appgroup
USER appuser
COPY --from=builder /build/ms-gateway/target/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
