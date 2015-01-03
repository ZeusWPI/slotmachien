from flask import abort, request
from flask.ext.login import current_user


from app import app, db
from models import ServiceToken, Token, User

# API Utils
def create_token(user):
    token = Token()
    token.configure(user)
    db.session.add(token)
    db.session.commit()
    return token.token

# Method before request to check if allowed to make request
def before_request():
    if current_user.is_anonymous():
        abort(401)

def after_slotmachien_request(response):
    headers = response.headers
    # add the user accestoken
    headers['token'] = current_user.tokens.first().token
