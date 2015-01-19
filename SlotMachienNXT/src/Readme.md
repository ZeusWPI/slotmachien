Structure:
==========

- Observable: Reactive programming primitives: observer, observable + generic utils (PeriodicSignal)
- Slotmachien: main, position, command
   + signals: signals sent by various sources (SMMotorHandler sends MovedTo, ButtonHandler sends Button)
   + Handlers: handles outside world stuff: Buttons, screens, usbconnections, motors
   + internal: actual, low level motor control. Should be used by SMMotorHandler ONLY!
   
   
  
Actual program structure:
See NXTMain, where all the events are piped in the right direction.