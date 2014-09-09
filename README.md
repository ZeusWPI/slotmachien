# SlotMachien
This repository contains all applications related to SlotMachien; a Lego MindStorms robot designed to turn a specific lock.
## Dependencies
[LeJOS NXJ](http://www.lejos.org/nxj.php)
## Setup
[LeJOS NXJ setup tutorial](http://www.lejos.org/nxt/nxj/tutorial/index.htm)
## Components
### SlotMachienNXT
Software running on the MindStorms brick. Communicates with SlotMachienPC using a stable USB connection.

### SlotMachienPC
Software running on the RPi. Communicates with SlotMachienNXT using a stable USB connection. Takes commands from _stdin_ and sends status updates to _stdout_.

### Web
Web server that handles POST requests to open/close the door, or can send the current status of the lock (open/closed/error). Runs on RPi in conjunction with SlotMachienPC.

### Android
Android app that communicates with the webserver to open/close the door remotely.
