FROM maven:3.9-eclipse-temurin-17 AS builder
WORKDIR /build
COPY pom.xml ./
COPY ms-clientes/pom.xml ms-clientes/
RUN mvn -f ms-clientes/pom.xml dependency:go-offline -q
COPY ms-clientes/src ms-clientes/src
RUN mvn -f ms-clientes/pom.xml package -DskipTests -q

FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
RUN addgroup -S appgroup && adduser -S appuser -G appgroup
USER appuser
COPY --from=builder /build/ms-clientes/target/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
