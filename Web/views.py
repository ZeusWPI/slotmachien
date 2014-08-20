from flask import Blueprint, request, jsonify

from app import app
from auth import auth, before_slotmachien_request
from utils import send_command


slotmachien_bp = Blueprint('slotmachien', __name__)
slotmachien_bp.before_request(before_slotmachien_request)

@slotmachien_bp.route('/open', methods=['POST'])
def open_door():
    return send_command('open')


@slotmachien_bp.route('/close', methods=['POST'])
def close_door():
    return send_command('close')


@slotmachien_bp.route('/', methods=['POST'])
def update_door():
    action = (request.form.get('text') or # slack support
                         request.get_json(force=True)['action'])
    if action in ['open', 'close', 'status']:
        return send_command(action)
    else:
        return jsonify({'error': 'command: ' + action + ' unknown'})

@slotmachien_bp.route('/')
def status_door():
    return send_command('status')


app.register_blueprint(slotmachien_bp, url_prefix='/slotmachien')
