package observable;

import lejos.nxt.Button;
import lejos.nxt.ButtonListener;
import slotmachien.signals.ButtonSignal;

public class ObservableButton extends AbstractObservable<ButtonSignal> {

	public ObservableButton(Button... b) {
		for (Button button : b) {
			button.addButtonListener(new NotifyListener());
		}
	}

	// adapter class
	class NotifyListener implements ButtonListener {

		@Override
		public void buttonPressed(Button arg0) {
			notifyObservers(new ButtonSignal(arg0));
		}

		@Override
		public void buttonReleased(Button arg0) {
			// Do nothing
		}

	}

}
