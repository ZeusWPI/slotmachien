package observable;

/**
 * Converts events from a observable to notifications in a observer
 * 
 * @author pietervdvn
 *
 */
public abstract class Mapper<T1 extends Signal, T2 extends Signal> extends
		AbstractObservable<T2> implements Observer<T1> {

	public abstract T2 map(T1 t);

	@Override
	public void notified(T1 signal) {
		T2 t = map(signal);
		if (t != null) {
			notifyObservers(t);
		}
	}

	public static <T extends Signal, T1 extends Signal> void pipe(
			AbstractObservable<T> source, Mapper<T, T1> map, Observer<T1> sink) {
		source.addObserver(map);
		map.addObserver(sink);
	}

}
