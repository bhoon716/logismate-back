# 1) Gradle 빌드
FROM gradle:7.6-jdk17 AS builder
WORKDIR /app
COPY . /app
RUN gradle clean build -x test

# 2) 런타임
FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app
COPY --from=builder /app/build/libs/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
