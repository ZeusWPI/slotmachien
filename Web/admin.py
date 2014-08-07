from flask_peewee.admin import Admin, ModelAdmin

from app import app, db
from auth import auth
from models import AuthKey, LogAction

class AuthKeyAdmin(ModelAdmin):
    columns = ('service', 'key')


class LogActionAdmin(ModelAdmin):
    columns = ('auth_key', 'action', 'logged_on',)


admin = Admin(app, auth)

admin.register(AuthKey, AuthKeyAdmin)
admin.register(LogAction, LogActionAdmin)
