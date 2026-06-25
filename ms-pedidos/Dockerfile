FROM maven:3.9-eclipse-temurin-17 AS builder
WORKDIR /build
COPY pom.xml ./
COPY ms-pedidos/pom.xml ms-pedidos/
RUN mvn -f ms-pedidos/pom.xml dependency:go-offline -q
COPY ms-pedidos/src ms-pedidos/src
RUN mvn -f ms-pedidos/pom.xml package -DskipTests -q

FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
RUN addgroup -S appgroup && adduser -S appuser -G appgroup
USER appuser
COPY --from=builder /build/ms-pedidos/target/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
