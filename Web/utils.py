from datetime import datetime as dt
from subprocess import Popen, PIPE
from threading import Thread
from functools import wraps
import threading
import json
import time
import signal
import sys

import requests
from flask.ext.login import current_user


from app import app, db, logger
from models import LogAction, User


def is_alive(f):  # decorater for the Process class
    @wraps(f)
    def decorated(self, *args, **kwargs):
        if not self.check_alive():
            self.create()

        return f(self, *args, **kwargs)
    return decorated


class Process:

    def __init__(self):
        self.process = None
        self.inputProcessing = None
        self.heartbeat = None
        self.write_lock = None
        self.stopped = False
        self.last_status = ''
        self.create()

    def create(self):
        self.clean_process()
        self.process = Popen([app.config['PROCESS']], stdin=PIPE, stdout=PIPE,
                             shell=True)

        logger.info('SlotMachienPC pid: %d' % self.process.pid)
        self.stopped = False
        self.last_status = self.process.stdout.readline()

        # Create input processing thread
        self.inputProcessing = InputProcessingThread(self)
        self.inputProcessing.setDaemon(True)
        self.inputProcessing.start()

        # Create heartbeat thread
        # TEMPORARY DISABLE HEARTBEAT BECAUSE SLOTMACHIENNXT CANNOT LIVE WITH IT
        #self.heartbeat = HeartBeatThread(self)
        #self.heartbeat.setDaemon(True)
        #self.heartbeat.start()

        self.write_lock = threading.Lock()
        logger.info("Started all threads")

    def clean_process(self):
        logger.info("Started cleaning")
        if self.process and not self.process.poll():
            self.process.stdin.close()
            self.process.stdout.close()
            try:
                self.process.terminate()
            except OSError:
                logger.warn("No process to terminate")
            self.process = None

        if self.inputProcessing and self.inputProcessing.isAlive():
            self.inputProcessing = None
            # does not need to be stopped because stdin is closed

        if self.heartbeat and self.heartbeat.isAlive():
            self.heartbeat.stop = True
            self.heartbeat = None
        logger.info("Closed all threads")

    def check_alive(self):
        if not self.process or self.process.poll() or self.stopped:
            logger.info("Java process went down")
            return False
        return True

    @is_alive
    def stdin(self):
        return self.process.stdin

    @is_alive
    def stdout(self):
        return self.process.stdout

    @is_alive
    def send_command(self, command):
        command = command.upper()
        if command in ['OPEN', 'CLOSE'] \
            and command not in self.last_status.upper():
            command = command + ';' + current_user.username
            self._write_command_(command)
            time.sleep(0.75)  # wait for a couple of seconds to return
        return {'status': self.last_status.lower().strip()}

    def _write_command_(self, command):
        if self.write_lock is not None:
            self.write_lock.acquire()
            self.process.stdin.write(command + '\n')
            self.process.stdin.flush()
            self.write_lock.release()


class InputProcessingThread(Thread):

    def __init__(self, process):
        super(InputProcessingThread, self).__init__()
        self.process = process

    def run(self):
        logger.info('Starting the input processing thread')
        for line in iter(self.process.stdout().readline, ""):
            if len(line) > 1:
                old_line = line
                line = self.clean_status(line)
                self.process.last_status = line
                logger.info("Door status changed to %s" % (line))
                webhookthread = WebhookSenderThread(line)
                webhookthread.start()
        logger.info('Input processing thread stopped')
        self.process.stopped = True

    def clean_status(self, status):
        status = status.lower().strip()
        if status in ["open", "closed"]:
            return status

        if ';' in status:
            # new kind
            parsed_status = status.split(';')
            action = parsed_status[0]
            by = 'manual'
            if 'p:' in parsed_status[1]:
                # user
                username = parsed_status[1].split(':')[1]
                by = username
            elif parsed_status[1] in ['pdc', 'dc', 'bo', 'bc']:
                by = 'buttons'
            return '%s by %s' % (action, by)

        if "nxt" in status:
            return "NXT Error"

        logger.error("Door status inconsistent: %s" % (status))
        return "error: contact sysadmins"

class WebhookSenderThread(Thread):

    def __init__(self, message):
        super(WebhookSenderThread, self).__init__()
        self.message = message

    def run(self):
        self.slack_webhook()

    def slack_webhook(self):
        js = json.dumps({'text': self.message})
        url = app.config['SLACK_WEBHOOK']
        if len(url) > 0:
            requests.post(url, data=js)

class HeartBeatThread(Thread):

    def __init__(self, process):
        super(HeartBeatThread, self).__init__()
        self.process = process
        self.stop = False

    def run(self):
        logger.info('Starting the heartbeat thread')
        while not self.stop:
            self.process._write_command_('PING')
            self.process.check_alive()
            time.sleep(5)
        logger.info('Stopping the heartbeat thread')


def send_command(command):
    global process
    log_action(command)

    if process is None:
        start_process()

    response = process.send_command(command)

    return response


def start_process():
    global process
    process = Process()

process = None

# Add signal handler because SlotMachienPC cannot be closed by ctrl+c


def signal_handler(signal, frame):
    global process
    logger.info("SIGINT called, stopping the program")
    process.stdin().close()
    process.clean_process()
    # process.inputProcessing.join()
    sys.exit(0)

signal.signal(signal.SIGINT, signal_handler)


def log_action(action):
    logger.info("User %s:%s" % (current_user, action))
    if action not in ["status"]:
        logaction = LogAction()
        logaction.configure(current_user, action, dt.now())
        db.session.add(logaction)
        db.session.commit()
