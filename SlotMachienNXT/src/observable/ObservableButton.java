package observable;

import lejos.nxt.Button;
import lejos.nxt.ButtonListener;

public class ObservableButton extends AbstractObservable<ButtonSignal> {

    private final Button b;
    
    public ObservableButton(Button b){
        this.b = b;
        b.addButtonListener(new NotifyListener());
    }
    
    // adapter class
    class NotifyListener implements ButtonListener {

        @Override
        public void buttonPressed(Button arg0) {
            notifyObservers(new ButtonSignal(b));
        }

        @Override
        public void buttonReleased(Button arg0) {
            // Do nothing
        }
        
    }

}
