# config


class Configuration(object):
    SQLALCHEMY_DATABASE_URI = 'sqlite:///slotmachien.db'
    DEBUG = True
    SECRET_KEY = '<change>'
    SLACK_WEBHOOK = '<add url>'
    PROCESS = 'python test.py'
    LOGFILE = 'slotmachien.log'
    ZEUS_KEY = '<fill in>'
    ZEUS_SECRET = '<fill in>'
