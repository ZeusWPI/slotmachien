from app import app, db, socketio

from admin import admin
from login import login_manager
from models import *
from utils import start_process
from views import *
from sockets import *


if __name__ == '__main__':
    start_process()
    #socketio.run(app, resource='slotmachien/socket.io')
    socketio.run(app)
