import java.io.*;
import lejos.nxt.*;
import lejos.nxt.comm.*;

public class NXTMain {

    public static void main(String[] args) throws Exception {

        boolean quit = false;
        while (!quit) {

            drawString("Waiting");
            NXTConnection connection = USB.waitForConnection();
            DataInputStream dis = connection.openDataInputStream();

            drawString("Connected");

            boolean restart = false;

            //

            while (!restart) {

                byte b = dis.readByte();
                switch (b) {
                case 1:     //open the door
                    open();
                case 2:     //close the door
                    close();
                case 3:     //restart the program
                    restart = true;
                    drawString("Restarting");                    
                case 4:     //quit the program
                    quit = true;
                    drawString("Quitting");
                }
            }

            dis.close();
            connection.close();
            LCD.clear();
        }
    }

    public static void open() {
        if (getStatus() == 0) {
            turn(180);
        }
    }

    public static void close() {
        if (getStatus() == 1) {
            turn(-180);
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

    public static int getStatus() { //check if the door is open or closed
        if (Motor.B.getTachoCount() > 350 && Motor.B.getTachoCount() < 10) {
            return 0;
        } else if (Motor.B.getTachoCount() > 170
                && Motor.B.getTachoCount() < 190) {
            return 1;
        } else {
            setToKnownStatus();
            return 2;
        }
    }
    
    public static void setToKnownStatus() {
        // check the tachocount turn the lock to the closest fixed position
    }
    
    public static void drawString(String s) {
        LCD.clear();
        LCD.drawString(s, 0, 0);
    }

}