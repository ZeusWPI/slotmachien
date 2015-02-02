# config

class Configuration(object):
    DATABASE = {
        'name': 'slotmachien.db',
        'engine': 'peewee.SqliteDatabase',
        'check_same_thread': False,
    }
    DEBUG = False
    SECRET_KEY = 'shhhh'
    SLACK_WEBHOOK = ''
    PROCESS = 'cd /home/slotmachien/slotmachien/SlotMachienPC/src && java -cp /opt/leJOS_NXT/lib/pc/pccomm.jar:. PCMain'
    LOGFILE = 'slotmachien.log'
