from flask import render_template, flash, redirect, request, url_for, session, jsonify
from flask.ext.login import LoginManager, login_user, current_user, logout_user
from flask_oauthlib.client import OAuth


from app import app, db
from models import User, Token, ServiceToken
from github import oauth

login_manager = LoginManager()
login_manager.init_app(app)


@login_manager.user_loader
def load_user(userid):
    return User.query.filter_by(id = userid).first()

@login_manager.request_loader
def load_user_from_request(request):
    # slack login
    auth_key = (request.args.get('token') or
                request.form.get('token'))
    print auth_key
    if auth_key:
        servictoken = ServiceToken.query.filter_by(key = auth_key).first()
        if servictoken:
            username = (request.args.get('user_name') or
                        request.form.get('user_name') or
                        request.get_json(force=True).get('username'))

            if username:
                user = User.query.filter_by(slackname = username).first()
                if user.is_authenticated():
                    return user


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

@app.route("/login")
def user_login():
    return redirect(url_for('login'))

@app.route("/logout")
def user_logout():
    return redirect(url_for('logout'))
