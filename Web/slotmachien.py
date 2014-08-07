from flask import Flask, Blueprint, abort, request, jsonify

from admin import init_admin
from auth import create_auth
from database import create_database, create_tables
from settings import *
from views import register_blueprint


# Define the app
app = Flask(__name__)
app.config.from_object(__name__)

# Define the databaae
db = create_database(app)

# Do the Auth
auth = create_auth(app, db)

# Yes. We need admins.
init_admin(app, auth)

# Register the blueprint
register_blueprint(app)


# Run, you fools!
if __name__ == '__main__':
    create_tables(auth)

    app.run()

