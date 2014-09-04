import uuid
from datetime import date, timedelta

from flask import abort, request
from flask_peewee.auth import Auth

from app import app, db
from models import ServiceToken, Token, User

def create_auth(app, db):
   return Auth(app, db)

# API Utils
def create_token(user):
    token_string = uuid.uuid4()
    expires_on = date.today() + timedelta(days=365) # 1 year
    token = Token.create(
            user = user,
            token = token_string,
            created_on = date.today(),
            expires_on = expires_on
            )
    return token.token

def authorize_user():
    token = request.headers.get('Authorization')
    try:
        token = Token.get(Token.token == token)
        return token.user
    except Token.DoesNotExist:
        return None

# Slack Utils
def has_slack_token():
    # Token support for slack, because it doesn't support customizing headers out of the box
    auth_key = (request.args.get('token') or
                request.form.get('token'))
    try:
        return ServiceToken.get(ServiceToken.key == auth_key)
    except ServiceToken.DoesNotExist:
        return None


def has_username():
    username = (request.args.get('user_name') or
                request.form.get('user_name') or
                request.get_json(force=True).get('username'))
    try:
        return User.get(User.slackname == username)
    except User.DoesNotExist:
        return None

# Get user from slack of API
def get_user():
    user = has_username() # slack user
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

# Do the Auth
auth = create_auth(app, db)
