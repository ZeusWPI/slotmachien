package slotmachien;
import java.util.ArrayList;

import observable.AbstractObservable;
import observable.Observable;
import observable.Observer;
import lejos.nxt.NXTRegulatedMotor;
import lejos.util.Delay;


public class SMMotorHandler extends AbstractObservable {
    
    private static final int OPEN_LIMIT_BEFORE_DEADZONE = -40;
    private static final int CLOSED_LIMIT_BEFORE_DEADZONE = -100;
    
    private static final int ANGLE_STALL_THRESHOLD = 5;
    private static final int TIME_STALL_THRESHOLD = 200;
    
    private static final int EXTREMUM_TO_OPEN = -440;
    private static final int EXTREMUM_TO_CLOSED = EXTREMUM_TO_OPEN + 180;
    
    private static final int ALLOWED_TACHO_MARGIN = 5;
    
    
    private MotorBlock motorblock;
        
    public SMMotorHandler(NXTRegulatedMotor... motors) {
        this.motorblock = new MotorBlock(motors); 
        
        // a motor is considered 'stalled' if the current angle is off by 5 degrees for over 200ms
        motorblock.setStallThreshold(ANGLE_STALL_THRESHOLD, TIME_STALL_THRESHOLD);
        
        calibrate();
        
        // observe motor status
        new Thread(new StatusListener()).start();
    }

    public void turnTo(int turn) {
        turnTo(turn, true);
    }

    public void turnTo(int turn, boolean allowTurnBack) {
        int prevPos = motorblock.getTachoCount();
        motorblock.setSpeed(900);
        motorblock.rotateTo(turn, false);
        //if the motor is not in the projected area after turning, undo the turn
        if (( allowTurnBack && 
                (motorblock.getTachoCount() > turn + ALLOWED_TACHO_MARGIN ||
                motorblock.getTachoCount() < turn - ALLOWED_TACHO_MARGIN) )) {
            turnTo(prevPos,false);
        }
        motorblock.flt();
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
    
    private class StatusListener implements Runnable {
        private Status previousStatus = getStatus();
        
        @Override
        public void run() {
            while(true) {
                Status status = getStatus();
                if (status != previousStatus && status != Status.DEADZONED) {
                    previousStatus = status;
                    notifyObservers();
                }
                Delay.msDelay(500);
            }
            
        }
        
    }
    
}
