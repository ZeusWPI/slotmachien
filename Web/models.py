import uuid
from datetime import date, timedelta

from app import db

# Create database models
class ServiceToken(db.Model):
    service = db.Column(db.String(20), primary_key=True)
    key = db.Column(db.String(120))
    logactions = db.relationship('LogAction', backref='service_token', lazy='dynamic')

    def configure(self, service, key):
        self.service = service
        self.key = key

    def __repr__(self):
        return '%s' % self.service


class User(db.Model):
    id = db.Column(db.Integer, primary_key=True)
    username = db.Column(db.String(80), unique=True)
    slackname = db.Column(db.String(80), unique=True)
    allowed = db.Column(db.Boolean)
    admin = db.Column(db.Boolean)
    tokens = db.relationship('Token', backref='user', lazy='dynamic')
    logactions = db.relationship('LogAction', backref='user', lazy='dynamic')


    def configure(self, username, slackname, allowed, admin):
        self.username = username
        self.slackname = slackname
        self.allowed = allowed
        self.admin = admin

    def is_authenticated(self):
        return self.allowed

    def is_active(self):
        return self.allowed

    def is_admin(self):
        return self.admin

    def is_anonymous(self):
        return False

    def get_id(self):
        try:
            return unicode(self.id)  # python 2
        except NameError:
            return str(self.id)  # python 3

    def __repr__(self):
        return '%s' % self.username

class Token(db.Model):
    id = db.Column(db.Integer, primary_key=True)
    token = db.Column(db.String(120), unique=True)
    user_id = db.Column(db.Integer, db.ForeignKey('user.id'))
    created_on = db.Column(db.DateTime)
    expires_on = db.Column(db.DateTime)

    def configure(self, user):
        self.user = user
        self.created_on = date.today()
        self.expires_on = date.today() + timedelta(days=365) # 1 year

        self.create_token()

    def create_token(self):
        self.token = str(uuid.uuid4())

    def __repr__(self):
        return '%s: %s' % (self.user.username, self.token)


class LogAction(db.Model):
    id = db.Column(db.Integer, primary_key=True)
    service_token_service = db.Column(db.String(20), db.ForeignKey('service_token.service'))
    user_id = db.Column(db.Integer, db.ForeignKey('user.id'))
    action = db.Column(db.String(15))
    logged_on = db.Column(db.DateTime)

    def configure(self, service_token, user, action, logged_on):
        self.service_token = service_token
        self.user = user
        self.action = action
        self.logged_on = logged_on

    def __repr__(self):
        return '%s: %s on %s' % (self.user.username, self.action, self.service_token_service)
