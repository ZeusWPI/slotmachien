import java.io.*;

import lejos.nxt.*;
import lejos.nxt.comm.*;
import lejos.util.Delay;

public class NXTMain {
    private static final int POSITION_OPEN = 20;
    private static final int POSITION_CLOSED = 200;

    public static void main(String[] args) throws Exception {
        
        Motor.B.setStallThreshold(5, 500);
        calibrate();
  
        boolean quit = false;
        
        while (!quit) {
            
            drawString("Waiting");
            NXTConnection connection = USB.waitForConnection();
            DataInputStream dis = connection.openDataInputStream();

            drawString("Connected");

            byte b = dis.readByte();
            drawString(Byte.toString(b));
            switch (b) {
            case 1: // open de door
                turnTo(POSITION_OPEN);
                break;
            case 2: // close de door
                turnTo(POSITION_CLOSED);
                break;
            case 3: // quit de program
                quit = true;
                drawString("Quitting");
                break;
            }

            dis.close();
            connection.close();
        }

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
        // IK GA HARD MAN
        Motor.B.setSpeed(900);
        Motor.C.setSpeed(900);
        Motor.B.rotateTo(turn, true);
        Motor.C.rotateTo(turn);
        Motor.B.flt();
        Motor.C.flt();
    }

    //DEPRECATED
    public static int getStatus() { // check if the door is open or closed
        if (Motor.B.getTachoCount() > 10 && Motor.B.getTachoCount() < 30) {
            return 0;
        } else if (Motor.B.getTachoCount() > 190
                && Motor.B.getTachoCount() < 210) {
            return 1;
        } else {
            calibrate();
            return 2;
        }
    }

    public static void drawString(String s) {
        LCD.clear();
        LCD.drawString(s, 0, 0);
    }
    
    public static void calibrate() {
        while(!Motor.B.isStalled()) {
            turn(10);
        }
        turn(-440);
        Motor.B.resetTachoCount();
        Motor.C.resetTachoCount();
        turnTo(POSITION_OPEN);
        
        // De bug
        drawString(Integer.toString(Motor.B.getTachoCount()));
        Delay.msDelay(2000);
    }
}