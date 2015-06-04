## TEMPORARY ## SHOULD BE DELETED AFTER KELDER.ZEUS HAS THEIR CERTIFICATE
from flask.ext.migrate import MigrateCommand, Migrate
from flask.ext.script import Manager
import os
os.environ["OAUTHLIB_INSECURE_TRANSPORT"] = "1"

from app import app, db

from admin import admin
from login import login_manager
from models import *
from utils import start_process
from views import *


if __name__ == '__main__':
    start_process()

    # do it here, because make accessing db changes only possible when executing the program directly
    migrate = Migrate(app, db)
    manager = Manager(app)
    manager.add_command('db', MigrateCommand)
    manager.run()