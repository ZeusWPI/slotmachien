import settings

from flask import Flask, Blueprint, abort, request
from flask_peewee.admin import Admin
from flask_peewee.auth import Auth
from flask_peewee.db import Database
from peewee import *


# Database settings
DATABASE = {
    'name': settings.db_name,
    'engine': 'peewee.SqliteDatabase',
}
SECRET_KEY = settings.secret_key
DEBUG = True

# Create the database
app = Flask(__name__)
app.config.from_object(__name__)
db = Database(app)

# Create database models
class AuthKey(db.Model):
    key = TextField()

# Create an Auth object for use with our flask app and database wrapper
auth = Auth(app, db)

admin = Admin(app, auth)
admin.register(AuthKey)
admin.setup()

slotmachien_bp = Blueprint('slotmachien', __name__)


# Method before request to check if allowed to make request
def before_slotmachien_request():
    # Token support for slack, because it doesn't support customizing headers
    # out of the box
    auth_key = (request.headers.get('Authorization') or
                request.args.get('token'))
    key = AuthKey.select().where(AuthKey.key == auth_key)
    if key.count() == 0:
        abort(401)
slotmachien_bp.before_request(before_slotmachien_request)


# Create the routes
@slotmachien_bp.route("/open", methods=['POST'])
def open_door():
    send_command("open")
    return "ok"


@slotmachien_bp.route('/close', methods=['POST'])
def close_door():
    send_command("close")
    return "ok"

app.register_blueprint(slotmachien_bp, url_prefix='/slotmachien')


def send_command(command):
    # TODO: send command to named pipe
    print(command)


def create():
    auth.User.create_table(fail_silently=True)
    AuthKey.create_table(fail_silently=True)

if __name__ == "__main__":
    create()
    app.run(
        port=settings.port
    )
