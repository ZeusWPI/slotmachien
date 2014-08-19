package slotmachien;
import java.util.ArrayList;

import lejos.nxt.NXTRegulatedMotor;


public class SMMotorHandler implements Observable {
    
    private static final int OPEN_LIMIT_BEFORE_DEADZONE = -40;
    private static final int CLOSED_LIMIT_BEFORE_DEADZONE = -100;
    
    private static final int ANGLE_STALL_THRESHOLD = 5;
    private static final int TIME_STALL_THRESHOLD = 200;
    
    private static final int EXTREMUM_TO_OPEN = -440;
    private static final int EXTREMUM_TO_CLOSED = EXTREMUM_TO_OPEN + 180;
    
    private static final int ALLOWED_TACHO_MARGIN = 5;
    
    private MotorBlock motorblock;
	private ArrayList<Observer> observerList = new ArrayList<>();
        
    public SMMotorHandler(NXTRegulatedMotor... motors) {
        this.motorblock = new MotorBlock(motors); 
        
        //a motor is considered 'stalled' if the current angle is off by 5 degrees for over 200ms
        motorblock.setStallThreshold(ANGLE_STALL_THRESHOLD, TIME_STALL_THRESHOLD);
    }

    public void turnTo(int turn) {
        turnTo(turn, true);
    }

    public void turnTo(int turn, boolean allowUndoTurn) {
        // IK GA HARD MAN
        int prevPos = motorblock.getTachoCount();
        motorblock.setSpeed(900);
        motorblock.rotateTo(turn, false);
        //if the motor is not in the projected area after turning, undo the turn
        if (( allowUndoTurn && 
                (motorblock.getTachoCount() > turn + ALLOWED_TACHO_MARGIN ||
                motorblock.getTachoCount() < turn - ALLOWED_TACHO_MARGIN) )) {
            turnTo(prevPos,false);
        }
        motorblock.flt();
        notifyObservers();
    }
    
    public void calibrate() {
        motorblock.setSpeed(400);
        motorblock.forward();
        motorblock.waitComplete();

        motorblock.resetTachoCount();
        
        turnTo(EXTREMUM_TO_CLOSED, false);

        motorblock.resetTachoCount();
        
        motorblock.flt();
    }
    
    // check door status
    public Status getStatus() {
        if (motorblock.getTachoCount() < OPEN_LIMIT_BEFORE_DEADZONE) {
            return Status.OPEN;
        }
        if (motorblock.getTachoCount() > CLOSED_LIMIT_BEFORE_DEADZONE) {
            return Status.CLOSED;
        }
        return Status.DEADZONED;
    }

	@Override
	public void notifyObservers() {
		for (Observer o : observerList) {
			o.invalidated();
		}
	}

	@Override
	public void addObserver(Observer o) {
		observerList.add(o);
		
	}

	@Override
	public void removeObserver(Observer o) {
		observerList.remove(o);
	}
    
}
