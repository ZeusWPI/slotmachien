package slotmachien;

import lejos.nxt.Button;
import lejos.nxt.Sound;
import observable.ButtonSignal;
import observable.Observer;
import time.Countdown;

/**
 * 
 * Listens to buttons, converts them into events for the motor handler
 * 
 * @author don
 *
 */
public class ButtonHandlers implements Observer<ButtonSignal> {

    private final SMMotorHandler handler;

    private static final int SECOND = 1000;

    public ButtonHandlers(SMMotorHandler handler) {
        this.handler = handler;
    }

    @Override
    public void notified(ButtonSignal signal) {
        Sound.playTone(800, 100);
        
        switch (signal.source.getId()) {
        case Button.ID_LEFT:
            handler.addCommand(new Command(Position.OPEN, "Button open"));
            break;
        case Button.ID_RIGHT:
            handler.addCommand(new Command(Position.CLOSED, "Button close"));
            break;
        case Button.ID_ENTER:
            handler.addCommand(new Command(Position.OPEN, "Open before delayed close"));
            new Countdown(10, SECOND, new Countdown.Ticker() {
                @Override
                public void doTick(int tick) {
                    Sound.beep();
                }
            }, new Runnable() {
                @Override
                public void run() {
                    handler.addCommand(new Command(Position.CLOSED, "Delayed close"));
                }
            }).start();
            break;
        }
    }

}
