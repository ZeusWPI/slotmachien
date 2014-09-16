from flask_peewee.admin import Admin, ModelAdmin

from app import app, db
from auth import auth
from models import ServiceToken, User, Token, LogAction

class ServiceTokenAdmin(ModelAdmin):
    columns = ('service', 'key')


class UserAdmin(ModelAdmin):
    columns = ('username', 'slackname', 'allowed')


class TokenAdmin(ModelAdmin):
    columns = ('user', 'token', 'created_on', 'expires_on')


class LogActionAdmin(ModelAdmin):
    columns = ('auth_key', 'user', 'action', 'logged_on')


admin = Admin(app, auth)

admin.register(ServiceToken, ServiceTokenAdmin)
admin.register(User, UserAdmin)
admin.register(Token, TokenAdmin)
admin.register(LogAction, LogActionAdmin)
