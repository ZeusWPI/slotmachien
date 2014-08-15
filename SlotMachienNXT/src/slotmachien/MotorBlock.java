package slotmachien;
import lejos.nxt.NXTRegulatedMotor;

public class MotorBlock {

    private NXTRegulatedMotor[] motors;

    public MotorBlock(NXTRegulatedMotor... motors) {
        this.motors = motors;
    }
    
    public void setStallThreshold(int angle, int delay) {
        for (NXTRegulatedMotor m : motors) {
            m.setStallThreshold(angle, delay);
        }
    }

    public void setSpeed(int speed) {
        for (NXTRegulatedMotor m : motors) {
            m.setSpeed(speed);
        }
    }

    public int getTachoCount() {
        return motors[0].getTachoCount();
    }

    public void rotateTo(int turn, boolean b) {
        for (NXTRegulatedMotor m : motors) {
            m.rotateTo(turn, true);
        }

        if(!b) { motors[0].waitComplete(); }
    }

    public void flt() {
        for (NXTRegulatedMotor m : motors) {
            m.flt();
        }
    }

    public void forward() {
        for (NXTRegulatedMotor m : motors) {
            m.forward();
        }
    }
    
    public void resetTachoCount() {
        for (NXTRegulatedMotor m : motors) {
            m.resetTachoCount();
        }
    }
    
    public boolean isMoving() {
        for (NXTRegulatedMotor m : motors) {
            if(!m.isMoving()) {
                return false;
            }
        }
        return true;
    }
    
    public boolean isStalled() {
        for (NXTRegulatedMotor m : motors) {
            if(m.isStalled()) {
                return true;
            }
        }
        return false;
    }

}
