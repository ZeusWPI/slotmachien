from flask_peewee.db import Database

from settings import DATABASE
from models import AuthKey, LogAction

def create_database(app):
    return Database(app)

def create_tables(auth):
    auth.User.create_table(fail_silently=True)
    AuthKey.create_table(fail_silently=True)
    LogAction.create_table(fail_silently=True)
