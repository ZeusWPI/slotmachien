# config

class Configuration(object):
    SQLALCHEMY_DATABASE_URI = 'sqlite:///test.db'
    DEBUG = True
    SECRET_KEY = 'shhhh'
    SLACK_WEBHOOK = ''
    PROCESS = 'python /Users/feliciaan/Documents/ZeusWPI/slotmachien/Web/test.py'
    LOGFILE = 'slotmachien.log'
