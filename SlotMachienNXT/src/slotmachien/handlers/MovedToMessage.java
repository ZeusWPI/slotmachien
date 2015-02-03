package slotmachien.handlers;

import observable.Mapper;
import slotmachien.signals.MovedToSignal;
import slotmachien.signals.MessageSignal;

/**
 * 
 * Prints changes to usb!
 * @author pietervdvn
 *
 */
public class MovedToMessage extends Mapper<MovedToSignal, MessageSignal> {

	@Override
	public MessageSignal map(MovedToSignal t) {
		return new MessageSignal(t.pos.toString(), t.comment);
	}
}
