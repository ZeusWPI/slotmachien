package slotmachien.handlers;

import java.io.IOException;

import observable.AbstractObservable;
import observable.Observable;
import observable.Observer;
import slotmachien.internal.UsbIO;
import slotmachien.signals.MessageSignal;
import slotmachien.signals.UsbStatusSignal;
import slotmachien.signals.UsbStatusSignal.UsbStatus;

/**
 * Handles the USB-input, emits neat "usb-signals".
 * 
 * Can observe a other observable. These signals will be sent back over USB.
 * 
 * Automatically reconnects.
 * 
 * @author pietervdvn
 */
public class USBHandler extends AbstractObservable<MessageSignal> implements
		Observer<MessageSignal> {

	private UsbIO io = null;
	private final AbstractObservable<UsbStatusSignal> statusSignaller = new AbstractObservable<UsbStatusSignal>() {
	};

	/**
	 * Does all the stuff! Connects, retries connecting if connection is lost, sends signals to known parties and accepts events to send back over usb.
	 * ...
	 * 
	 * @param clock
	 */
	public USBHandler() {
		statusSignaller.addObserver(new RetryConnection());
		startReading();
	}

	private void startReading() {
		Runnable r = new Runnable() {
			@Override
			public void run() {

				while (io != null) {
					try {
						String read = io.readLine();
						notifyObservers(new MessageSignal(read));
					} catch (Exception e) {
						disconnect();
					}
				}
			}
		};
		new Thread(r).start();
	}

	/**
	 * Attempts connecting
	 */
	private void connect() {
		io = UsbIO.waitForUsbIO();
		statusSignaller
				.notifyObservers(new UsbStatusSignal(UsbStatus.CONNECTED));
		startReading();
	}

	private void disconnect() {
		io = null;
		statusSignaller.notifyObservers(new UsbStatusSignal(
				UsbStatus.DISCONNECTED));
	}

	@Override
	public void notified(MessageSignal signal) {
		if (io == null) {
			System.out.println("No connection: " + signal.content);
			return;
		}
		try {
			io.writeLine(signal.content);
		} catch (IOException e) {
			// something failed -> send disconnect msg
			disconnect();
		}
	}
	
	public void notified(String msg){
		notified(new MessageSignal(msg));
	}
	
	public void notified(String person, String msg){
		notified(new MessageSignal("@"+person+": "+msg));
	}

	public Observable<UsbStatusSignal> getStatusObservable() {
		return statusSignaller;
	}

	/**
	 * Spawns a new thread to connect when connection is down
	 * 
	 * @author pietervdvn
	 *
	 */
	private class RetryConnection implements Observer<UsbStatusSignal> {

		@Override
		public void notified(UsbStatusSignal signal) {

			if (signal.status == UsbStatus.DISCONNECTED) {

				new Thread(new Runnable() {

					@Override
					public void run() {
						connect();
					}
				}).start();
			}
		}
	}

}
