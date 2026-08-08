#!/usr/bin/env bash

PROJECT_ROOT="/home/ubuntu/app"
SPRING_PROFILE="prod"

# 1. for 루프로 최신 JAR 파일 탐색 (-plain.jar 제외)
JAR_FILE=""
for file in "$PROJECT_ROOT"/*.jar; do
  if [ -f "$file" ] && [[ "$file" != *-plain.jar ]]; then
    if [ -z "$JAR_FILE" ] || [ "$file" -nt "$JAR_FILE" ]; then
      JAR_FILE="$file"
    fi
  fi
done

APP_LOG="$PROJECT_ROOT/application.log"
ERROR_LOG="$PROJECT_ROOT/error.log"
DEPLOY_LOG="$PROJECT_ROOT/deploy.log"

TIME_NOW=$(date +%c)

# 2. JAR 파일 실행 (모든 변수에 큰따옴표 적용)
echo "$TIME_NOW > $JAR_FILE 파일 실행" >> "$DEPLOY_LOG"
nohup java -jar -Dspring.profiles.active="$SPRING_PROFILE" "$JAR_FILE" > "$APP_LOG" 2> "$ERROR_LOG" &

# 3. 프로세스 구동 대기 (2초)
sleep 2

# 4. 파일명만 추출하여 안전하게 PID 확인
JAR_NAME=$(basename "$JAR_FILE")
CURRENT_PID=$(pgrep -f "$JAR_NAME")

echo "$TIME_NOW > 실행된 프로세스 아이디: $CURRENT_PID" >> "$DEPLOY_LOG"
