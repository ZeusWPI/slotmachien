import java.io.*;

import lejos.nxt.*;
import lejos.nxt.comm.*;
import lejos.util.Delay;

public class NXTMain {

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
            switch (b) {
            case 1: // open the door
                open();
                break;
            case 2: // close the door
                close();
                break;
            case 3: // quit the program
                quit = true;
                drawString("Quitting");
                break;
            }

            dis.close();
            connection.close();
            LCD.clear();
        }
    }

    public static void open() {
        if (getStatus() == 1) {
            turn(-180);
        }
    }

    public static void close() {
        if (getStatus() == 0) {
            turn(180);
        }
    }

    public static void turn(int turn) {
        Motor.B.setSpeed(900); // 2.5 rotations per second
        Motor.C.setSpeed(900);
        Motor.B.rotate(turn, true);
        Motor.C.rotate(turn);
        Motor.B.flt();
        Motor.C.flt();
    }

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
        turn(20);
        drawString(Integer.toString(Motor.B.getTachoCount()));
        Delay.msDelay(2000);
    }
}