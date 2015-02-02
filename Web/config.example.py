# config


class Configuration(object):
    SQLALCHEMY_DATABASE_URI = 'sqlite:///slotmachien.db'
    DEBUG = True
    SECRET_KEY = '<change>'
    SLACK_WEBHOOK = '<add url>'
    PROCESS = 'python test.py'
    LOGFILE = 'slotmachien.log'
    GITHUB_KEY = '<fill in>'
    GITHUB_SECRET = '<fill in>'
