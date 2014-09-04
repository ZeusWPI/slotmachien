from peewee import *

from app import db

# Create database models
class ServiceToken(db.Model):
    service = TextField()
    key = TextField()

    def __unicode__(self):
        return '%s' % self.service


class User(db.Model):
    username = CharField(unique=True)
    slackname = CharField()
    allowed = BooleanField()

    def __unicode__(self):
        return '%s' % self.username

    class Meta:
        db_table = 'SMUser'

class Token(db.Model):
    user = ForeignKeyField(User, related_name='tokens')
    token = TextField()
    created_on = DateTimeField()
    expires_on = DateTimeField()

    def __unicode__(self):
        return '%s from %s' % (self.token, self.user.username)


class LogAction(db.Model):
    auth_key = ForeignKeyField(ServiceToken, related_name='actions', null=True)
    user = ForeignKeyField(User, related_name='actions', null=True)
    action = TextField()
    logged_on = DateTimeField()
