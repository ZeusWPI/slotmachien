package slotmachien.handlers;

import lejos.nxt.Sound;
import observable.Observable;
import observable.Observer;
import observable.Signal;
import slotmachien.internal.Position;
import slotmachien.signals.Command;
import slotmachien.signals.UnsubscribeMeException;

public class DelayedClose implements Observer<Signal> {

	private final SMMotorHandler motor;
	private final int ticks;
	private final Observable<Signal> clock;
	private final DelayedCloser closer = new DelayedCloser();
	private final Observer<Signal> canceller = new Observer<Signal>() {
		public void notified(Signal signal){
			closer.cancelled = true;
		}
	};
	/**
	 * Adds a delayed close action each time the signal is given.
	 * 
	 * @param motor: the motor handler
	 * @param clock: the clock which is used for ticking the seconds
	 * @param ticks: the number of clockticks needed to close
	 */
	public DelayedClose(SMMotorHandler motor, Observable<Signal> clock,
			int ticks) {
		this.motor = motor;
		this.ticks = ticks;
		this.clock = clock;
		motor.addObserverDangerous(canceller);
	}

	@Override
	public void notified(Signal signal) throws UnsubscribeMeException {
		if(closer.ticks > 0){
			// closer is working, lets cancel him
			closer.cancelled = true;
			return;
		}
		
		if (motor.getState().pos != Position.OPEN) {
			motor.notified(new Command(Position.OPEN,
					"Opening pre-delayed close"));
		}else{
			motor.notified(new Command(Position.CLOSED, "Delayed close activated!"));
		}
		closer.cancelled = false;
		closer.ticks = ticks;
		
		clock.addObserver(closer);
		
	}

	// Add this observer to any input. When this input is signalled, the delayed close will be cancelled
	public Observer<Signal> getCanceller() {
		return canceller;
	}

	/**
	 * Actual delayed close
	 * 
	 * @author pietervdvn
	 *
	 */
	private class DelayedCloser implements Observer<Signal> {

		int ticks;
		boolean cancelled;

		@Override
		public void notified(Signal signal) throws UnsubscribeMeException {
			if(cancelled){
				throw new UnsubscribeMeException();
			}
			
			if (ticks <= 0) {
				motor.notified(new Command(Position.CLOSED, "Delayed close"));
				throw new UnsubscribeMeException();
			}
			motor.notified(new Command(Position.CLOSED, "Delayed close: "+ticks+"!"));
			ticks--;
			Sound.beep();
		}
	}

}
