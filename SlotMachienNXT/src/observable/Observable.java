package observable;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import slotmachien.signals.UnsubscribeMeException;

public class Observable<T extends Signal> {

	private List<Observer<T>> observers = new ArrayList<Observer<T>>();

	public void notifyObservers(T signal) {
		for (Iterator<Observer<T>> iterator = observers.iterator(); iterator
				.hasNext();) {
			try {
				iterator.next().notified(signal);
			} catch (UnsubscribeMeException e) {
				iterator.remove();
			}

		}
	}

	public void addObserver(Observer<T> o) {
		observers.add(o);
	}
	
	/**
	 * Adds observer of which the subtype doesn't exactly match
	 * @param o
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void addObserverDangerous(Observer<Signal> o) {
		addObserver((Observer) o);
	}

	public void removeObserver(Observer<T> o) {
		observers.remove(o);
	}

	public int observerCount() {
		return observers.size();
	}

}
