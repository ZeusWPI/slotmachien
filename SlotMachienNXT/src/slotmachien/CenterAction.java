package slotmachien;

import lejos.nxt.LCD;
import lejos.nxt.Sound;
import time.Countdown;
import time.Countdown.Ticker;

public class CenterAction extends Action {
	
    @Override
    public void performAction() {
        new Countdown(10, 1000, new Ticker() {
        	@Override
        	public void doTick(int tick) {
        		LCD.clear();
        		LCD.drawInt(tick, 0, 0);
        		Sound.playTone(800, 300);
        	}
        }, new Action () {
        	@Override
        	public void performAction() {
        		NXTMain.MOTORS.turnTo(NXTMain.POSITION_CLOSED);
        	}
        }.compose(new Action() {
			@Override
			public void performAction() {
				Sound.beepSequenceUp();
			}	
        })).start();
    }
}
