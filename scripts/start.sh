#!/usr/bin/env bash

PROJECT_ROOT="/home/ubuntu/app"
DEPLOY_LOG="$PROJECT_ROOT/deploy.log"
TIME_NOW=$(date +%c)

cd "$PROJECT_ROOT" || exit 1

echo "$TIME_NOW > 최신 도커 이미지 다운로드(pull) 시작" >> "$DEPLOY_LOG"
# 1. Docker Hub에서 latest 이미지 최신본 다운로드
docker compose pull >> "$DEPLOY_LOG" 2>&1

echo "$TIME_NOW > Docker Compose 컨테이너 실행(up) 시작" >> "$DEPLOY_LOG"
# 2. 백그라운드로 컨테이너 재시작
docker compose up -d --remove-orphans >> "$DEPLOY_LOG" 2>&1

echo "$TIME_NOW > Docker Compose 실행 상태 확인" >> "$DEPLOY_LOG"
# 3. 컨테이너가 정상적으로 떠 있는지(State: Up) 확인 로그 남기기
docker compose ps >> "$DEPLOY_LOG" 2>&1

echo "$TIME_NOW > 미사용 댕글링 이미지(Dangling Images) 정리" >> "$DEPLOY_LOG"
docker image prune -f >> "$DEPLOY_LOG" 2>&1

echo "$TIME_NOW > 배포 완료" >> "$DEPLOY_LOG"
