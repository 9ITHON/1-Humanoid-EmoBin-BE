#!/bin/bash

JAR_PATH="/home/ubuntu/deploy/build/libs/emobin-0.0.1-SNAPSHOT.jar"
LOG_DIR="/home/ubuntu/deploy/logs"
LOG_FILE="$LOG_DIR/app.log"

echo "[ Spring Boot Application restart ]"

# JAR 존재 확인
if [ ! -f "$JAR_PATH" ]; then
  echo "[ERROR] JAR 파일이 존재하지 않습니다: $JAR_PATH"
  exit 1
fi

# java 확인
if ! command -v java &> /dev/null; then
  echo "[ERROR] java 명령을 찾을 수 없습니다."
  exit 1
fi

# 이전 프로세스 종료
echo "[ Stop Application ]"
PID=$(pgrep -f "$JAR_PATH")
if [ -n "$PID" ]; then
  kill -9 $PID || echo "프로세스 종료 실패"
  echo "종료된 프로세스: $PID"
else
  echo "실행 중인 프로세스 없음."
fi

sleep 2

# 로그 디렉토리 생성
mkdir -p "$LOG_DIR"

# 애플리케이션 실행
echo "[ Start Application ]"
nohup java -jar -Dspring.profiles.active=prod "$JAR_PATH" > "$LOG_FILE" 2>&1 &

sleep 3

# 로그 출력
if [ -f "$LOG_FILE" ]; then
  echo "[ 로그 출력 (최근 30줄) ]"
  tail -n 30 "$LOG_FILE"
else
  echo "[WARN] 로그 파일이 아직 생성되지 않았습니다: $LOG_FILE"
fi

echo "[Success] App started. Use: tail -f $LOG_FILE"

