package observable;

/**
 * Sends out a signal every _t_ ms to all observers.
 * @author pietervdvn
 *
 */
public class PeriodicSignal extends AbstractObservable<Signal>{
	
	private final int millis;
	
	public PeriodicSignal(int millis) {
		this.millis = millis;
		new Thread(new Ticker()).start();
	}
	
	
	private class Ticker implements Runnable{

		@Override
		public void run() {
			while(true){
				try {
					Thread.sleep(millis);
				} catch (InterruptedException e) {
				}
				notifyObservers(new Signal());
			}
		}
	}
	
	

}
