package slotmachien.handlers;

import observable.Observer;
import slotmachien.signals.MovedToSignal;
import slotmachien.signals.UsbSignal;

/**
 * 
 * Prints changes to usb!
 * @author pietervdvn
 *
 */
public class UsbPrinter implements Observer<MovedToSignal> {
	
	private final USBHandler usb;
	
	public UsbPrinter(USBHandler usb) {
		this.usb = usb;
	}

	@Override
	public void notified(MovedToSignal signal) {
		String content = signal.pos.toString()+": "+signal.comment;
		usb.notified(new UsbSignal(content));
	}

}
