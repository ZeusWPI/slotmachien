import java.io.*;
import lejos.nxt.*;
import lejos.nxt.comm.*;

public class NXTMain {

    public static void main(String[] args) throws Exception {

        boolean quit = false;
        while (!quit) {

            LCD.drawString("Waiting", 0, 0);
            NXTConnection connection = USB.waitForConnection();
            DataInputStream dis = connection.openDataInputStream();

            LCD.clear();
            LCD.drawString("Connected", 0, 0);

            boolean restart = false;

            while (!restart) {
                /**
                 * als 1 gestuurd wordt dan draaien de motoren een halve slag
                 * vooruit als 2 gestuurd wordt dan draaien de motoren een halve
                 * slag achteruit als 3 gestuurd wordt dan herstart het
                 * programma als 4 (of groter) gestuurd wordt dan stopt het
                 * programma
                 */

                byte b = dis.readByte();
                if (b > 2) {
                    restart = true;
                    LCD.clear();
                    LCD.drawString("Restarting", 0, 0);

                    if (b > 3) {
                        quit = true;
                        LCD.clear();
                        LCD.drawString("Quiting", 0, 0);
                    }
                } else {
                    turn(b == 1);
                }
            }

            dis.close();

            connection.close();
            LCD.clear();
        }
    }

    public static void turn(boolean b) {
        int turn = 180;
        String turning = "Opening";
        if (!b) {
            turn = -turn;
            turning = "Closing";
        }
        LCD.clear();
        LCD.drawString(turning, 0, 0);
        Motor.B.setSpeed(900);
        Motor.C.setSpeed(900);
        Motor.B.rotate(turn, true);
        Motor.C.rotate(turn);
        Motor.B.flt();
        Motor.C.flt();
    }
}