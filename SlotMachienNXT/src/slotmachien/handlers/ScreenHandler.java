package slotmachien.handlers;

import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Screen;

import observable.Observer;
import slotmachien.signals.MessageSignal;

/**
 * Prints messages to the screen
 */
public class ScreenHandler implements Observer<MessageSignal> {

    public static final boolean DEBUG = false;
    Screen[] screens;

    @Override
    public void notified(MessageSignal signal) {
        if (DEBUG) {
            System.out.println(signal.content);
        }
        Graphics g = new Graphics();
        
        if(signal.content.startsWith("open")) {
            g.drawImage(Images.OPEN, 0, 0, 0);
        } else if (signal.content.startsWith("close")) {
            g.drawImage(Images.CLOSED, 0, 0, 0);
        }
    }

    public void nextScreen() {

    }

}