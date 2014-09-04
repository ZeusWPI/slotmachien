package observable;

import lejos.nxt.Button;
import lejos.nxt.ButtonListener;

public class ObservableButton extends AbstractObservable {
    
    static ObservableButton knop = new ObservableButton(Button.LEFT);
    public ObservableButton(Button b){
        b.addButtonListener(new NotifyListener());
    }
    
    // adapter class
    class NotifyListener implements ButtonListener {

        @Override
        public void buttonPressed(Button arg0) {
            notifyObservers();
        }

        @Override
        public void buttonReleased(Button arg0) {
            // Do nothing
        }
        
    }

}
