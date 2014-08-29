package slotmachien.actions;

import slotmachien.SMMotorHandler;
import slotmachien.Status;

public class TurnToAction extends Action {
    
    private int target;
    private SMMotorHandler smmh;
    
    public TurnToAction(SMMotorHandler smmh, int target) {
        this.smmh = smmh;
        this.target = target;
    }

    @Override
    public void perform() {
        smmh.turnTo(target);
    }
}
