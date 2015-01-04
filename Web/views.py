from flask import Blueprint, request, jsonify, redirect, url_for


from app import app
from login import before_request
from utils import send_command
from models import User

supported_actions = ['open', 'close', 'status']


@app.route('/slotmachien/', methods=['POST'])
def update_door():
    before_request()
    action = request.get_json(force=True)['action']
    if action in supported_actions:
        return jsonify(send_command(action))
    else:
        return jsonify({'error': 'command: ' + action + ' unknown'})


# TODO: attach
def after_slotmachien_request(response):
    headers = response.headers
    # add the user accestoken
    headers['token'] = current_user.tokens.first().token


@app.route('/slotmachien/')
def status_door():
    content_type = request.headers.get('Content-Type', None)
    if content_type and content_type in 'application/json':
        return jsonify(send_command('status'))

    return redirect(url_for("admin.index"))


@app.route('/slotmachien/slack/', methods=['POST'])
def slack_update_door():
    before_request()
    action = request.form.get('text')
    print action
    if action in supported_actions:
        return 'The door is ' + send_command(action)['status'] + '!'
    else:
        return "This command " + action + " is not supported!"

if app.config['DEBUG']:  # add route information
    @app.route('/routes')
    def list_routes(self):
        import urllib
        output = []
        for rule in app.url_map.iter_rules():
            options = {}
            for arg in rule.arguments:
                options[arg] = "[{0}]".format(arg)

            methods = ','.join(rule.methods)
            url = url_for(rule.endpoint, **options)
            line = urllib.unquote(
                "{:50s} {:20s} {}".format(rule.endpoint, methods, url))
            output.append(line)

        string = ''
        for line in sorted(output):
            string += line + "<br/>"

        return string
