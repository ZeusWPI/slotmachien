package slotmachien.handlers;

import java.io.File;

import lejos.nxt.Sound;
import observable.Observer;
import slotmachien.internal.Position;
import slotmachien.signals.Command;
import slotmachien.signals.MessageSignal;
import slotmachien.signals.UnsubscribeMeException;

/**
 * Gets USB-signals, parses them, executes them
 * 
 * @author pietervdvn
 *
 */
public class UsbParser implements Observer<MessageSignal> {

	private final SMMotorHandler motor;
	private final USBHandler usb;

	public UsbParser(SMMotorHandler motor, USBHandler usb, ScreenHandler screen) {
		this.motor = motor;
		this.usb = usb;
	}

	@Override
	public void notified(MessageSignal signal) throws UnsubscribeMeException {

		try {
			parse(signal);
		} catch (Exception e) {
			usb.notified(new MessageSignal("Parsing failed: " + signal.content));
		}
	}

	public void parse(MessageSignal signal) throws UnsubscribeMeException {
		String sep = ";";
		String lower = signal.content.toLowerCase();
		String comm = lower.substring(0, lower.indexOf(sep));
		String person = lower.substring(lower.indexOf(sep) + 1);

		if (comm.equals("open")) {
			motor.addCommand(new Command(Position.OPEN, "p:" + person));

		}
		
		if (comm.equals("close")) {
			motor.addCommand(new Command(Position.CLOSED,"p:" + person));
		}

	}

}
