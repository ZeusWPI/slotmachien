from flask import Flask, redirect, url_for, session, request, jsonify
from flask.ext.login import LoginManager, login_user, current_user, logout_user
from flask.ext.admin import helpers
from flask_oauthlib.client import OAuth


from app import app, db
from models import User, Token

oauth = OAuth(app)

github = oauth.remote_app(
    'github',
    consumer_key=app.config['GITHUB_KEY'],
    consumer_secret=app.config['GITHUB_SECRET'],
    request_token_params={'scope': 'user:email'},
    base_url='https://api.github.com/',
    request_token_url=None,
    access_token_method='POST',
    access_token_url='https://github.com/login/oauth/access_token',
    authorize_url='https://github.com/login/oauth/authorize'
)


@app.route('/slotmachien/login')
def login():
    return github.authorize(callback=url_for('authorized', _external=True))


@app.route('/slotmachien/logout')
def logout():
    if 'github_token' in session:
        session.pop('github_token', None)
    logout_user()
    return redirect(url_for('admin.index'))


@app.route('/slotmachien/login/github/authorized')
def authorized():
    resp = github.authorized_response()
    if resp is None:
        return 'Access denied: reason=%s error=%s' % (
            request.args['error'],
            request.args['error_description']
        )
    session['github_token'] = (resp['access_token'], '')
    me = github.get('user')

    user = User.query.filter_by(username=me.data['login'].lower()).first()
    if user:
        login_user(user)
        # add_token(resp['access_token'], user)
        return redirect(url_for("admin.index"))

    return jsonify(me.data)


@github.tokengetter
def get_github_oauth_token():
    return session.get('github_token')


def add_token(token, user):
    token = Token.query.filter_by(token=token, user=user).first()
    if token is None:
        token = Token()
        token.configure(user)
        token.token = token
        db.session.add(token)
        db.session.commit()
