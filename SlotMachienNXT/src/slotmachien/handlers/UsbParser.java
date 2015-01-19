package slotmachien.handlers;

import lejos.nxt.Sound;
import observable.Observer;
import slotmachien.internal.Position;
import slotmachien.signals.Command;
import slotmachien.signals.MessageSignal;

/**
 * Gets USB-signals, parses them, executes them
 * @author pietervdvn
 *
 */
public class UsbParser implements Observer<MessageSignal>{
	
	private final SMMotorHandler motor;
	private final USBHandler usb;
	
	
	public UsbParser(SMMotorHandler motor, USBHandler usb) {
		this.motor = motor;
		this.usb = usb;
	}

	@Override
	public void notified(MessageSignal signal) {
		String lower = signal.content.toLowerCase();
		if(lower.startsWith("open")){
			motor.addCommand(new Command(Position.OPEN, signal.content.substring(5)));
		}
		if(lower.startsWith("close")){
			motor.addCommand(new Command(Position.CLOSED, signal.content.substring(6)));
		}
		if(lower.startsWith("beep")){
			Sound.beep();
			System.out.println(signal.content.substring(5));
		}
		if(lower.startsWith("ping")){
			usb.notified(new MessageSignal("pong"));
		}
		if(lower.startsWith("status")){
			usb.notified(new MessageSignal("Status: "+motor.getState().toString()));
		}
	}

}
