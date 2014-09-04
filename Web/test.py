import random
import time
import sys
import os

# This is script with the same functionality as SlotMachienPC
print >> sys.stderr, os.getpid()

responses = ['OPEN', 'CLOSED']
state = random.choice(responses)
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
    print(random.choice(responses))
    sys.stdout.flush()
    i+=1
