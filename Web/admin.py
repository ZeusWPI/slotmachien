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
    column_display_pk = True  # show the service
    column_hide_backrefs = True
    form_columns = ('service', 'key')


class DoorView(BaseView):

    @expose('/')
    def index(self):
        state = send_command('status')['status']

        return self.render('admin/door.html', state=state)

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

admin = Admin(app, name='SlotMachien', url='/slotmachien/admin', template_mode='bootstrap3')

admin.add_view(DoorView(name='Door', endpoint='door'))

admin.add_view(ServiceTokenAdmin(ServiceToken, db.session))
admin.add_view(ModelBaseView(User, db.session))
admin.add_view(ModelBaseView(Token, db.session))
admin.add_view(ModelBaseView(LogAction, db.session))
