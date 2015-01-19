package observable;

public interface Observer<T extends Signal> {

	public void notified(T signal);
	
}
