package slotmachien;

import lejos.nxt.Button;
import lejos.nxt.Motor;
import lejos.nxt.Sound;
import observable.Mapper;
import observable.ObservableButton;
import observable.Observer;
import observable.PeriodicSignal;
import slotmachien.handlers.ButtonHandlers;
import slotmachien.handlers.DelayedClose;
import slotmachien.handlers.MovedToMessage;
import slotmachien.handlers.SMMotorHandler;
import slotmachien.handlers.ScreenHandler;
import slotmachien.handlers.USBHandler;
import slotmachien.handlers.UsbParser;
import slotmachien.signals.ButtonSignal;
import slotmachien.signals.UnsubscribeMeException;

public class NXTMain {

	public static void main(String[] args) {

		PeriodicSignal clock = new PeriodicSignal(1000);

		SMMotorHandler motors = new SMMotorHandler(clock, Motor.B, Motor.C);
		USBHandler usb = new USBHandler();
		ScreenHandler screen = new ScreenHandler();

		// write status updates to USB
		Mapper.pipe(motors, new MovedToMessage(), usb);
		// and to screen
		Mapper.pipe(motors, new MovedToMessage(), screen);

		// delayed closer
		DelayedClose delayedC = new DelayedClose(motors, clock, 10);
		// abort delayed close when something happens:
		motors.addObserverDangerous(delayedC.getCanceller());

		
		
		// handle buttons
		ButtonHandlers buttonHandler = new ButtonHandlers();
		buttonHandler.addObserver(motors);
		
		
		new ObservableButton(Button.LEFT, Button.RIGHT)
				.addObserver(buttonHandler);
		new ObservableButton(Button.ENTER).addObserverDangerous(delayedC);
		new ObservableButton(Button.ESCAPE)
				.addObserver(new Observer<ButtonSignal>() {
					@Override
					public void notified(ButtonSignal signal)
							throws UnsubscribeMeException {
						Sound.buzz();
					}
				});

		// handle usb-input
		UsbParser parser = new UsbParser(motors, usb);
		usb.addObserver(parser);

	}
}
