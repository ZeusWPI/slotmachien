package slotmachien.handlers;

import java.io.IOException;

import observable.Observable;
import observable.Observable;
import observable.Observer;
import slotmachien.internal.UsbIO;
import slotmachien.signals.MessageSignal;
import slotmachien.signals.UsbStatusSignal;
import slotmachien.signals.UsbStatusSignal.UsbStatus;

/**
 * Handles the USB-input, emits neat "usb-signals".
 * 
 * Can observe a other observable. These signals will be sent back over USB.
 * 
 * Automatically reconnects.
 * 
 * @author pietervdvn
 */
public class USBHandler extends Observable<MessageSignal> implements
        Observer<MessageSignal> {

    public static boolean debug = false;

    private UsbIO io = null;
    private final Observable<UsbStatusSignal> statusSignaller = new Observable<UsbStatusSignal>();

    /**
     * Does all the stuff! Connects, retries connecting if connection is lost,
     * sends signals to known parties and accepts events to send back over usb.
     * ...
     * 
     * @param clock
     */
    public USBHandler() {
        log("Made handler");
        statusSignaller.addObserver(new RetryConnection());
        statusSignaller.notifyObservers(new UsbStatusSignal(UsbStatus.DISCONNECTED));
        startReading();
    }

    private void startReading() {
        new Thread(new Runnable() {
            @Override
            public void run() {

                while (io != null) {
                    try {
                        String read = io.readLine();
                        if (read.equals("")) {
                            disconnect();
                        } else {
                            notifyObservers(new MessageSignal(read));
                        }
                    } catch (Exception e) {
                        disconnect();
                    }
                }
            }
        }).start();
    }

    /**
     * Attempts connecting
     */
    private void connect() {
        io = UsbIO.waitForUsbIO();
        statusSignaller.notifyObservers(new UsbStatusSignal(UsbStatus.CONNECTED));
        startReading();
    }

    private void disconnect() {
        try {
            io.close();
        } catch (IOException e) {
        }
        io = null;
        statusSignaller.notifyObservers(new UsbStatusSignal(UsbStatus.DISCONNECTED));
    }

    @Override
    public void notified(MessageSignal signal) {
        if (io == null) {
            // Return silently
            return;
        }
        try {
            io.writeLine(signal.content);
        } catch (Exception e) {
            // something failed -> send disconnect msg
            disconnect();
        }
    }

    public void notified(String msg) {
        notified(new MessageSignal(msg));
    }

    public Observable<UsbStatusSignal> getStatusObservable() {
        return statusSignaller;
    }

    /**
     * Spawns a new thread to connect when connection is down.
     * 
     * @author pietervdvn
     *
     */
    private class RetryConnection implements Observer<UsbStatusSignal> {

        @Override
        public void notified(UsbStatusSignal signal) {

            if (signal.status == UsbStatus.DISCONNECTED) {

                new Thread(new Runnable() {

                    @Override
                    public void run() {
                        log("Trying to connect");
                        connect();
                    }
                }).start();
            }
        }
    }

    private static void log(String msg) {
        if (debug) {
            System.out.println("UHL: " + msg);
        }
    }

}
