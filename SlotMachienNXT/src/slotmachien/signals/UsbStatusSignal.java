package slotmachien.signals;

import observable.Signal;

public class UsbStatusSignal extends Signal{
	
	public final UsbStatus status;
	
	public UsbStatusSignal(UsbStatus status) {
		this.status = status;
	}
	
	static public enum UsbStatus{
		CONNECTED, DISCONNECTED
	}

}
