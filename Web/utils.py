from datetime import datetime as dt
import subprocess

from flask import jsonify

from settings import *


def send_command(command):
    log_action(command)
    if settings.DEBUG:
        return jsonfiy({'status': 'error'})
    if command == 'status':
        return jsonify({'status': 'error'})
    print(command)

    subprocess.call(['cd ../SlotMachienPC/src && sudo java -classpath /opt/leJOS_NXJ/lib/pc/pccomm.jar:. PCMain ' + command], shell=True)
    return jsonify({'status': command})


def log_action(action):
    LogAction.create(auth_key=auth_key(), action=action, logged_on=dt.now())
