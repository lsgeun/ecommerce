#!/usr/bin/env bash

PROJECT_ROOT="/home/ubuntu/app"
DEPLOY_LOG="$PROJECT_ROOT/deploy.log"
TIME_NOW=$(date +%c)

cd "$PROJECT_ROOT" || exit 1

rm -f "$DEPLOY_LOG"

echo "$TIME_NOW > Docker Compose 컨테이너 중지 시작" >> "$DEPLOY_LOG"

# 실행 중인 Docker Compose 컨테이너 및 관련 네트워크 정리
docker compose down >> "$DEPLOY_LOG" 2>&1

echo "$TIME_NOW > Docker Compose 컨테이너 중지 완료" >> "$DEPLOY_LOG"
