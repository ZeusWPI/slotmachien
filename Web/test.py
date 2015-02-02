import random
import time
import sys
import os

from threading import Thread

# This is script with the same functionality as SlotMachienPC
print >> sys.stderr, os.getpid()

responses = ['OPEN', 'CLOSED']
<<<<<<< HEAD
state = random.choice(['open;test;','close;test;'])
print(state)
sys.stdout.flush()
i = 0
while True:
    temp = sys.stdin.readline()
    if temp == "CLOSE":
        state = 'CLOSED'
    if temp == "OPEN":
        state = 'OPEN'
    time.sleep(random.random()*4)
    print(random.choice(['open;test;','close;test;']))
    sys.stdout.flush()
    i+=1
=======

class SimulateDoor(Thread):
    def __init__(self):
        self.state = 'CLOSED'
        super(SimulateDoor, self).__init__()

    def run(self):
        i = 0
        while True:
            time.sleep(random.random()*10)
            self.modify_state(random.choice(responses))

            i+=1

    def modify_state(self, state):
        if self.state in state or self.state in "ping":
            return None
        self.state = state
        print(self.state)
        sys.stdout.flush()
        return state

door = SimulateDoor()
door.setDaemon(True)
door.start()
for line in iter(sys.stdin.readline, ""):
    if len(line) > 1:
        line = line.strip().lower()
        if line in "open":
            door.modify_state("open")
        if line in "closed":
            door.modify_state("closed")
>>>>>>> upstream/web-interface2
