from models import *
from app import db

db.drop_all()
db.create_all()

feli = User()
feli.configure("feliciaan", "felikaan", True, True)
db.session.add(feli)


# commit all the things
db.session.commit()
