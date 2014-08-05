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

# Sending requests
All requests need to have an `Authorization` field with the AuthKey as param, or as ?token=<key>

## Open door
Send a POST request to `/slotmachien/open`

## Close door
Send a POST request to `/slotmachien/close`

## Status door
Send a GET request to `/slotmachien/`
