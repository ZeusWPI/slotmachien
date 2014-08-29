package slotmachien.actions;

import lejos.nxt.Sound;
import time.Countdown;
import time.Countdown.Ticker;

public class DelayedAction extends Action {
	
    private static final int SECOND = 1000;
    private Countdown cd;
    
    public DelayedAction(int delay, Ticker ticker, Action action){
        cd = new Countdown(delay, SECOND, ticker, action);
    }
    
    @Override
    public void perform() {
        if (cd.running()){
            cd.cancel();
        } else {
            cd.start();
        }
    }
}
