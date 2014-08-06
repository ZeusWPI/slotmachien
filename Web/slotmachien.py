import settings

from datetime import datetime as dt
import subprocess

from flask import Flask, Blueprint, abort, request, jsonify
from flask_peewee.admin import Admin, ModelAdmin
from flask_peewee.auth import Auth
from flask_peewee.db import Database
from peewee import *


# Database settings
DATABASE = {
    'name': settings.db_name,
    'engine': 'peewee.SqliteDatabase',
}
SECRET_KEY = settings.secret_key
DEBUG = settings.debug

# Create the database
app = Flask(__name__)
app.config.from_object(__name__)
db = Database(app)

# Create database models
class AuthKey(db.Model):
    service = TextField()
    key = TextField()

    def __unicode__(self):
        return '%s' % self.service

class AuthKeyAdmin(ModelAdmin):
    columns = ('service', 'key')

class LogAction(db.Model):
    auth_key = ForeignKeyField(AuthKey, related_name='actions')
    action = TextField()
    logged_on = DateTimeField()

class LogActionAdmin(ModelAdmin):
    columns = ('auth_key', 'action', 'logged_on',)

# Create an Auth object for use with our flask app and database wrapper
auth = Auth(app, db)

admin = Admin(app, auth)
admin.register(AuthKey, AuthKeyAdmin)
admin.register(LogAction, LogActionAdmin)
admin.setup()

slotmachien_bp = Blueprint('slotmachien', __name__)


# Method before request to check if allowed to make request
def before_slotmachien_request():
    if not auth_key():
        abort(401)
slotmachien_bp.before_request(before_slotmachien_request)


# Create the routes
@slotmachien_bp.route('/open', methods=['POST'])
def open_door():
    return send_command('open')

@slotmachien_bp.route('/close', methods=['POST'])
def close_door():
    return send_command('close')

@slotmachien_bp.route('/', methods=['POST'])
def update_door():
    return send_command(request.get_json(force=True)['action'])

@slotmachien_bp.route('/')
def status_door():
    return send_command('status')

app.register_blueprint(slotmachien_bp, url_prefix='/slotmachien')


def send_command(command):
    log_action(command)
    if settings.debug:
        return jsonfiy({'status': 'error'})
    if command == 'status':
        return jsonify({'status': 'error'})
    print(command)

    subprocess.call(['cd ../SlotMachienPC/src && sudo java -classpath /opt/leJOS_NXJ/lib/pc/pccomm.jar:. PCMain ' + command], shell=True)
    return jsonify({'status': command})

def auth_key():
    # Token support for slack, because it doesn't support customizing headers
    # out of the box
    auth_key = (request.headers.get('Authorization') or
                request.args.get('token'))
    try:
        return AuthKey.get(AuthKey.key == auth_key)
    except AuthKey.DoesNotExist:
        return None

def log_action(action):
    LogAction.create(auth_key=auth_key(), action=action, logged_on=dt.now())

def create():
    auth.User.create_table(fail_silently=True)
    AuthKey.create_table(fail_silently=True)
    LogAction.create_table(fail_silently=True)

if __name__ == "__main__":
    create()
    app.run(
        port=settings.port,
        host='0.0.0.0'
    )
