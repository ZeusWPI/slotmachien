from flask_peewee.admin import Admin, ModelAdmin

from app import app, db
from auth import auth
from models import AuthKey, LogAction, AcceptedUser

class AuthKeyAdmin(ModelAdmin):
    columns = ('service', 'key')


class AcceptedUserAdmin(ModelAdmin):
    columns = ('username', )


class LogActionAdmin(ModelAdmin):
    columns = ('auth_key', 'user', 'action', 'logged_on',)


admin = Admin(app, auth)

admin.register(AuthKey, AuthKeyAdmin)
admin.register(AcceptedUser, AcceptedUserAdmin)
admin.register(LogAction, LogActionAdmin)
