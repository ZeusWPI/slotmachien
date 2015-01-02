from app import app, db

from auth import *
from admin import admin
from login import login_manager
from models import *
from views import *


if __name__ == '__main__':
    app.run()
