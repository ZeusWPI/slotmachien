SlotMachienWeb
==============

# Installation
Use virtualenv or so, install everything through pip
Do `pip install -r requirements.txt`

After that start the server in one shell, and in another one create an admin account
```
from slotmachien import auth
auth.User.create_table(fail_silently=True)  # make sure table created.
admin = auth.User(username='admin', email='', admin=True, active=True)
admin.set_password('admin')
admin.save()
```

After that you can go to localhost:<port>/admin, and login to the admin interface,
and add an AuthKey for the requests.
# API
## Sending requests
Get the authorization code by authenticating @ login

### Status door (without authorization)
Send a GET request to `/slotmachien/` or send the send `status` with update door

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
Send a POST request to `/slotmachien/login`
Header set the `Content-Type: application/json`
```javascript
{
    "username": username,
    "password": password
}
```

### Response
If the HTTP status is 200, then we respond with a json, when succesull:
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
