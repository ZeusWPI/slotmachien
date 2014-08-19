package slotmachien;

import lejos.nxt.LCD;
import time.Countdown;
import time.Countdown.Ticker;

public class CenterAction extends Action {
	
    @Override
    public void performAction() {
    	
        Countdown cd = new Countdown(10, 1000, new Ticker() {
        	@Override
        	public void doTick(int tick) {
        		LCD.clear();
        		LCD.drawInt(tick, 0, 0);
        	}
        }, new Action () {
        	@Override
        	public void performAction() {
        		NXTMain.MOTORS.turnTo(NXTMain.POSITION_CLOSED);
        	}
        });
        cd.start();
    }
}
