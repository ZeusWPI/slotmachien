package slotmachien.handlers;

import lejos.nxt.Button;
import observable.Mapper;
import slotmachien.Command;
import slotmachien.Position;
import slotmachien.signals.ButtonSignal;

/**
 * 
 * Listens to buttons, converts them into events for the motor handler
 * 
 * @author don
 *
 */
public class ButtonHandlers extends Mapper<ButtonSignal, Command> {

   

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
