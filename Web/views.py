from flask import Blueprint, request

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
    return send_command((request.form.get('text') or # slack support
                         request.get_json(force=True)['action']))

@slotmachien_bp.route('/')
def status_door():
    return send_command('status')


app.register_blueprint(slotmachien_bp, url_prefix='/slotmachien')
