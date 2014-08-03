from flask import Flask, Blueprint, abort, request
from peewee import *
from flask_peewee.admin import Admin
from flask_peewee.auth import Auth
from flask_peewee.db import Database

import settings
app = Flask(__name__)

#Database settings
DATABASE = {
  'name': settings.db_name,
  'engine': 'peewee.SqliteDatabase',
}

SECRET_KEY = settings.secret_key
DEBUG = True
# Create the database
app.config.from_object(__name__)
db = Database(app)

# create an Auth object for use with our flask app and database wrapper
auth = Auth(app, db)

class AuthKey(db.Model):
  key = TextField()

admin = Admin(app, auth)
admin.register(AuthKey)
admin.setup()

slotmachien_bp = Blueprint('slotmachien', __name__)

# Method before request to check if allow to make request
def before_slotmachien_request():
  authKey = ""
  if request.headers.get('Authorization'):
    authKey = request.headers['Authorization']
  key = AuthKey.select().where(AuthKey.key == authKey)
  print key.count()
  if key.count() == 0:
    abort(401)
slotmachien_bp.before_request(before_slotmachien_request)

# create the routes
@slotmachien_bp.route("/open", methods=['POST'])
def open():
  send_command("open")
  return "ok"

app.register_blueprint(slotmachien_bp, url_prefix='/slotmachien')

def send_command(command):
  #TODO: send command to named pipe
  print(command)

def create():
  auth.User.create_table(fail_silently=True)
  AuthKey.create_table(fail_silently=True)

if __name__ == "__main__":
  create()
  app.run(
    port = settings.port
  )
