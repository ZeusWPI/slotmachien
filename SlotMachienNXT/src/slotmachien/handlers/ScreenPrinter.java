package slotmachien.handlers;

import observable.Observer;
import slotmachien.signals.MovedToSignal;

/**
 * 
 * Prints changes to the screen
 * @author don
 *
 */
public class ScreenPrinter implements Observer<MovedToSignal>{

    @Override
    public void notified(MovedToSignal signal) {
        String state = signal.pos.toString().substring(0,1);
        System.out.println(state + " " + signal.comment);
    }

}
