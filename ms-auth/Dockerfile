FROM maven:3.9-eclipse-temurin-17 AS builder
WORKDIR /build
COPY pom.xml ./
COPY ms-auth/pom.xml ms-auth/
RUN mvn -f ms-auth/pom.xml dependency:go-offline -q
COPY ms-auth/src ms-auth/src
RUN mvn -f ms-auth/pom.xml package -DskipTests -q

FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
RUN addgroup -S appgroup && adduser -S appuser -G appgroup
USER appuser
COPY --from=builder /build/ms-auth/target/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
