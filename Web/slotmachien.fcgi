#!/bin/python

from flup.server.fcgi import WSGIServer
from logbook import FileHandler


from app import app, db

#init logbook here to also handle the process
log_handler = FileHandler(app.config['LOGFILE'])
log_handler.push_application()

from admin import admin
from login import login_manager
from models import *
from utils import start_process
from views import *

if __name__ == '__main__':
    start_process()
    WSGIServer(app, bindAddress='/tmp/slotmachien.sock').run()
