from models import AuthKey, LogAction, AcceptedUser

def create_tables(auth):
    auth.User.create_table(fail_silently=True)
    AuthKey.create_table(fail_silently=True)
    AcceptedUser.create_table(fail_silently=True)
    LogAction.create_table(fail_silently=True)
