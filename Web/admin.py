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


class UserAdminModel(ModelBaseView):
    column_searchable_list = ('username', 'slackname')
    inline_models = None
    form_columns = ('username', 'slackname', 'allowed', 'admin')


class ServiceTokenAdminModel(ModelBaseView):
    column_display_pk = True  # show the service
    column_hide_backrefs = True
    form_columns = ('service', 'key')


class LogActionAdminModel(ModelBaseView):
    can_create = False
    can_edit = False
    can_delete = False
    column_default_sort = ('logged_on', True)


class DoorView(BaseView):

    @expose('/')
    def index(self):
        return self.render('admin/door.html')

    @expose('/toggle/')
    def toggle(self):
        state = send_command('status')['status']
        if 'open' in state:
            state = send_command('close')['status']
        else:
            state = send_command('open')['status']

        return redirect(url_for('admin.index'))

    @expose('/open/')
    def open(self):
        return self.send('open')

    @expose('/close/')
    def close(self):
        return self.send('close')

    def send(self, command):
        send_command(command)['status']
        return redirect(url_for('.index'))

    def is_visible(self):
        return False

    def is_accessible(self):
        if login.current_user.is_anonymous():
            return False

        return login.current_user.is_authenticated()


@app.context_processor
def door_processor():
    def door_status():
        if not login.current_user.is_anonymous():
            return send_command('status')['status']
        return "Not authenticated"
    return dict(door_status=door_status)

admin = Admin(app, name='SlotMachien', url='/slotmachien/admin', template_mode='bootstrap3')

admin.add_view(DoorView(name='Door', endpoint='door'))

admin.add_view(UserAdminModel(User, db.session))
admin.add_view(ServiceTokenAdminModel(ServiceToken, db.session))
admin.add_view(ModelBaseView(Token, db.session))
admin.add_view(LogActionAdminModel(LogAction, db.session))
