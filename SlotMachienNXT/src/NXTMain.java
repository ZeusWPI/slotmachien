import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;

import lejos.nxt.Button;
import lejos.nxt.ButtonListener;
import lejos.nxt.LCD;
import lejos.nxt.Motor;
import lejos.nxt.Sound;
import lejos.nxt.comm.NXTConnection;
import lejos.nxt.comm.USB;
import lejos.util.Delay;

enum Status {
	OPEN(0), CLOSED(1), DEADZONED(2);

	private byte b;

	private Status(int b) {
		this.b = (byte) b;
	}

	public byte toByte() {
		return b;
	}
}

public class NXTMain {

	private static final int POSITION_OPEN = 0;
	private static final int POSITION_CLOSED = 180;

	private static final int OPEN_LIMIT_BEFORE_DEADZONE = 40;
	private static final int CLOSED_LIMIT_BEFORE_DEADZONE = 100;

	private static final int EXTREMUM_TO_OPEN = -440;
	
	private static final int ALLOWED_TACHO_MARGIN = 3;

	public static void main(String[] args) throws Exception {

		addButtonListeners();
		
		Motor.B.setStallThreshold(5, 200);
		Motor.C.setStallThreshold(5, 200);

		// initial calibration
		calibrate();

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
		switch (b) {
		case 1: // open de door
			turnTo(POSITION_OPEN);
			break;
		case 2: // close de door
			turnTo(POSITION_CLOSED);
			break;
		case 3: // send status to slotmachienPC;
			break;
		}
		Status status = getStatus();
		sendStatus(conn, status);
		drawString(status.toString());
	}

	public static void turn(int turn) {
		// IK GA HARD MAN
		Motor.B.setSpeed(900);
		Motor.C.setSpeed(900);
		Motor.B.rotate(turn, true);
		Motor.C.rotate(turn);
		Motor.B.flt();
		Motor.C.flt();
	}
	
	public static void turnTo(int turn) {
		turnTo(turn, true);
	}

	public static void turnTo(int turn, boolean pingpong) {
		// IK GA HARD MAN
		int prevPos = Motor.B.getTachoCount();
		Motor.B.setSpeed(900);
		Motor.C.setSpeed(900);
		Motor.B.rotateTo(turn, true);
		Motor.C.rotateTo(turn, true);
		while ((Motor.B.isMoving() && Motor.C.isMoving())) {
			//wait for the motors to stop moving
		}
		if ( ( Motor.B.getTachoCount() > turn + ALLOWED_TACHO_MARGIN ||
				Motor.B.getTachoCount() < turn - ALLOWED_TACHO_MARGIN ) &&
				pingpong) {
			turnTo(prevPos,false);
		}
		Motor.B.flt();
		Motor.C.flt();
	}

	// check door status
	public static Status getStatus() {
		if (Motor.B.getTachoCount() < OPEN_LIMIT_BEFORE_DEADZONE) {
			return Status.OPEN;
		}
		if (Motor.B.getTachoCount() > CLOSED_LIMIT_BEFORE_DEADZONE) {
			return Status.CLOSED;
		}
		return Status.DEADZONED;
	}

	public static void drawString(String s) {
		LCD.clear();
		LCD.drawString(s, 0, 0);
	}

	public static void calibrate() {
		drawString("calibrating");
		Motor.B.setSpeed(400);
		Motor.C.setSpeed(400);
		Motor.B.forward();
		Motor.C.forward();
		while (!(Motor.B.isStalled() || Motor.C.isStalled())) {
			//wait for the motors to stall
		}

		Motor.B.resetTachoCount();
		Motor.C.resetTachoCount();
		
		turnTo(EXTREMUM_TO_OPEN,false);
		
		Motor.B.resetTachoCount();
		Motor.C.resetTachoCount();
		
		Motor.B.flt();
		Motor.C.flt();
		
		drawString(getStatus().toString());
	}

	public static void sendStatus(NXTConnection conn, Status status) {
		drawString("waiting for stream");
		try (DataOutputStream dos = conn.openDataOutputStream()) {
			drawString("outputstream opened");
			dos.write(status.toByte());
			dos.flush();
			drawString("byte sent");
		} catch (IOException|NullPointerException e) {
			drawString("could not send status");
		}
	}

	public static void addButtonListeners() {
		Button.LEFT.addButtonListener(new ButtonListener() {
			public void buttonPressed(Button b) {
				runCode((byte) 1, null); // I highly doubt this is good practice
			}

			public void buttonReleased(Button b) {
			}
		});
		Button.RIGHT.addButtonListener(new ButtonListener() {
			public void buttonPressed(Button b) {
				runCode((byte) 2, null); // I highly doubt this is good practice
			}

			public void buttonReleased(Button b) {
			}
		});
		Button.ENTER.addButtonListener(new ButtonListener() {
			public void buttonPressed(Button b) {
				
				// MAAK KABAAL LOL
				drawString("LOLOLOLOLOLOLOL");
				File muziekje = new File("muziekje.wav");
				Sound.playSample(muziekje, 100);
				Delay.msDelay(11000);
				drawString(getStatus().toString());
			}

			public void buttonReleased(Button b) {
				// nothing lol
			}
		});
		Button.ESCAPE.addButtonListener(new ButtonListener() {
			public void buttonPressed(Button b) {
				System.exit(0);
			}

			public void buttonReleased(Button b) {
				// nothing lol
			}
		});

	}
}