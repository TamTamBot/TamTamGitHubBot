#!/bin/bash
command=$1
filename=pid.txt
scriptFolred=./scripts/

function start {
  cd ..
  java -Dspring.config.location=file:config/ -jar testbot-0.3.jar &
  PID=$!
  echo $PID > "$scriptFolred$filename"
}

function stop {
  pid=$(head -n 1 $filename)
  kill "$pid"
}

function mainFunc {
  echo "Your command: ${command}"
  case $command in
  start)
    start
    ;;
    stop)
      stop
      rm $filename
      ;;
  restart)
    if test -f "$filename"; then
    stop
    fi
    start
    ;;
  *)
    echo "Sorry, command ${command} not found."
  esac
}

mainFunc
