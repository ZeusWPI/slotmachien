import java.io.*;

import lejos.nxt.*;
import lejos.nxt.comm.*;
import lejos.nxt.remote.NXTCommand;
import lejos.nxt.remote.NXTProtocol;
import lejos.util.Delay;

public class NXTMain {
    private static final int POSITION_OPEN = 0;
    private static final int POSITION_CLOSED = 180;
    
    public static void main(String[] args) throws Exception {
        
        addButtonListeners();
        
        // initial calibration
        calibrate();
        
        while (true) {
            
            // Wait for incoming command
            NXTConnection connection = USB.waitForConnection();
            DataInputStream dis = connection.openDataInputStream();
            
            // read and run instruction code
            byte b = dis.readByte();
            runCode(b);
            
            // Terminate incoming connection
            dis.close();
            connection.close();
        }

    }
    
    public static void runCode(byte b) {
        switch (b) {
            case 1: // open de door
                turnTo(POSITION_OPEN);
                break;
            case 2: // close de door
                turnTo(POSITION_CLOSED);
                break;
        }
        drawString( getStatus()?"open":"closed" );
        
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

    // Get door status
    public static boolean getStatus() {
        if ( checkRotationProximity( Motor.B.getTachoCount(), POSITION_OPEN, 20) ) {
            return true;
        }
        if ( checkRotationProximity( Motor.B.getTachoCount(), POSITION_CLOSED, 20) ) {
            return false;
        }
        
        // If no status could be determined, try recalibrating.
        //calibrate();
        return true;
    }
    
    public static boolean checkRotationProximity(int rotation, int target, int toleration){
        int normalized = (rotation - target + 360)%360;
        return ( 360-toleration < normalized || normalized < toleration);
    }

    public static void drawString(String s) {
        LCD.clear();
        LCD.drawString(s, 0, 0);
    }
    
    public static void calibrate() {
        drawString("calibrating");
        // Turn to extremum as calibration point
        Motor.B.setStallThreshold(5, 500);
        while(!Motor.B.isStalled()) {
            turn(20);
        }
        // Turn to open position
        turn(-450);
        // Calibrate open position to 0
        Delay.msDelay(200);
        Motor.B.resetTachoCount();
        Motor.C.resetTachoCount();
        
        //shit's fucked, yo
        Motor.B.flt();
        Motor.C.flt();
        
        drawString("calibrated");
    }
    
    public static void addButtonListeners() {
        Button.LEFT.addButtonListener(new ButtonListener() {
            public void buttonPressed(Button b) {
                turnTo(POSITION_OPEN);
            }

            public void buttonReleased(Button b) {
                drawString( getStatus()?"open":"closed" );
            }
        });
        Button.RIGHT.addButtonListener(new ButtonListener() {
            public void buttonPressed(Button b) {
                turnTo(POSITION_CLOSED);
            }

            public void buttonReleased(Button b) {
                drawString( getStatus()?"open":"closed" );
            }
        });
        Button.ENTER.addButtonListener(new ButtonListener() {
            public void buttonPressed(Button b) {
                //MAAK KABAAL LOL
                drawString("LOLOLOLOLOLOLOL");
                File muziekje = new File("muziekje.wav");
                Sound.playSample(muziekje, 100);
                Delay.msDelay(12000);
                drawString( getStatus()?"open":"closed" );
            }

            public void buttonReleased(Button b) {
                //nothing lol
            }
        });
        Button.ESCAPE.addButtonListener(new ButtonListener() {
            public void buttonPressed(Button b) {
                //TO BE REPLACED WITH "calibrate()" BEFORE DEPLOYMENT
                //CURRENTLY FOR DEBUGGING ONLY
                
                System.exit(0);
            }

            public void buttonReleased(Button b) {
                //nothing lol
            }
        });
        
    }
}