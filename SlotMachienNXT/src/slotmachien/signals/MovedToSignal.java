package slotmachien.signals;

import observable.Signal;
import slotmachien.internal.Position;

public class MovedToSignal extends Signal {
	public final Position pos;
	public final String comment;
	
	public MovedToSignal(Position pos, String comment) {
		this.pos = pos;
		this.comment = comment;
	}

	public MovedToSignal(Command currentState) {
		this(currentState.pos, currentState.comment);
	}
	
	@Override
	public String toString() {
		String p	= pos.toString().substring(0,1)+ pos.toString().toLowerCase().substring(1);
		return p + " " + comment;
	}
}
