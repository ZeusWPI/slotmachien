package slotmachien.signals;

import observable.Signal;

/**
 * Has a string, which is a single line read from Usb.
 * 
 * @author pietervdvn
 *
 */
public class UsbSignal extends Signal{

	public final String content;

	public UsbSignal(String content) {
		this.content = content;
	}
}
