from datetime import datetime as dt
from subprocess import Popen, PIPE
from threading import Thread
import threading
import json
import time
import signal
import sys

import requests

from app import app, db, logger
from auth import has_slack_token, get_user
from models import LogAction


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
        self.heartbeat = HeartBeatThread(self)
        self.heartbeat.setDaemon(True)
        self.heartbeat.start()

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

    def stdin(self):
        if not self.check_alive():
            self.create()

        return self.process.stdin

    def stdout(self):
        if not self.check_alive():
            self.create()

        return self.process.stdout

    def send_command(self, command):
        if not self.check_alive():
            self.create()
        command = command.upper()
        if command in ['OPEN', 'CLOSE']:
            self._write_command_(command)
            time.sleep(1.5)  # wait for a couple of seconds to return
        return {'status': self.last_status.lower().strip()}

    def _write_command_(self, command):
        if self.write_lock != None:
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
                logger.info("Door status changed: %s" % (line))
                self.webhooks(line)
                # TODO: do webhooks (in new thread)
        logger.info('Input processing thread stopped')
        self.process.stopped = True

    def webhooks(self, text):
        js = json.dumps({'text': text})
        url = app.config['SLACK_WEBHOOK']
        if len(url) > 0:
            requests.post(url, data=js)

    def clean_status(self, status):
        status = status.lower().strip()
        if status in ["open", "closed"]:
            return status

        if "NXT" in status:
            return "NXT Error"

        logger.error("Door status inconsistent: %s" % (status))



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

    response = process.send_command(command)

    return response

process = Process()


# Add signal handler because SlotMachienPC cannot be closed by ctrl+c
def signal_handler(signal, frame):
        global process
        logger.info("SIGINT called, stopping the program")
        process.stdin().close()
        process.clean_process()
        #process.inputProcessing.join()
        sys.exit(0)

signal.signal(signal.SIGINT, signal_handler)


def log_action(action):
    logger.info("User %s:%s" % (get_user(), action))
    if action not in ["status"]:
        logaction = LogAction()
        logaction.configure(has_slack_token(), get_user(), action, dt.now())
        db.session.add(logaction)
        db.session.commit()
