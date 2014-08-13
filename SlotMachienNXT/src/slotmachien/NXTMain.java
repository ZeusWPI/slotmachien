package slotmachien;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import lejos.nxt.Button;
import lejos.nxt.LCD;
import lejos.nxt.Motor;
import lejos.nxt.comm.NXTConnection;
import lejos.nxt.comm.USB;

public class NXTMain {

    private static final int POSITION_OPEN = 0;
    private static final int POSITION_CLOSED = 180;

    static final SMMotorHandler MOTORS = new SMMotorHandler(Motor.B, Motor.C);
    private static final Map<Byte, Action> ACTIONS = new HashMap<Byte, Action>();

    public static void main(String[] args) throws Exception {

        // Define actions.
        ACTIONS.put((byte) 1, new TurnToAction(MOTORS, POSITION_OPEN));
        ACTIONS.put((byte) 2, new TurnToAction(MOTORS, POSITION_CLOSED));
        ACTIONS.put((byte) 3, new IDAction());

        Button.LEFT.addButtonListener(new ActionButtonListener(
                new TurnToAction(MOTORS, POSITION_OPEN)));
        Button.RIGHT.addButtonListener(new ActionButtonListener(
                new TurnToAction(MOTORS, POSITION_CLOSED)));
        Button.ENTER.addButtonListener(new ActionButtonListener(
                new CenterAction()));
        Button.ESCAPE.addButtonListener(new ActionButtonListener(
                new EscapeAction()));

        // initial calibration
        drawString("calibrating");
        MOTORS.calibrate();
        drawString(MOTORS.getStatus().toString());

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
        drawString(status.toString());
    }

    public static void sendStatus(NXTConnection conn, Status status) {
        drawString("waiting for stream");
        try (DataOutputStream dos = conn.openDataOutputStream()) {
            drawString("outputstream opened");
            dos.write(status.toByte());
            dos.flush();
            drawString("byte sent");
        } catch (IOException | NullPointerException e) {
            drawString("could not send status");
        }
    }

    public static void drawString(String s) {
        LCD.clear();
        LCD.drawString(s, 0, 0);
    }
}
