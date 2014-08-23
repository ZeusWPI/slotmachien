package slotmachien;

public class TurnToAction extends Action {
    
    private int target;
    private SMMotorHandler smmh;
    
    public TurnToAction(SMMotorHandler smmh, int target) {
        this.target = target;
        this.smmh = smmh;
    }

    public TurnToAction(SMMotorHandler smmh, Status s) {
        TurnToAction(smmh, s.getPosition());
    }

    @Override
    public void performAction() {
        smmh.turnTo(target);
    }
}
