package slotmachien;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import lejos.nxt.Button;
import lejos.nxt.Motor;
import lejos.nxt.comm.NXTConnection;
import lejos.nxt.comm.USB;

public class NXTMain {

    public static final int POSITION_OPEN = -180;
    public static final int POSITION_CLOSED = 0;

    public static final SMMotorHandler MOTORS = new SMMotorHandler(Motor.B, Motor.C);
    private static final Map<Byte, Action> ACTIONS = new HashMap<>();

    public static void main(String[] args) throws Exception {
    	try {
			run();
		} catch (Exception e) {
			System.exit(1);
		}
    }
    
    private static void run() throws IOException {
    	Thread manual = new Thread(new ManualChecker());
    	manual.start();
    	MOTORS.addObserver(new LCDObserver());

        // Define actions.
        ACTIONS.put((byte) 1, new TurnToAction(MOTORS, Status.OPEN));
        ACTIONS.put((byte) 2, new TurnToAction(MOTORS, Status.CLOSED));
        ACTIONS.put((byte) 3, new IDAction());

        Button.LEFT.addButtonListener(new ActionButtonListener(
                new TurnToAction(MOTORS, Status.OPEN)));
        Button.RIGHT.addButtonListener(new ActionButtonListener(
                new TurnToAction(MOTORS, Status.CLOSED)));
        Button.ENTER.addButtonListener(new ActionButtonListener(
                new CenterAction()));

        MOTORS.calibrate();

        while (true) {
            // Wait for incoming command
            NXTConnection conn = USB.waitForConnection();
            DataInputStream dis = conn.openDataInputStream();

            // read and run instruction code
            byte b = dis.readByte();
            runCode(b, conn);

            // Terminate incoming connection
            dis.close();
            conn.close();
        }
    }

    public static void runCode(byte b, NXTConnection conn) {
        ACTIONS.get(b).performAction();
        Status status = MOTORS.getStatus();
        sendStatus(conn, status);
    }

    public static void sendStatus(NXTConnection conn, Status status) {
        try (DataOutputStream dos = conn.openDataOutputStream()) {
            dos.write(status.toByte());
            dos.flush();
        } catch (IOException | NullPointerException e) {
        }
    }
    
}
