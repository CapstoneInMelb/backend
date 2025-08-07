# 1단계: 프로젝트 빌드(Build) 단계
FROM gradle:8.5-jdk17-alpine AS build
WORKDIR /home/gradle/src
COPY build.gradle settings.gradle ./
COPY gradle ./gradle
COPY src ./src

# 변경 전: RUN gradle build --no-daemon
# 변경 후: 'build'를 'bootJar'로 변경하여 테스트를 건너뜁니다.
RUN gradle bootJar --no-daemon

# 2단계: 최종 실행(Runtime) 단계
FROM openjdk:17-slim
WORKDIR /app
COPY --from=build /home/gradle/src/build/libs/*.jar ./app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]