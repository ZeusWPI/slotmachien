package slotmachien.signals;

import observable.Signal;

public class UsbStatusSignal extends Signal{
	
	public final UsbStatus status;
	
	public UsbStatusSignal(UsbStatus status) {
		this.status = status;
	}
	
	public static enum UsbStatus {
		CONNECTED, DISCONNECTED
	}

}
