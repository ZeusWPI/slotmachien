## TEMPORARY ## SHOULD BE DELETED AFTER KELDER.ZEUS HAS THEIR CERTIFICATE
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
    app.run()
