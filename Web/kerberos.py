
def authenticate_user(username, password):
    #TODO: add real kereberos implementation
    if password == 'slotmachien':
        return True
    else:
        return False
