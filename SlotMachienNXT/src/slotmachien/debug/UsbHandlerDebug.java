package slotmachien.debug;

import observable.Observer;
import slotmachien.handlers.USBHandler;
import slotmachien.signals.MessageSignal;
import slotmachien.signals.UnsubscribeMeException;
import slotmachien.signals.UsbStatusSignal;

/**
 * Debugs the handler
 * @author pietervdvn
 *
 */
public class UsbHandlerDebug {
	
	public static void main() {
		System.out.println("USB handler debug!");
		USBHandler.debug = true;
		USBHandler handler = new USBHandler();
		handler.addObserver(new Observer<MessageSignal>() {
			
			@Override
			public void notified(MessageSignal signal) throws UnsubscribeMeException {
				System.out.println("GOT: "+signal.content);
			}
		});
		
		handler.getStatusObservable().addObserver(new Observer<UsbStatusSignal>() {
			
			@Override
			public void notified(UsbStatusSignal signal) throws UnsubscribeMeException {
				System.out.println("St: "+signal.status.toString());
			}
		});
		
	}

}
