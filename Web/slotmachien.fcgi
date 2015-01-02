#!/Users/feliciaan/Documents/ZeusWPI/slotmachien/Web/env/bin/python
import sys # insert path
sys.path.insert(0, '/Users/feliciaan/Documents/ZeusWPI/slotmachien/Web/env/lib/python2.7/site-packages')


from flup.server.fcgi import WSGIServer
from logbook import FileHandler


from app import app, db

#init logbook here to also handle the process
log_handler = FileHandler(app.config['LOGFILE'])
log_handler.push_application()

from auth import *
from admin import admin
from login import login_manager
from models import *
from views import *

if __name__ == '__main__':
    WSGIServer(app, bindAddress='/tmp/slotmachien.sock').run()
