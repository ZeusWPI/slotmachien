from app import app, db

from auth import *
from admin import admin
from database import create_tables
from models import *
from views import *


admin.setup()

if __name__ == '__main__':
    create_tables(auth)
    app.run()
