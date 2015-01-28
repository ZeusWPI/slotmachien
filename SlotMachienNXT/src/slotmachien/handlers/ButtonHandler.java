package slotmachien.handlers;

import lejos.nxt.Button;
import observable.Mapper;
import slotmachien.internal.Position;
import slotmachien.signals.ButtonSignal;
import slotmachien.signals.Command;

/**
 * 
 * Listens to buttons, converts them into events for the motor handler
 * 
 * @author don
 *
 */
public class ButtonHandler extends Mapper<ButtonSignal, Command> {

   

    @Override
    public Command map(ButtonSignal signal) {
        switch (signal.source.getId()) {
        case Button.ID_LEFT:
           return new Command(Position.OPEN, "Button open");
        case Button.ID_RIGHT:
            return new Command(Position.CLOSED, "Button close");
        }
        return null;
    }

}
