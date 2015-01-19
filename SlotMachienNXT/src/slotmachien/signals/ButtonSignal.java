package slotmachien.signals;

import observable.Signal;
import lejos.nxt.Button;

public class ButtonSignal extends Signal {
    public final Button source;

    public ButtonSignal(Button source) {
        this.source = source;
    }
}
