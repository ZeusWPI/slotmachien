SlotMachienWeb
==============

# Installation
1. Install necessary packages, best in a virtualenv.
Do `pip install -r requirements.txt`

2. Modify configuration settings, copy the `config.example.py`-file to `config.py` and fill it in.
To acquire OAuth-credentials, mail sysadmins@zeus.UGent.be.
Make sure you're in debug when the NXT is not connected.

3. Initialize database with alembic, execute the following command `python slotmachien.py db upgrade`

4. To start developing, add yourself to the database. Open a python shell and execute the following commands:
```
from models import User, db
admin = User()
admin.configure('<your Zeus OAuth name>', True, True)
db.session.add(admin)

# commit all the things
db.session.commit()
```

5. Run the server on your computer with `python slotmachien.py runserver`
After that you can go to localhost:<port>/admin, and login to the admin interface,
and add an AuthKey for the slack requests.
# API
## Sending requests
Get the authorization code by authenticating @ login

### Status door (without authorization)
Send a GET request to `/slotmachien/` or send the send `status` with update door,
if the header is set to `Content-Type: application/json` then a JSON is returned,
otherwise you're redirected to `/slotmachien/admin/`.

### Update door
Send a POST request to `/slotmachien/`
Header set the `Content-Type: application/json` and `Authorization: <token>`
With payload
```javascript
{
    "action": <action>
}
```
Possible action keywords are: open, close, status

### Response
If the HTTP status is 200, then we respond with a json, 2 kinds when succesfull:
```javascript
{
    "status": <current status>
}
```
Possibilities: `open` and `closed`
Error:
```javascript
{
    "error": "error message as string"
}
```

If the HTTP status code is 401, authentication failed.

## Login
### Request
Login using the link.

### Response
If the HTTP status is 200, then we respond with a json, when succesull, if the header has `Content-Type: application/json`:
```javascript
{
    "token": <new generated token>
}
```
Error:
```javascript
{
    "error": "error message as string"
}
```
If the HTTP status code is 401, authentication failed.

# Configuration stuff
## nginx configuration
You have to run the slotmachien.fcgi program.
Than add the following configuration to nginx:
```
location / {
    try_files $uri @slotmachien;
}

location @slotmachien {
    include fastcgi_params;
    fastcgi_param PATH_INFO $fastcgi_script_name;
    fastcgi_param SCRIPT_NAME "";
    fastcgi_pass unix:/tmp/slotmachien.sock;
}
```

## Daemon configuration
Copy the `slotmachien-fastcgi.sh` to the `/etc/init.d/` directory as `slotmachien`

## Config.py configuration
Default in demo files:
```
class Configuration(object):
    SQLALCHEMY_DATABASE_URI = 'sqlite:///slotmachien.db'
    DEBUG = False
    SECRET_KEY = '<s3r3t>'
    SLACK_WEBHOOK = 'https://zeuswpi.slack.com/services/hooks/incoming-webhook?token=<s3cr3t>'
    PROCESS = 'cd /home/slotmachien/slotmachien/SlotMachienPC/bin && /opt/leJOS_NXT/bin/nxjpc PCMain'
    LOGFILE = '/var/log/slotmachien.log'
    GITHUB_KEY = '<not-so-secret>'
    GITHUB_SECRET = '<super-secret>'
```
