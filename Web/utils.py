from datetime import datetime as dt
import subprocess

from flask import jsonify

from app import app
from auth import has_auth_key
from models import LogAction


def send_command(command):
    log_action(command)

    if not app.config['DEBUG']:
        subprocess.call(['cd ../SlotMachienPC/src && sudo java -classpath /opt/leJOS_NXJ/lib/pc/pccomm.jar:. PCMain ' + command], shell=True)
    return jsonify({'status': command})


def log_action(action):
    LogAction.create(auth_key=has_auth_key(), action=action, logged_on=dt.now())
