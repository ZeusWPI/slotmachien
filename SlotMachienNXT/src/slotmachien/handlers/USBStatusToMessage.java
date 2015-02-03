package slotmachien.handlers;

import observable.Mapper;
import slotmachien.signals.MessageSignal;
import slotmachien.signals.UsbStatusSignal;

public class USBStatusToMessage extends Mapper<UsbStatusSignal, MessageSignal>{

	@Override
	public MessageSignal map(UsbStatusSignal t) {
		if(t != null){
			return new MessageSignal("usb", t.status.toString());
		}
		return null;
	}

}
