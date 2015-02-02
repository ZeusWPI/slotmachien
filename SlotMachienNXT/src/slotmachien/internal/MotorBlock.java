package slotmachien.internal;
import lejos.nxt.NXTRegulatedMotor;

/**
 * Internal motor block handling, used by SMMotorHandler
 */
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
    
    /** The motorblock is moving only when both motors are moving
     * 
     */
    public boolean isMoving() {
        for (NXTRegulatedMotor m : motors) {
            if(!m.isMoving()) {
                return false;
            }
        }
        return true;
    }
    
    /** The motorblock is stalled when at least one motor is not moving
     * 
     * @return True if stalled
     */
    public boolean isStalled() {
        for (NXTRegulatedMotor m : motors) {
            if(m.isStalled()) {
                return true;
            }
        }
        return false;
    }
    
    public void waitComplete() {
    	motors[0].waitComplete();
    }
    
    /**
     * Moves the motor as much as possible to closed. This is the new "closed position"
     * @param extremum_to_closed: degrees
     */
	public void calibrate(int extremum_to_closed) {
		setSpeed(400);
		forward();
		waitComplete();

		resetTachoCount();

		setSpeed(900);
		rotateTo(extremum_to_closed, false);
		resetTachoCount();
		flt();
	}
	
	/**
	 * Returns Position.open if tachoCount < openLimit, 
	 * Position.closed if tachoCount > closedLimit
	 * Throw "illegalStateException" if in the deadzone
	 * @param openLimit
	 * @param closedLimit
	 * @return
	 */
	public Position getPosition(int openLimit, int closedLimit){
		if (getTachoCount() < openLimit) {
			return Position.OPEN;
		}
		if (getTachoCount() > closedLimit) {
			return Position.CLOSED;
		}
		throw new IllegalStateException("In the deadzone");
	}
	

}
