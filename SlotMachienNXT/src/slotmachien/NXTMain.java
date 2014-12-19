package slotmachien;

import io.UsbIO;
import lejos.nxt.Button;
import lejos.nxt.Motor;
import lejos.nxt.Sound;
import observable.ObservableButton;
import slotmachien.actions.Action;
import slotmachien.actions.DelayedAction;
import slotmachien.actions.DrawLcdAction;
import slotmachien.actions.TurnToAction;
import slotmachien.actions.WriteStatusAction;
import time.Countdown;

public class NXTMain {

	private static final int POSITION_OPEN = -180;
	private static final int POSITION_CLOSED = 0;

	private Action openAction;
	private Action closeAction;
	private Action drawLcdAction;
	private Action delayedCloseAction;
	private SMMotorHandler motors;

	public static void main(String[] args) {
		new NXTMain();
	}

	// DIT MOEST IK DOEN VAN T21
	private NXTMain() {
		motors = new SMMotorHandler(Motor.B, Motor.C);

		openAction = new TurnToAction(motors, POSITION_OPEN);
		closeAction = new TurnToAction(motors, POSITION_CLOSED);
		drawLcdAction = new DrawLcdAction(motors);
		delayedCloseAction = new DelayedAction(10, new Countdown.Ticker() {
			@Override
			public void doTick(int tick) {
				Sound.beep();
			}
		}, closeAction);

		openAction.performOnNotification(new ObservableButton(Button.LEFT));
		closeAction.performOnNotification(new ObservableButton(Button.RIGHT));
		delayedCloseAction.performOnNotification(new ObservableButton(
				Button.ENTER));

		drawLcdAction.performOnNotification(motors);

		new ConnectWithPC().run();
	}

	class ConnectWithPC implements Runnable {

		@Override
		public void run() {
			UsbIO conn = UsbIO.waitForUsbIO();
			conn.setOnBreak(this);

			StreamObserver streamObserver = new StreamObserver(conn);
			Action writeStatusAction = new WriteStatusAction(motors, conn);

			openAction
					.performOnNotification(streamObserver.getObserver("OPEN"));
			closeAction.performOnNotification(streamObserver
					.getObserver("CLOSE"));

			writeStatusAction.performOnNotification(motors);

			// Write initial status
			writeStatusAction.perform();
		}

	}
}
