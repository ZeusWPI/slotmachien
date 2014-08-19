package slotmachien;

import static slotmachien.NXTMain.MOTORS;
import lejos.util.Delay;

public class ManualChecker implements Runnable {

	@Override
	public void run() {
		Status lastStatus = MOTORS.getStatus();
		while(true) {
			Status s = MOTORS.getStatus();
			if (s != lastStatus && s != Status.DEADZONED) {
				lastStatus = s;
				MOTORS.notifyObservers();
			}
			Delay.msDelay(500);
		}
	}

}
