DEBUG = False

DATABASE = {
    'name': 'default.db',
    'engine': 'peewee.SqliteDatabase',
    'check_same_thread': False,
}

# Try to import local settings
try:
    from local_settings import *
except ImportError:
    pass
