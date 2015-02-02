#!/bin/sh

### BEGIN INIT INFO
# Provides: SlotMachien
# Required-Start: $remote_fs $syslog
# Required-Stop: $remote_fs $syslog
# Default-Start: 2 3 4 5
# Default-Stop: 0 1 6
# Short-Description: SlotMachien Web
# Description: Web Interface for SlotMachien PC and NXT
### END INIT INFO

DIR=/home/slotmachien/slotmachien/Web
DAEMON=$DIR/slotmachien.fcgi
DAEMON_NAME=slotmachien

# This next line determines what user the script runs as.
# Root generally not recommended but necessary if you are using the Raspberry Pi GPIO from Python.
DAEMON_USER=www-data


# The process ID of the script when it runs is stored here:
PIDFILE=/var/run/$DAEMON_NAME.pid

. /lib/lsb/init-functions

do_start () {
    log_daemon_msg "Starting system $DAEMON_NAME daemon"
    rm -f /tmp/slotmachien.sock
    start-stop-daemon --start --background --pidfile $PIDFILE --make-pidfile --user $DAEMON_USER -g slotmachien \
    --startas /bin/bash -- -c "exec $DAEMON $DAEMON_ARGS > /var/log/slotmachien-error.log 2>&1"

    # ugly fix to make socket readable
    sleep 20
    chmod 777 /tmp/slotmachien.sock
    log_end_msg $?
}
do_stop () {
    log_daemon_msg "Stopping system $DAEMON_NAME daemon"
    start-stop-daemon --stop --pidfile $PIDFILE --retry 10
    log_end_msg $?
}

case "$1" in
    start|stop)
    do_${1}
    ;;

    restart|reload|force-reload)
    do_stop
    do_start
    ;;

    status)
    status_of_proc "$DAEMON_NAME" "$DAEMON" && exit 0 || exit $?
    ;;
    *)

    echo "Usage: /etc/init.d/$DEAMON_NAME {start|stop|restart|status}"
    exit 1
    ;;
esac
exit
