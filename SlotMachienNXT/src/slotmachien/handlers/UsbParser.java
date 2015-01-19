package slotmachien.handlers;

import java.io.File;

import lejos.nxt.Sound;
import observable.Observer;
import slotmachien.internal.Position;
import slotmachien.signals.Command;
import slotmachien.signals.MessageSignal;

/**
 * Gets USB-signals, parses them, executes them
 * 
 * @author pietervdvn
 *
 */
public class UsbParser implements Observer<MessageSignal> {

	private final SMMotorHandler motor;
	private final USBHandler usb;

	public UsbParser(SMMotorHandler motor, USBHandler usb) {
		this.motor = motor;
		this.usb = usb;
	}

	@Override
	public void notified(MessageSignal signal) {
		String sep	= ";";
		String lower = signal.content.toLowerCase();
		String comm = lower.substring(0, lower.indexOf(sep));
		lower = lower.substring(lower.indexOf(sep) + 1);
		String person = lower.substring(0, lower.indexOf(sep));
		lower = lower.substring(lower.indexOf(sep) + 1);
		
		
		if(comm.startsWith("imitate") && person.startsWith("pietervdvn")){
			notified(new MessageSignal(lower));
			usb.notified("pietervdvn","The imitation game: "+lower);
			return;
		}
		

		if (comm.startsWith("open")) {
			motor.addCommand(new Command(Position.OPEN, person));
			
		} else if (comm.startsWith("close")) {
			motor.addCommand(new Command(Position.CLOSED, person));
			
		} else if (comm.startsWith("beep")) {
			Sound.beep();
			usb.notified("channel", "@" + person
					+ " annoyed everyone by beeping!");
			return;
			
		} else if (comm.startsWith("buzz")) {
			Sound.buzz();
			usb.notified("channel", "@" + person
					+ " annoyed everyone by buzzing!");
			return;
			
		} else if (comm.startsWith("ping")) {
			usb.notified(person, "pong");
			
		} else if (comm.startsWith("status")) {
			usb.notified(person, "Status: " + motor.getState().toString());
			
		} else if (comm.startsWith("sound")) {
			try {
				Sound.playSample(new File(lower));
			} catch (Exception e) {
				usb.notified(person, "Playing " + lower + " failed");
			}
		} else{
			
			// command not found
			usb.notified(person, "Command "+signal.content+" failed. Ask pietervdvn!");
			
		}

		if (person.equals("tlelaxu") || person.equals("tleilaxu")) {
			Sound.beepSequence();
			usb.notified("channel", "LEL");
		}

		if (person.equals("felikaan")) {
			usb.notified("channel", "TIS KAPOT! MAAR... MAAR...");
		}

		if (person.equals("iasoon")) {
			usb.notified("channel", "HASKELL IS THE MAX!");
		}
		
		if(person.equals("don")){
			usb.notified("channel", "Dikke wubs");
			Sound.playNote(Sound.FLUTE	, 880, 500);
			Sound.playNote(Sound.PIANO	, 440, 500);
			Sound.playNote(Sound.FLUTE	, 440, 500);
			Sound.playNote(Sound.PIANO	, 880, 500);
		}

	}

}
