package slotmachien;

import lejos.nxt.Button;
import lejos.nxt.Motor;
import observable.Mapper;
import observable.ObservableButton;
import observable.PeriodicSignal;
import slotmachien.handlers.ButtonHandlers;
import slotmachien.handlers.MovedToMessage;
import slotmachien.handlers.SMMotorHandler;
import slotmachien.handlers.ScreenHandler;
import slotmachien.handlers.USBHandler;

public class NXTMain {

    public static void main(String[] args) {

    	
    	PeriodicSignal clock	= new PeriodicSignal(500);
    	
        SMMotorHandler motors = new SMMotorHandler(clock, Motor.B, Motor.C);
        USBHandler usb	= new USBHandler();
        ScreenHandler screen	= new ScreenHandler();

        // handle buttons
        ButtonHandlers buttonHandler = new ButtonHandlers();
        buttonHandler.addObserver(motors);
        new ObservableButton(Button.LEFT).addObserver(buttonHandler);
        new ObservableButton(Button.RIGHT).addObserver(buttonHandler);
        new ObservableButton(Button.ENTER).addObserver(buttonHandler);

        
        // write status updates to USB
        Mapper.pipe(motors, new MovedToMessage(), usb);
        // and to screen
        Mapper.pipe(motors, new MovedToMessage(), screen);
       
        
        
    }
}
