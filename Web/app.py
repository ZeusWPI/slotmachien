from flask import Flask
from flask.ext.sqlalchemy import SQLAlchemy
from flask.ext.socketio import SocketIO


from logbook import Logger

app = Flask(__name__)
app.config.from_object('config.Configuration')

db = SQLAlchemy(app)

socketio = SocketIO(app)

logger = Logger('SlotMachien-Web')
