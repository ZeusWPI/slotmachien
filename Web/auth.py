from flask import abort, request
from flask.ext.login import current_user


from app import app, db
from models import ServiceToken, Token, User

def create_auth(app, db):
   return Auth(app, db)

# API Utils
def create_token(user):
    token = Token()
    token.configure(user)
    db.session.add(token)
    db.session.commit()
    return token.token

def authorize_user():
    token = request.headers.get('Authorization')
    token = Token.query.filter_by(token=token).first()
    if token:
        return token.user
    else:
        return None

# Slack Utils
def has_slack_token():
    # Token support for slack, because it doesn't support customizing headers out of the box
    auth_key = (request.args.get('token') or
                request.form.get('token'))

    if auth_key:
        return ServiceToken.query.filter_by(key = auth_key).first()

    return None

def has_username():
    username = None
    if current_user.is_anonymous():
        username = (request.args.get('user_name') or
                    request.form.get('user_name') or
                    request.get_json(force=True).get('username'))

        if username:
            return User.query.filter_by(slackname = username).first()

    else:
        return current_user

    return None

# Get user from slack of API
def get_user():
    user = has_username() # slack user

    if not user and not current_user.is_anonymous():
        user = current_user # web admin user

    if not user:
        user = authorize_user() # API user

    return user

# Method before request to check if allowed to make request
def before_slack_request():
    if not has_slack_token() or not has_username():
        abort(401)

def before_slotmachien_request():
    if not authorize_user():
        abort(401)
