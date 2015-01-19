package observable;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import slotmachien.signals.UnsubscribeMeException;

public abstract class AbstractObservable<T extends Signal> implements
		Observable<T> {

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

	@Override
	public void addObserver(Observer<T> o) {
		observers.add(o);
	}

	@Override
	public void removeObserver(Observer<T> o) {
		observers.remove(o);
	}

	@Override
	public int observerCount() {
		return observers.size();
	}

}
