# config

class Configuration(object):
    DATABASE = {
        'name': 'slotmachien.db',
        'engine': 'peewee.SqliteDatabase',
        'check_same_thread': False,
    }
    DEBUG = False
    SECRET_KEY = 'shhhh'
