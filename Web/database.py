from models import ServiceToken, Token, LogAction, User

def create_tables(auth):
    auth.User.create_table(fail_silently=True)
    ServiceToken.create_table(fail_silently=True)
    User.create_table(fail_silently=True)
    Token.create_table(fail_silently=True)
    LogAction.create_table(fail_silently=True)
