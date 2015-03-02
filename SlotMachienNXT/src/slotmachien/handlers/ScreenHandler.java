package slotmachien.handlers;

import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

import observable.Observer;
import slotmachien.signals.MessageSignal;

/**
 * Prints messages to the screen
 */
public class ScreenHandler implements Observer<MessageSignal> {

    static Graphics g = new Graphics();
    static Image lastImage = Images.ZEUS_LOGO;
    
    @Override
    public void notified(MessageSignal signal) {
        String lowerhead = signal.head.toLowerCase();
        if (lowerhead.startsWith("open")) {
            if (!signal.body.startsWith("pdc")) {
                displayImage(Images.OPEN);
            }
        } else if (lowerhead.startsWith("close")) {
            displayImage(Images.CLOSED);
        } else {
            displayImage(Images.ZEUS_LOGO);
        }
    }

    public static void displayImage(Image i) {
        g.drawImage(i, 0, 0, 0);
        lastImage = i;
    }
    
    public static void redraw() {
        g.clear();
        displayImage(lastImage);
    }

}