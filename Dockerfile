# 1단계: Java 21이 설치된 OpenJDK 베이스 이미지 사용
FROM gradle:8.4.0-jdk21 AS builder

COPY --chown=gradle:gradle . /app
WORKDIR /app
RUN gradle build --no-daemon

# 2단계: 실행용 이미지 (최소화)
FROM eclipse-temurin:21-jre

WORKDIR /app
COPY --from=builder /app/build/libs/*.jar app.jar

CMD ["java", "-jar", "app.jar"]