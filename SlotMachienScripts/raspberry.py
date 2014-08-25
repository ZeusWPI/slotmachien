#!/usr/bin/env python
# encoding: utf-8
#
# Located on the raspberry pi, in the slotmachien user's home. Evoking `door
# username open` (or close or status) opens the slotmachien in name of the
# username.

from __future__ import print_function
import sys
from datetime import datetime
from subprocess import Popen, PIPE
from peewee import (Model, TextField, ForeignKeyField, DateTimeField,
                    SqliteDatabase)

DB = SqliteDatabase('slotmachien/Web/slotmachien.db')
# Error codes
MISSING_ARGUMENTS = 1
UNKNOWN_COMMAND = 2
INTERNAL_ERROR = 3


class DBModel(Model):
    class Meta:
        database = DB


class AuthKey(DBModel):
    service = TextField()
    key = TextField()


class AcceptedUser(DBModel):
    username = TextField()


class LogAction(DBModel):
    auth_key = ForeignKeyField(AuthKey, related_name='actions')
    user = ForeignKeyField(AcceptedUser, related_name='actions')
    action = TextField()
    logged_on = DateTimeField()


def send_command(username, command):
    if command not in ('open', 'close', 'status'):
        return (UNKNOWN_COMMAND, 'command not available')

    p = Popen(['cd slotmachien/SlotMachienPC/src && ' +
               'java -cp /opt/leJOS_NXJ/lib/pc/pccomm.jar:. PCMain ' +
               command], stdin=PIPE, stdout=PIPE, shell=True)
    rc = p.wait()
    output, error = p.communicate()
    if rc == 0 and len(output) > 0:
        # success
        return (0, output.strip())
    else:
        return (INTERNAL_ERROR, 'internal error: %s' % error)


def log(username, command):
    with DB.transaction():
        # Since the execution of this script means the executor is trusted,
        # there is no harm in adding new accepted users during execution.
        user = AcceptedUser.get_or_create(username=username)
        LogAction.create(auth_key=AuthKey.get(service='ssh'), user=user,
                         action=command, logged_on=datetime.now())


if __name__ == '__main__':
    if len(sys.argv) < 3:
        print('Usage: %s username command' % sys.argv[0], file=sys.stderr)
        sys.exit(MISSING_ARGUMENTS)

    username, command = sys.argv[1], ' '.join(sys.argv[2:])
    log(username, command)
    rc, output = send_command(username, command)
    print(output)
    sys.exit(rc)
