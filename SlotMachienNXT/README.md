# SlotMachienNXT

# Usage
* Left Button: Opens the door. Gets on the floor. Everybody walks the dinosaur.
* Right Button: Closes the door.
* Middle Button: Commences a countdown that closes the door when it ends.
* Small middle button: Buzz

# USB: supported commands:

- open
- close
- ping
- status			

Plus a few annoying ones! But these might bite you. Get into the source code to use those :p

Expected format:

    command;person;args
    
Args are allowed to contain ";", command and person are not.

If no args:

    command;person;
    
Returns kan be of the following forms

    status;cause

Where status is either open or closed, and cause is
    p:name   Person + name
    pdc      Pre Delayed Close
    dc       Delayed Close
    bo       Button Open
    bc       Button Close
