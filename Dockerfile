# 1. 빌드 이미지
FROM amazoncorretto:17 AS builder

# 작업 디렉토리 설정
WORKDIR /app

# 파일 복사
COPY . .

# Gradle Wrapper를 사용한 애플리케이션 빌드
RUN chmod +x ./gradlew && ./gradlew clean build -x test --no-daemon

# 2. 실행 전용 이미지
FROM amazoncorretto:17

WORKDIR /app

ARG PROJECT_NAME=discodeit
ARG PROJECT_VERSION=1.2-M8
COPY --from=builder /app/build/libs/${PROJECT_NAME}-${PROJECT_VERSION}.jar app.jar


# 환경 변수 설정
ENV JVM_OPTS=""

# 80 포트 노출
EXPOSE 80

# 애플리케이션 실행
CMD ["sh", "-c", "java $JVM_OPTS -jar app.jar --spring.profiles.active=prod --server.port=80"]
