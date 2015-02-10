from flask.ext.socketio import emit

from app import logger, socketio

NAMESPACE='/slotmachien'

@socketio.on('connect', namespace=NAMESPACE)
def connect_websocket():
    logger.info('client connected')

@socketio.on('my event', namespace=NAMESPACE)
def test_message(message):
    emit('my response',
         {'data': message['data'], 'count': 1})

@socketio.on('disconnect', namespace=NAMESPACE)
def test_disconnect():
    logger.info('Client disconnected')

def send_message(message):
    global NAMESPACE
    socketio.emit('state-change', {'data': message}, namespace=NAMESPACE)
