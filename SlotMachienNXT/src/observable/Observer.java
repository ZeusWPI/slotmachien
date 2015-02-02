package observable;

import slotmachien.signals.UnsubscribeMeException;

public interface Observer<T extends Signal> {

	/**
	 * When a {@link UnsubscribeMeException} gets thrown, you get unsubscribed
	 * @param signal
	 * @return
	 */
	public void notified(T signal) throws UnsubscribeMeException;
	
}
