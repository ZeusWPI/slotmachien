package slotmachien.handlers;

import java.util.EmptyQueueException;
import java.util.Queue;

import lejos.nxt.NXTRegulatedMotor;
import observable.AbstractObservable;
import slotmachien.Command;
import slotmachien.MotorBlock;
import slotmachien.Position;
import slotmachien.signals.MovedToSignal;

public class SMMotorHandler extends AbstractObservable<MovedToSignal> {

    private static final int OPEN_LIMIT_BEFORE_DEADZONE = -40;
    private static final int CLOSED_LIMIT_BEFORE_DEADZONE = -100;

    private static final int ANGLE_STALL_THRESHOLD = 5;
    private static final int TIME_STALL_THRESHOLD = 200;

    private static final int EXTREMUM_TO_OPEN = -440;
    private static final int EXTREMUM_TO_CLOSED = EXTREMUM_TO_OPEN + 180;

    private static final int ALLOWED_TACHO_MARGIN = 5;

    private MotorBlock motorblock;
    private final Queue<Command> commandQueue = new Queue<>();
    private Command currentState = new Command(Position.CLOSED, "calibrated");

    private final Thread thread;

    public SMMotorHandler(NXTRegulatedMotor... motors) {
        this.motorblock = new MotorBlock(motors);

        // a motor is considered 'stalled' if the current angle is off by 5
        // degrees for over 200ms
        motorblock.setStallThreshold(ANGLE_STALL_THRESHOLD,
                TIME_STALL_THRESHOLD);

        calibrate();
        notifyObservers(new MovedToSignal(currentState));

        // observe motor status
        thread = new Thread(new StatusListener());
        thread.start();
    }

    private void turnTo(Command command) {
        turnTo(command, true);
    }

    private void turnTo(Command command, boolean allowTurnBack) {
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
    }

    private void calibrate() {
        motorblock.setSpeed(400);
        motorblock.forward();
        motorblock.waitComplete();

        motorblock.resetTachoCount();

        motorblock.setSpeed(900);
        motorblock.rotateTo(EXTREMUM_TO_CLOSED, false);

        motorblock.resetTachoCount();

        motorblock.flt();
    }

    // check door status
    public void checkStatus() {
        if (motorblock.getTachoCount() < OPEN_LIMIT_BEFORE_DEADZONE) {
            if (currentState.pos != Position.OPEN) {
                setState(new Command(Position.OPEN, "manual open"));
            }
            return;
        }
        if (motorblock.getTachoCount() > CLOSED_LIMIT_BEFORE_DEADZONE) {
            if (currentState.pos != Position.CLOSED) {
                setState(new Command(Position.CLOSED, "manual close"));
            }
            return;
        }
        addCommand(new Command(Position.OPEN, "in deadzone"));
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
            commandQueue.notify();
        }
    }

    private class StatusListener implements Runnable {

        @Override
        public void run() {
            while (true) {
                try {
                    Command cmd;
                    synchronized (commandQueue) {
                        cmd = (Command) commandQueue.pop();
                    }
                    turnTo(cmd);
                    currentState = cmd;
                    notifyObservers(new MovedToSignal(cmd));
                } catch (IllegalStateException e) {
                } catch (EmptyQueueException e) {
                }

                // TODO check status each 500 ms
                checkStatus();

                synchronized (commandQueue) {
                    try {
                        commandQueue.wait();
                    } catch (InterruptedException e) {
                    }
                }

            }

        }

    }

}
