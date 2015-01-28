from flask import Blueprint, request, jsonify, redirect, url_for


from app import app
from login import before_request
from utils import send_command
from models import User

supported_actions = ['open', 'close', 'status', 'ping','buzz','beep']


@app.route('/slotmachien/', methods=['POST'])
def update_door():
    before_request()
    request = request.get_json(force=True)['action']

    logger.info("Got general request: "+request)
    if action in supported_actions:
        return jsonify(send_command(action,'todo',''))
    else:
        return jsonify({'error': 'command: ' + action + ' unknown'})


@app.route('/slotmachien/')
def status_door():
    content_type = request.headers.get('Content-Type', None)
    if content_type and content_type in 'application/json':
        return jsonify(send_command('status','todo',''))

    return redirect(url_for("admin.index"))

@app.route('/slotmachien/slack/', methods=['POST'])
def slack_update_door():
    before_request()
    request = request.form.get('text')
    print("Got slack request: "+request)
    return jsonify({"text","Hi slack!"})



if app.debug:  # add route information
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
