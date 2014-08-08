
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

public class NXTMain {

	private static final int POSITION_OPEN = 0;
	private static final int POSITION_CLOSED = 180;

	private static final int OPEN_LIMIT_BEFORE_DEADZONE = 40;
	private static final int CLOSED_LIMIT_BEFORE_DEADZONE = 100;

	private static final int STEPPER_ANGLE = 20;
	private static final int EXTREMUM_TO_OPEN = -440;

	private static boolean status; // true: door unlocked; false: door locked.

	public static void main(String[] args) throws Exception {

		addButtonListeners();

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
			status = true;
			break;
		case 2: // close de door
			turnTo(POSITION_CLOSED);
			status = false;
			break;
		case 3: // send status to slotmachienPC
			sendStatus(conn);
			break;
		}
		drawString(checkStatus() ? "open" : "closed");

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

	// CODE DUPLICATION !!!1!!
	public static void turnTo(int turn) {
		// IK GA HARD MAN
		Motor.B.setSpeed(900);
		Motor.C.setSpeed(900);
		Motor.B.rotateTo(turn, true);
		Motor.C.rotateTo(turn);
		Motor.B.flt();
		Motor.C.flt();
	}

	// check door status
	public static boolean checkStatus() {
		if (Motor.B.getTachoCount() < OPEN_LIMIT_BEFORE_DEADZONE
				&& status) {
			return true;
		}
		if (Motor.B.getTachoCount() > CLOSED_LIMIT_BEFORE_DEADZONE
				&& !status) {
			return false;
		}

		// If no status could be determined, recalibrate because
		// the physical lock angle is located in the deadzone, or
		// the current STATUS does not match the physical lock status
		calibrate();
		return true;
	}

	public static void drawString(String s) {
		LCD.clear();
		LCD.drawString(s, 0, 0);
	}

	public static void calibrate() {
		drawString("calibrating");
		// Turn to extremum as calibration point (in steps of 20)
		Motor.B.setStallThreshold(5, 500);
		while (!Motor.B.isStalled()) {
			turn(STEPPER_ANGLE);
		}
		// Turn to open position
		turn(EXTREMUM_TO_OPEN);

		// Calibrate open position to 0
		Delay.msDelay(200);
		Motor.B.resetTachoCount();
		Motor.C.resetTachoCount();

		// shit's fucked, yo
		Motor.B.flt();
		Motor.C.flt();

		status = true;

		drawString("calibrated");
	}

	public static void sendStatus(NXTConnection conn) {
		DataOutputStream dos = conn.openDataOutputStream();
		byte b = 0;
		if (!status) {
			b = 1;
		}
		try {
			dos.write(b);
			dos.flush();
			dos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void addButtonListeners() {
		Button.LEFT.addButtonListener(new ButtonListener() {
			public void buttonPressed(Button b) {
				runCode((byte) 1, null); //I highly doubt this is good practice
			}

			public void buttonReleased(Button b) {
				drawString(checkStatus() ? "open" : "closed");
			}
		});
		Button.RIGHT.addButtonListener(new ButtonListener() {
			public void buttonPressed(Button b) {
				runCode((byte) 2, null); //I highly doubt this is good practice
			}

			public void buttonReleased(Button b) {
				drawString(checkStatus() ? "open" : "closed");
			}
		});
		Button.ENTER.addButtonListener(new ButtonListener() {
			public void buttonPressed(Button b) {
				// MAAK KABAAL LOL
				drawString("LOLOLOLOLOLOLOL");
				File muziekje = new File("muziekje.wav");
				Sound.playSample(muziekje, 100);
				Delay.msDelay(12000);
				drawString(checkStatus() ? "open" : "closed");
			}

			public void buttonReleased(Button b) {
				// nothing lol
			}
		});
		Button.ESCAPE.addButtonListener(new ButtonListener() {
			public void buttonPressed(Button b) {
				// TO BE REPLACED WITH "calibrate()" BEFORE DEPLOYMENT
				// CURRENTLY FOR DEBUGGING ONLY

				System.exit(0);
			}

			public void buttonReleased(Button b) {
				// nothing lol
			}
		});

	}
}