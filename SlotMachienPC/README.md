# SlotMachienPC
===============

1. Follow the [LeJOS NXJ setup tutorial](http://www.lejos.org/nxt/nxj/tutorial/index.htm)
2. Modify the udev rules, in `/etc/udev/rules.d/` add an file: `70-lego.rules` with the contents:
```
SUBSYSTEM=="usb", ATTRS{idVendor}=="0694", GROUP="slotmachien", MODE="0660"
```
