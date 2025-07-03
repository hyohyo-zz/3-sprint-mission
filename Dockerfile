# 베이스 이미지
FROM amazoncorretto:17

# 작업 디렉토리 설정
WORKDIR /app

# 파일 복사
COPY . .

# Gradle Wrapper를 사용한 애플리케이션 빌드
RUN ./gradlew clean build --no-daemon

# 환경 변수 설정
ENV PROJECT_NAME=discodeit \
    PROJECT_VERSION=1.2-M8 \
    JVM_OPTS=""

# 80 포트 노출
EXPOSE 80

# 애플리케이션 실행
CMD ["sh", "-c", "java $JVM_OPTS -jar build/libs/$PROJECT_NAME-$PROJECT_VERSION.jar"]
