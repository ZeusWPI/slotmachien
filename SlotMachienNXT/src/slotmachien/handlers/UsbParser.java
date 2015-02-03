package slotmachien.handlers;

import observable.Observer;
import observable.UnsubscribeMeException;
import slotmachien.internal.Position;
import slotmachien.signals.Command;
import slotmachien.signals.MessageSignal;
import slotmachien.signals.StringSignal;

/**
 * Gets USB-signals, parses them, executes them
 * 
 * @author pietervdvn
 *
 */
public class UsbParser implements Observer<StringSignal> {

	private final SMMotorHandler motor;
	private final USBHandler usb;

	public UsbParser(SMMotorHandler motor, USBHandler usb, ScreenHandler screen) {
		this.motor = motor;
		this.usb = usb;
	}

	@Override
	public void notified(StringSignal signal) throws UnsubscribeMeException {

		try {
			parse(signal);
		} catch (Exception e) {
			usb.notified(new MessageSignal("parsefail", signal.content));
		}
	}

	public void parse(StringSignal signal) throws UnsubscribeMeException {
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
