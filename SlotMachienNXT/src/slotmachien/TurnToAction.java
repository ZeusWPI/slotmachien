package slotmachien;

public class TurnToAction implements Action{
    
    private int target;
    private SMMotorHandler smmh;
    
    public TurnToAction(SMMotorHandler smmh, int target) {
        this.target = target;
        this.smmh = smmh;
    }

    @Override
    public void performAction() {
        smmh.turnTo(target);
    }
}
