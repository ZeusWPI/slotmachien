package observable;

public interface Observable<T extends Signal> {

	public void notifyObservers(T signal);
	
	public void addObserver(Observer<T> o);
	
	public void removeObserver(Observer<T> o);
	
	public int observerCount();
}
