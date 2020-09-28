#!/bin/bash

PROG=jpower-auth-exec
SERVER_NAME=jpower-auth
PIDFILE=/root/data/app/jpower-auth/${PROG}.pid

export JAVA_HOME=/data/jdk1.8.0_231
export JRE_HOME=$JAVA_HOME/jre

status() {
    if [ -f $PIDFILE ]; then
        PID=$(cat $PIDFILE)
        if [ ! -x /proc/${PID} ]; then
            return 1
        else
            return 0
        fi
    else
        return 1
    fi
}

case "$1" in
    start)
        status
        RETVAL=$?
        if [ $RETVAL -eq 0 ]; then
            echo "$PIDFILE exists, process is already running or crashed"
            exit 1
        fi

        echo "Starting $PROG ..."

	export SW_AGENT_COLLECTOR_BACKEND_SERVICES=219.148.186.214:11800
	export SW_AGENT_NAME=${SERVER_NAME}	

        nohup java -javaagent:/root/config/skywalking/skywalking-apm/agent/skywalking-agent.jar -server -XX:+DisableExplicitGC -XX:+UseParNewGC  -Djava.awt.headless=true -Dspring.profiles.active=test -Dserver.port=80 -jar ${PROG}.jar > logs/log.log 2>&1 &
        RETVAL=$?
        if [ $RETVAL -eq 0 ]; then
            echo "$PROG is started"
            echo $! > $PIDFILE
            exit 0
        else
            echo "Stopping $PROG"
            rm -f $PIDFILE
            exit 1
        fi
        ;;
    stop)
        status
        RETVAL=$?
        if [ $RETVAL -eq 0 ]; then
            echo "Shutting down $PROG"
            kill `cat $PIDFILE`
            RETVAL=$?
            if [ $RETVAL -eq 0 ]; then
                rm -f $PIDFILE
            else
                echo "Failed to stopping $PROG"
            fi
        fi
        ;;
    status)
        status
        RETVAL=$?
        if [ $RETVAL -eq 0 ]; then
            PID=$(cat $PIDFILE)
            echo "$PROG is running ($PID)"
        else
            echo "$PROG is not running"
        fi
        ;;
    restart)
        $0 stop
        $0 start
        ;;
    *)
        echo "Usage: $0 {start|stop|restart|status}"
        ;;
esac
