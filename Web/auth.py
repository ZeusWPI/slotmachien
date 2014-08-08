from flask import abort, request
from flask_peewee.auth import Auth

from app import app, db
from models import AuthKey

def create_auth(app, db):
   return Auth(app, db)


def has_auth_key():
    # Token support for slack, because it doesn't support customizing headers out of the box
    auth_key = (request.headers.get('Authorization') or
                request.args.get('token'))
    try:
        return AuthKey.get(AuthKey.key == auth_key)
    except AuthKey.DoesNotExist:
        return None


# Method before request to check if allowed to make request
def before_slotmachien_request():
    if not has_auth_key():
        abort(401)


# Do the Auth
auth = create_auth(app, db)
