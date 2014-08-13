from flask import abort, request
from flask_peewee.auth import Auth

from app import app, db
from models import AuthKey, AcceptedUser

def create_auth(app, db):
   return Auth(app, db)


def has_auth_key():
    # Token support for slack, because it doesn't support customizing headers out of the box
    auth_key = (request.headers.get('Authorization') or
                request.args.get('token') or
                request.form.get('token') or
                request.get_json(force=True).get('token')) # token in POST payload
    try:
        return AuthKey.get(AuthKey.key == auth_key)
    except AuthKey.DoesNotExist:
        return None


def has_username():
    username = (request.args.get('user_name') or
                request.form.get('user_name') or
                request.get_json(force=True).get('user_name'))
    try:
        return AcceptedUser.get(AcceptedUser.username == username)
    except AcceptedUser.DoesNotExist:
        return None

# Method before request to check if allowed to make request
def before_slotmachien_request():
    if not has_auth_key() or not has_username():
        abort(401)


# Do the Auth
auth = create_auth(app, db)
