package slotmachien;

import lejos.nxt.Button;
import lejos.nxt.ButtonListener;

// 'Adapter' from Action to ButtonListener
public class ActionButtonListener implements ButtonListener {
	private Action action;
	
	public ActionButtonListener(Action action){
		this.action = action;
	}

	@Override
	public void buttonPressed(Button b) {
		action.performAction();
		
	}

	@Override
	public void buttonReleased(Button b) {
		// LOLOLOL DOE NIETS
		
	}
	
}
