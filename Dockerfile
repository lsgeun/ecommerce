FROM eclipse-temurin:17-jre

WORKDIR /app

# 1. 보안용 계정 미리 생성
RUN groupadd -r appuser && useradd -r -g appuser appuser

# 2. 파일 복사 시점에 소유권(--chown)을 한 번에 부여 (추가 RUN chown 레이어 제거)
COPY --chown=appuser:appuser build/libs/*-SNAPSHOT.jar app.jar

# 3. 비-root 계정으로 전환
USER appuser

# 4. 환경 변수 설정 (기본값 prod, 외부에서 전달 가능)
ENV SPRING_PROFILES_ACTIVE=prod
ENV JAVA_OPTS=""

# 5. ENTRYPOINT에서 환경 변수 및 옵션 조합
ENTRYPOINT ["sh", "-c", "exec java $JAVA_OPTS -Dspring.profiles.active=$SPRING_PROFILES_ACTIVE -jar app.jar"]
