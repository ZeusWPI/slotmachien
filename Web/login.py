from flask import redirect, request, url_for, abort, session
from flask.ext.login import LoginManager, current_user, logout_user
from flask_oauthlib.client import OAuth

import requests

from app import app, db, logger, cache
from models import User, Token, ServiceToken
from zeus import oauth, zeus_login

login_manager = LoginManager()
login_manager.init_app(app)


@login_manager.user_loader
def load_user(userid):
    return User.query.filter_by(id=userid).first()


@login_manager.request_loader
def load_user_from_request(request):
    # slack login
    auth_key = (request.args.get('token') or
                request.form.get('token'))
    if auth_key:
        servictoken = ServiceToken.query.filter_by(key=auth_key).first()
        if servictoken:
            user_id = (request.args.get('user_id') or
                        request.form.get('user_id'))
            user = request_user_slack(user_id)
            if user and user.is_allowed():
                return user
            else:
                logger.error(
                    "User ID %s is not in the database" % user_id)

    # try token login
    token = request.headers.get('Authorization')
    if token:
        token = Token.query.filter_by(token=token).first()
        if token:
            user = token.user
            if user.is_authenticated():
                return user

    # finally, return None if both methods did not login the user
    return None

@app.route('/slotmachien/login')
def login():
    return zeus_login()


@app.route('/slotmachien/logout')
def logout():
    if 'zeus_token' in session:
        session.pop('zeus_token', None)
    logout_user()
    return redirect(url_for('admin.index'))


def before_request():
    if current_user.is_anonymous() or not current_user.is_allowed():
        abort(401)


def request_user_slack(user_id):
    identifier = 'slack_id/%s' % user_id
    username = cache.get(identifier)
    if username is None:
        username = request_username_slack(user_id)
        if username is not None:
            cache.set(identifier, username)
    if username is not None:
        user = User.query.filter_by(username=username.lower()).first()
        return user
    return None


def request_username_slack(user_id):
    payload = {'token': app.config['SLACK_TOKEN'], 'user': user_id}
    r = requests.get('https://slack.com/api/users.info', params=payload)

    if r.status_code == 200:
        response = r.json()
        json_user = response.get('user', '')
        if len(json_user) > 0:
            email = response['user']['profile']['email']
            if email.endswith('@zeus.ugent.be'):
                username = email.split('@zeus.ugent.be')[0]
                return username
    return None
