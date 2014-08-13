from datetime import datetime as dt
from subprocess import Popen, PIPE

from flask import jsonify

from app import app
from auth import has_auth_key, has_username
from models import LogAction


def send_command(command):
    log_action(command)

    if app.config['DEBUG']:
        response = 'error'
    else:
        p = Popen(['cd ../SlotMachienPC/src && ' +
                   'java -cp /opt/leJOS_NXJ/lib/pc/pccomm.jar:. PCMain ' +
                   command], stdin=PIPE, stdout=PIPE, shell=True)
        rc = p.wait(timeout=10)  # 10 seconds
        output, error = p.communicate()
        if rc == 0 and len(output) > 0:
            # success
            response = output.strip()
        else:
            response = 'error'

    return jsonify({'status': response})


def log_action(action):
    LogAction.create(auth_key=has_auth_key(), user=has_username(), action=action, logged_on=dt.now())
