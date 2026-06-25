FROM maven:3.9-eclipse-temurin-17 AS builder
WORKDIR /build
COPY pom.xml ./
COPY ms-inventario/pom.xml ms-inventario/
RUN mvn -f ms-inventario/pom.xml dependency:go-offline -q
COPY ms-inventario/src ms-inventario/src
RUN mvn -f ms-inventario/pom.xml package -DskipTests -q

FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
RUN addgroup -S appgroup && adduser -S appuser -G appgroup
USER appuser
COPY --from=builder /build/ms-inventario/target/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
