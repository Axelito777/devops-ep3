FROM maven:3.9-eclipse-temurin-17 AS builder
WORKDIR /build
COPY pom.xml ./
COPY ms-eureka/pom.xml ms-eureka/
RUN mvn -f ms-eureka/pom.xml dependency:go-offline -q
COPY ms-eureka/src ms-eureka/src
RUN mvn -f ms-eureka/pom.xml package -DskipTests -q

FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
RUN addgroup -S appgroup && adduser -S appuser -G appgroup
USER appuser
COPY --from=builder /build/ms-eureka/target/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
