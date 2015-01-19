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
	
	
	public UsbParser(SMMotorHandler motor) {
		this.motor = motor;
	}

	@Override
	public void notified(MessageSignal signal) {
		String lower = signal.content.toLowerCase().substring(0,4);
		if(lower == "open"){
			motor.addCommand(new Command(Position.OPEN, signal.content.substring(5)));
		}
		if(lower == "close"){
			motor.addCommand(new Command(Position.CLOSED, signal.content.substring(5)));
		}
		if(lower == "beep"){
			Sound.beep();
		}
	}

}
