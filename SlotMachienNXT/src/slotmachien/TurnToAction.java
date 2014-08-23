package slotmachien;

public class TurnToAction extends Action {
    
    private Status status;
    private SMMotorHandler smmh;
    
    public TurnToAction(SMMotorHandler smmh, Status status) {
        this.smmh = smmh;
        this.status = status;
    }

    @Override
    public void performAction() {
        smmh.turnTo(status);
    }
}
