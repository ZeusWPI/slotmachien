package slotmachien;

import lejos.util.Delay;

public class EscapeAction implements Action {

    private int target;
    private int delay;
    private SMMotorHandler smmh;
    
    public EscapeAction(SMMotorHandler smmh, int target, int delay) {
        this.target = target;
        this.smmh = smmh;
        this.delay = delay;
    }

    @Override
    public void performAction() {
        for (int i = delay; i >0; i++) {
            NXTMain.drawString("Closing in " + i + "s");
            Delay.msDelay(1000);
        }
        smmh.turnTo(target);
    }

}
