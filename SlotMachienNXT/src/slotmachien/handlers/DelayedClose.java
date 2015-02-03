package slotmachien.handlers;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

import lejos.nxt.Sound;
import observable.Observable;
import observable.Observer;
import observable.UnsubscribeMeException;
import slotmachien.internal.Position;
import slotmachien.signals.Command;
import slotmachien.signals.MovedToSignal;
import slotmachien.signals.Signal;

public class DelayedClose implements Observer<Signal> {

    private final SMMotorHandler motor;
    private final int ticks;
    private final Observable<Signal> clock;
    private final DelayedCloser closer = new DelayedCloser();

    private final Command preOpen = new Command(Position.OPEN, "pdc");

    private final Observer<Signal> canceller = new Observer<Signal>() {
        public void notified(Signal signal) {
            boolean signalIsPreOpen = false;

            if (signal instanceof MovedToSignal) {
                MovedToSignal mts = (MovedToSignal) signal;
                signalIsPreOpen = mts.causedBy == preOpen;
            }
            closer.cancelled = !signalIsPreOpen;
        }
    };

    /**
     * Adds a delayed close action each time the signal is given.
     * 
     * @param motor
     *            : the motor handler
     * @param clock
     *            : the clock which is used for ticking the seconds
     * @param ticks
     *            : the number of clockticks needed to close
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

        if (closer.ticks > 0) {
            // closing in progress, cancel it!
            canceller.notified(signal);
            return;
        }

        if (motor.getState().pos != Position.OPEN) {
            motor.notified(preOpen);
        }

        // lets reset and activate the actual closer!
        closer.cancelled = false;
        closer.ticks = ticks;
        clock.addObserver(closer);

    }

    // Add this observer to any input. When this input is signalled, the delayed
    // close will be cancelled
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
            if (cancelled) {
                System.out.println("Cancelled");
                cancelled = false;
                ticks = 0; // set ticks on zero, to indicate halting
                throw new UnsubscribeMeException();
            }

            if (ticks <= 0) {
                motor.notified(new Command(Position.CLOSED, "dc"));
                throw new UnsubscribeMeException();
            }
            System.out.println(ticks);
            File f = new File(ticks + ".lni");
            try {
                new Graphics().drawImage(
                        Image.createImage(new FileInputStream(f)), 0, 0, 0);
            } catch (IOException e) {
            }

            Sound.beep();
            ticks--;
        }
    }

}
