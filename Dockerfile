# Dockerfile

# 1단계: 빌드 환경의 JDK 버전을 21로 변경
FROM gradle:8.5-jdk21-alpine AS build

WORKDIR /home/gradle/src
COPY build.gradle settings.gradle ./
COPY gradle ./gradle
COPY src ./src
RUN gradle bootJar --no-daemon

# 2단계: 실행 환경의 JDK 버전도 21로 변경
FROM openjdk:21-slim

WORKDIR /app
COPY --from=build /home/gradle/src/build/libs/*.jar ./app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]