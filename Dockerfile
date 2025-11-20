# Java 21 JDK 이미지를 사용하여 빌드 환경을 구성
FROM eclipse-temurin:21-jdk-jammy AS builder

# 작업 디렉토리 설정
WORKDIR /app

# Gradle Wrapper, 설정 파일, 빌드 스크립트 복사
COPY gradlew .
COPY gradle gradle
COPY build.gradle .
COPY settings.gradle .

# 소스 코드를 제외한 의존성만 미리 다운로드하여 빌드 캐시를 활용
RUN ./gradlew dependencies

# 소스 코드 복사
COPY src src

# 애플리케이션 빌드: 실행 가능한 JAR 파일 (bootJar) 생성
RUN ./gradlew bootJar -x test

#JRE (Java Runtime Environment)만 포함된 경량 이미지를 사용하여 최종 이미지 크기를 최소화
FROM eclipse-temurin:21-jre-jammy

# 환경 변수 설정 (시간대 설정)
ENV TZ Asia/Seoul

# 빌드 단계에서 생성된 JAR 파일을 최종 이미지로 복사
COPY --from=builder /app/build/libs/*.jar app.jar

# 애플리케이션 실행 명령어 정의
# 이 명령어를 통해 Spring Boot 애플리케이션이 시작
ENTRYPOINT ["java", "-jar", "app.jar"]