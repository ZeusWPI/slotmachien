from flask_peewee.admin import Admin, ModelAdmin

from models import AuthKey, LogAction

class AuthKeyAdmin(ModelAdmin):
    columns = ('service', 'key')


class LogActionAdmin(ModelAdmin):
    columns = ('auth_key', 'action', 'logged_on',)


def init_admin(app, auth):
    admin = Admin(app, auth)

    admin.register(AuthKey, AuthKeyAdmin)
    admin.register(LogAction, LogActionAdmin)

    admin.setup()
