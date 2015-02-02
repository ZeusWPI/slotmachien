from flask import Flask, redirect, url_for, session, request, jsonify, flash
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


def github_login():
    if app.debug:
        return github.authorize(callback=url_for('authorized', _external=True))
    else: # temporary solution because it otherwise gives trouble on the pi because of proxies and such
        return github.authorize(callback='http://kelder.zeus.ugent.be/slotmachien/login/github/authorized')


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
        content_type = request.headers.get('Content-Type', None)
        if content_type and content_type in 'application/json':
            token = add_token(user)
            return jsonify({'token': token.token})
        return redirect(url_for("admin.index"))

    flash("You're not allowed to enter, please contact a system administrator")
    return redirect(url_for("admin.index"))


@github.tokengetter
def get_github_oauth_token():
    return session.get('github_token')


def add_token(user):
    token = Token()
    token.configure(user)
    db.session.add(token)
    db.session.commit()
    return token
