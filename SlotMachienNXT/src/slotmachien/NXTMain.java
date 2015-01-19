package slotmachien;

import observable.ObservableButton;
import observable.PeriodicSignal;
import slotmachien.handlers.ButtonHandlers;
import slotmachien.handlers.SMMotorHandler;
import slotmachien.handlers.ScreenPrinter;
import lejos.nxt.Button;
import lejos.nxt.Motor;

public class NXTMain {

    public static void main(String[] args) {

    	
    	PeriodicSignal clock	= new PeriodicSignal(500);
    	
        SMMotorHandler motors = new SMMotorHandler(clock, Motor.B, Motor.C);

        ButtonHandlers buttonHandler = new ButtonHandlers(motors);
        new ObservableButton(Button.LEFT).addObserver(buttonHandler);
        new ObservableButton(Button.RIGHT).addObserver(buttonHandler);
        new ObservableButton(Button.ENTER).addObserver(buttonHandler);

        motors.addObserver(new ScreenPrinter());
    }
}
