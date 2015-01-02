from flask import url_for, redirect
from flask.ext.admin import Admin, BaseView, expose
from flask.ext.admin.contrib.sqla import ModelView
from flask.ext import login


from app import app, db
from models import ServiceToken, User, Token, LogAction
from utils import send_command

class ModelBaseView(ModelView):
    def is_accessible(self):
        if login.current_user.is_anonymous():
            return False

        return login.current_user.is_admin()


class ServiceTokenAdmin(ModelBaseView):
    column_display_pk = True # show the service
    column_hide_backrefs = True
    form_columns = ('service', 'key')


class DoorView(BaseView):
    @expose('/')
    def index(self):
        state = send_command('status')['status']

        return self.render('admin/door.html', state = state, toggle_url = url_for('.toggle'))

    @expose('/routes')
    def list_routes(self):
        import urllib
        output = []
        for rule in app.url_map.iter_rules():
            options = {}
            for arg in rule.arguments:
                options[arg] = "[{0}]".format(arg)

            methods = ','.join(rule.methods)
            url = url_for(rule.endpoint, **options)
            line = urllib.unquote("{:50s} {:20s} {}".format(rule.endpoint, methods, url))
            output.append(line)

        string = ''
        for line in sorted(output):
            string += line + "<br/>"

        return string

    @expose('/toggle/')
    def toggle(self):
        state = send_command('status')['status']
        if state in 'open':
            state = send_command('close')['status']
        else:
            state = send_command('open')['status']

        return redirect(url_for('.index'))

    def is_accessible(self):
        if login.current_user.is_anonymous():
            return False

        return login.current_user.is_admin()

admin = Admin(app, name='SlotMachien')

admin.add_view(DoorView(name='Door', endpoint='door'))

admin.add_view(ServiceTokenAdmin(ServiceToken, db.session))
admin.add_view(ModelBaseView(User, db.session))
admin.add_view(ModelBaseView(Token, db.session))
admin.add_view(ModelBaseView(LogAction, db.session))
