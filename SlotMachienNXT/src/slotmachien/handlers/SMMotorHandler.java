package slotmachien.handlers;

import java.util.EmptyQueueException;
import java.util.Queue;

import lejos.nxt.NXTRegulatedMotor;
import observable.Observable;
import observable.Observer;
import slotmachien.internal.MotorBlock;
import slotmachien.internal.Position;
import slotmachien.signals.Command;
import slotmachien.signals.MovedToSignal;
import slotmachien.signals.Signal;

public class SMMotorHandler extends Observable<MovedToSignal> implements
        Observer<Command> {

    private static final int OPEN_LIMIT_BEFORE_DEADZONE = -60;
    private static final int CLOSED_LIMIT_BEFORE_DEADZONE = -100;

    private static final int ANGLE_STALL_THRESHOLD = 5;
    private static final int TIME_STALL_THRESHOLD = 200;

    private static final int EXTREMUM_TO_OPEN = -440;
    private static final int EXTREMUM_TO_CLOSED = EXTREMUM_TO_OPEN + 180;

    private static final int ALLOWED_TACHO_MARGIN = 5;

    private MotorBlock motorblock;
    private final Queue<Command> commandQueue = new Queue<>();

    private volatile boolean turning = false;

    private Command currentState = new Command(Position.CLOSED, "calibrated");

    private final Thread thread;

    /**
     * Builds a controller for the given motors.
     * 
     * @param statusPing
     *            : a observable. On each signal of this observable, the status
     *            (deadzone) will be checked
     * @param motors
     */
    public SMMotorHandler(Observable<Signal> statusPing,
            NXTRegulatedMotor... motors) {
        this.motorblock = new MotorBlock(motors);

        // a motor is considered 'stalled' if the current angle is off by 5
        // degrees for over 200ms
        motorblock.setStallThreshold(ANGLE_STALL_THRESHOLD,
                TIME_STALL_THRESHOLD);

        motorblock.calibrate(EXTREMUM_TO_CLOSED);
        notifyObservers(new MovedToSignal(currentState));

        // start queue thread
        thread = new Thread(new QueueHandler());
        thread.start();

        // bind "deadzone" detector to the clock
        statusPing.addObserver(new PositionChecker());
    }

    private void turnTo(Command command) {
        turnTo(command, true);
    }

    private void turnTo(Command command, boolean allowTurnBack) {
        turning = true;
        int turn = command.pos.getPos();
        motorblock.setSpeed(900);
        motorblock.rotateTo(turn, false);
        // if the motor is not in the projected area after turning, undo the
        // turn
        motorblock.flt();
        if ((allowTurnBack && (motorblock.getTachoCount() > turn
                + ALLOWED_TACHO_MARGIN || motorblock.getTachoCount() < turn
                - ALLOWED_TACHO_MARGIN))) {
            turnTo(currentState, false);
            throw new IllegalStateException(
                    "turnto failed, motorblock turned back");
        }
        turning = false;

    }

    /**
     * Checks the motos position. When it is in the deadzone, a 'Door open' is
     * added to the queue
     */
    public void checkStatus() {
        if(turning){
            return;
        }

        try {
            Position p = motorblock.getPosition(OPEN_LIMIT_BEFORE_DEADZONE,
                    CLOSED_LIMIT_BEFORE_DEADZONE);
            if (p != currentState.pos) {
                setState(new Command(p, "manual"));
            }
        } catch (IllegalStateException e) {
            addCommand(new Command(Position.OPEN, "deadzone"));
        }
    }

    private void setState(Command cmd) {
        currentState = cmd;
        notifyObservers(new MovedToSignal(cmd));
    }
    
    /*
     * END OF UTILITY
     */

    public void addCommand(Command command) {
        synchronized (commandQueue) {
            commandQueue.push(command);
            commandQueue.notifyAll();
        }
    }

    @Override
    public void notified(Command signal) {
        addCommand(signal);
    }

    /**
     * Thread checking the queue, updating actions
     * 
     * @author pietervdvn
     */
    private class QueueHandler implements Runnable {

        @Override
        public void run() {
            while (true) {
                try {
                    Command cmd = null;
                    synchronized (commandQueue) {
                        if (!commandQueue.empty()) {
                            cmd = (Command) commandQueue.pop();
                        }
                    }
                    if (cmd != null && currentState.pos != cmd.pos) {
                        turnTo(cmd);
                        currentState = cmd;
                        notifyObservers(new MovedToSignal(cmd));
                    }
                } catch (IllegalStateException e) {
                } catch (EmptyQueueException e) {
                }

                synchronized (commandQueue) {
                    try {
                        if (commandQueue.isEmpty()) {
                            commandQueue.wait();
                        }
                    } catch (InterruptedException e) {
                    }
                }
            }
        }
    }

    /**
     * Observer which, on a received signal, checks the position. If deadzoned
     * -> open the door with a event
     */
    private class PositionChecker implements Observer<Signal> {
        @Override
        public void notified(Signal signal) {
            checkStatus();
        }
    }

    public Command getState() {
        return currentState;
    }

}
