#!/usr/bin/env bash

PROJECT_ROOT="/home/ubuntu/app"
수="prod"

# -plain.jar가 아닌 실제 실행 가능한 .jar 파일 중 가장 최근 파일 1개 찾기
JAR_FILE=$(ls -tr $PROJECT_ROOT/*.jar | grep -v 'plain\.jar$' | tail -n 1)

APP_LOG="$PROJECT_ROOT/application.log"
ERROR_LOG="$PROJECT_ROOT/error.log"
DEPLOY_LOG="$PROJECT_ROOT/deploy.log"

TIME_NOW=$(date +%c)

# jar 파일 실행
echo "$TIME_NOW > $JAR_FILE 파일 실행" >> $DEPLOY_LOG
nohup java -jar -Dspring.profiles.active=$SPRING_PROFILE $JAR_FILE > $APP_LOG 2> $ERROR_LOG &

CURRENT_PID=$(pgrep -f $JAR_FILE)
echo "$TIME_NOW > 실행된 프로세스 아이디 $CURRENT_PID 입니다." >> $DEPLOY_LOG
