from peewee import *

from app import db

# Create database models
class AuthKey(db.Model):
    service = TextField()
    key = TextField()

    def __unicode__(self):
        return '%s' % self.service


class LogAction(db.Model):
    auth_key = ForeignKeyField(AuthKey, related_name='actions')
    action = TextField()
    logged_on = DateTimeField()
