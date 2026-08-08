#!/usr/bin/env bash

PROJECT_ROOT="/home/ubuntu/app"

# 1. for 루프로 최신 JAR 파일 탐색 (-plain.jar 제외)
JAR_FILE=""
for file in "$PROJECT_ROOT"/*.jar; do
  if [ -f "$file" ] && [[ "$file" != *-plain.jar ]]; then
    if [ -z "$JAR_FILE" ] || [ "$file" -nt "$JAR_FILE" ]; then
      JAR_FILE="$file"
    fi
  fi
done

DEPLOY_LOG="$PROJECT_ROOT/deploy.log"
TIME_NOW=$(date +%c)

# 2. 순수 파일명으로 현재 구동 중인 PID 확인
JAR_NAME=$(basename "$JAR_FILE")
CURRENT_PID=$(pgrep -f "$JAR_NAME")

# 3. 프로세스 존재 여부 검사 (큰따옴표 "$CURRENT_PID" 필수!)
if [ -z "$CURRENT_PID" ]; then
  echo "$TIME_NOW > 현재 실행 중인 애플리케이션이 없습니다." >> "$DEPLOY_LOG"
else
  echo "$TIME_NOW > 실행 중인 프로세스($CURRENT_PID) 종료" >> "$DEPLOY_LOG"
  kill -15 "$CURRENT_PID"
  sleep 3
fi
