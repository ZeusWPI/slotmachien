from models import AuthKey, LogAction

def create_tables(auth):
    auth.User.create_table(fail_silently=True)
    AuthKey.create_table(fail_silently=True)
    LogAction.create_table(fail_silently=True)
