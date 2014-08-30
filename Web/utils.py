from datetime import datetime as dt
from subprocess import Popen, PIPE
from threading import Thread
import time
import signal
import sys

from flask import jsonify

from app import app
from auth import has_auth_key, has_username
from models import LogAction

class Process:
    def __init__(self):
        self.process = None
        self.inputProcessing = None
        self.heartbeat = None
        self.stopped = False
        self.last_status = ""
        self.create()

    def create(self):
        self.clean_process()
        if app.config['DEBUG']:
            self.process = Popen(['python test.py'], stdin=PIPE, stdout=PIPE,
                            shell=True, universal_newlines=True)
        else:
            self.process = Popen(['cd ../SlotMachienPC/src && ' +
                    'java -cp /opt/leJOS_NXT/lib/pc/pccomm.jar:. PCMain '],
                    stdin=PIPE, stdout=PIPE, shell=True)

        print('SlotMachienPC pid: ' + str(self.process.pid))
        self.stopped = False
        self.last_status = self.process.stdout.readline()

        # Create input processing thread
        self.inputProcessing = InputProcessingThread(self)
        self.inputProcessing.setDaemon(True)
        self.inputProcessing.start()

        # Create heartbeat thread
        self.heartbeat = HeartBeatThread(self)
        self.heartbeat.setDaemon(True)
        self.heartbeat.start()

    def clean_process(self):
        if self.process and not self.process.poll():
            self.process.stdin.close()
            self.process.stdout.close()
            self.process.terminate()

        if self.inputProcessing and self.inputProcessing.isAlive():
            self.inputProcessing.join() #TODO: check if something better exists
            self.inputProcessing = None

        if self.heartbeat and self.heartbeat.isAlive():
            self.heartbeat.stop = True
            self.heartbeat.join()
            self.heartbeat = None
        print("Closed all threads")

    def check_alive(self):
        if self.process and not self.process.poll() and not self.stopped:
            return True
        else:
            print("DEAD")
            self.create()
            return True

    def stdin(self):
        self.check_alive()
        return self.process.stdin

    def stdout(self):
        self.check_alive()
        return self.process.stdout

    def send_command(self, command):
        self.check_alive()
        command = command.upper()
        if command in ['OPEN', 'CLOSE']:
            self._write_command_(command)
            time.sleep(1.5) # wait for a couple of seconds to return
        return {'status': self.last_status.lower().strip()}

    def _write_command_(self, command):
        self.process.stdin.write(command + '\n')
        self.process.stdin.flush()

class InputProcessingThread(Thread):
    def __init__(self, process):
        self.process = process
        Thread.__init__(self)

    def run(self):
        print("starting thread")
        for line in iter(self.process.stdout().readline, ""):
            if len(line) > 1:
                self.process.last_status = line
                line = line.lower().strip()
                print("received: " + line)
                #TODO: add logging
                #TODO: do webhooks (in new thread)
        print("Thread is done")
        self.process.stopped = True


class HeartBeatThread(Thread):
    def __init__(self, process):
        self.process = process
        self.stop = False
        Thread.__init__(self)

    def run(self):
        while not self.stop:
            self.process._write_command_('PING')
            self.process.check_alive()
            time.sleep(5)

def send_command(command):
    global process
    log_action(command)

    response = process.send_command(command)

    return jsonify(response)

process = Process()

# Add signal handler because SlotMachienPC cannot be closed by ctrl+c
def signal_handler(signal, frame):
        global process
        print("SIGINT called")
        process.stdin().close()
        time.sleep(5) # sleep 5 seconds to let it close
        sys.exit(0)
signal.signal(signal.SIGINT, signal_handler)

def log_action(action):
    LogAction.create(auth_key=has_auth_key(), user=has_username(), action=action, logged_on=dt.now())
