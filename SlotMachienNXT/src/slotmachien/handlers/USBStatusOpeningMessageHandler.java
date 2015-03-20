package slotmachien.handlers;

import observable.Observable;
import observable.Observer;
import observable.UnsubscribeMeException;
import slotmachien.signals.MessageSignal;
import slotmachien.signals.UsbStatusSignal;
import slotmachien.signals.UsbStatusSignal.UsbStatus;

public class USBStatusOpeningMessageHandler extends Observable<MessageSignal>
        implements Observer<UsbStatusSignal> {

    SMMotorHandler smmh;

    public USBStatusOpeningMessageHandler(SMMotorHandler smmh) {
        this.smmh = smmh;
        
        try {
            // Write initial status in case USB already connected
            notified(null);
        } catch (UnsubscribeMeException e) {
        }
    }

    @Override
    public void notified(UsbStatusSignal signal) throws UnsubscribeMeException {
        if(signal != null && signal.status == UsbStatus.CONNECTED) {
            notifyObservers(new MessageSignal(smmh.getState().pos.name(), "init"));
        }
    }

}
