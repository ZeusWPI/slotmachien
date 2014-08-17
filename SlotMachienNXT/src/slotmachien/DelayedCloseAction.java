package slotmachien;

import lejos.nxt.Button;
import lejos.util.Delay;

public class DelayedCloseAction implements Action {

    private int target;
    private int delay;
    private SMMotorHandler smmh;

    public DelayedCloseAction(SMMotorHandler smmh, int target, int delay) {
        this.target = target;
        this.smmh = smmh;
        this.delay = delay;
    }

    @Override
    public void performAction() {
        while (!Button.ESCAPE.isDown()) {
            for (int i = delay; i > 0; i++) {
                NXTMain.drawString("closing in " + i + "s");
                Delay.msDelay(1000);
            }
            smmh.turnTo(target);
        }
        if (Button.ESCAPE.isDown()) {
            NXTMain.drawString("cancelled");
        }
    }

}
