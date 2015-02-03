package observable;

import slotmachien.signals.Signal;
import lejos.util.*;

/**
 * Sends out a signal every _t_ ms to all observers.
 * @author pietervdvn
 *
 */
public class PeriodicSignal extends Observable<Signal>{
	
	public PeriodicSignal(int millis) {
		Timer t = new Timer(millis, new TimerListener() {
            @Override
            public void timedOut() {
                notifyObservers(new Signal());
            }
        });
		t.start();
	}

}
