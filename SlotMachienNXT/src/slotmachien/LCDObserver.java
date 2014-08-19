package slotmachien;

import lejos.nxt.LCD;
import lejos.nxt.Sound;

public class LCDObserver implements Observer {

	@Override
	public void invalidated() {
		LCD.clear();
		LCD.drawString(NXTMain.MOTORS.getStatus().toString(), 0, 0);
		Sound.twoBeeps();
	}

}
