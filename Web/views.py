from flask import Blueprint, request, jsonify

from app import app
from auth import auth, before_slotmachien_request, before_slack_request, create_token
from utils import send_command
from models import User
from kerberos import authenticate_user


slotmachien_bp = Blueprint('slotmachien', __name__)
slotmachien_bp.before_request(before_slotmachien_request)

@slotmachien_bp.route('/', methods=['POST'])
def update_door():
    action = (request.form.get('text') or # slack support
                         request.get_json(force=True)['action'])
    if action in ['open', 'close', 'status']:
        return send_command(action)
    else:
        return jsonify({'error': 'command: ' + action + ' unknown'})


app.register_blueprint(slotmachien_bp, url_prefix='/slotmachien')

@app.route('/slotmachien/')
def status_door():
    return send_command('status')

@app.route('/slotmachien/slack/', methods=['POST'])
def slack_update_door():
    before_slack_request()
    return update_door()

@app.route('/slotmachien/login', methods=['POST'])
def login():
    json = request.get_json(force=True)
    username = json.get('username')
    password = json.get('password')

    if authenticate_user(username, password):
        try:
            user = User.get(User.username == username)
            return jsonify({'token': create_token(user)})
        except User.DoesNotExist:
            jsonify({'error': 'user is not added'})
    else:
        return jsonify({'error': 'username or password is wrong'})
