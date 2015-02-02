package slotmachien.handlers;

import observable.Observer;
import slotmachien.signals.MessageSignal;

/**
 * Prints messages to the screen
 */
public class ScreenHandler implements Observer<MessageSignal>{

    @Override
    public void notified(MessageSignal signal) {
        System.out.println(signal.content);
    }

}
