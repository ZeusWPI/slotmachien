package observable;

public interface Observable<T extends Signal> {

	public void addObserver(Observer<T> o);
	public void removeObserver(Observer<T> o);
	public int observerCount();
}
