#!/bin/bash

echo "[ Spring Boot Application restart ]"

echo "[ Stop Application ]"
PID=$(pgrep -f emobin-0.0.1-SNAPSHOT.jar)
if [ -n "$PID" ]; then
  kill -9 $PID
  echo "Stopped process: $PID"
else
  echo "No existing process running."
fi

sleep 2

echo "[ Start Application ]"
nohup java -jar -Dspring.profiles.active=prod emobin-0.0.1-SNAPSHOT.jar > app.log 2>&1 &

echo "[Success] App started. Use: tail -f app.log"
