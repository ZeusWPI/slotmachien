package time;

import java.util.Timer;
import java.util.TimerTask;

import slotmachien.Action;

public class Countdown {
    private int count;
    private long tick;
    private Ticker ticker;
    private Action action;
    private Timer timer;

    public Countdown(int count, long tick, Ticker ticker, Action action){
        this.count = count;
        this.tick = tick;
        this.ticker = ticker;
        this.action = action;
        this.timer = new Timer();
    }

    public void start(){
        for (int i = 1; i < count; i++){
            timer.schedule(new TickerTask(ticker, i) , i*tick);
        }
        timer.schedule(new TimerTask(){
            public void run(){
                action.performAction();
                timer.cancel();
            }
        }, count*tick);
    }

    public void cancel(){
        timer.cancel();
    }

    // Defines how to act when a tick occurs.
    public interface Ticker {
        public void doTick(int tick);
    }

    // Adapter class for Ticker to TimerTask
    private class TickerTask extends TimerTask {
        private Ticker ticker;
        private int tick;

        public TickerTask(Ticker ticker, int tick){
            this.ticker = ticker;
            this.tick = tick;
        }

        public void run(){
            ticker.doTick(tick);
        }
    }
}

