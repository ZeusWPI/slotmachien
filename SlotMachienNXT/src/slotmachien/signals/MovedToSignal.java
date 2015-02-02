package slotmachien.signals;

import observable.Signal;
import slotmachien.internal.Position;

public class MovedToSignal extends Signal {
	public final Position pos;
	public final String comment;
	public final Command causedBy;
	
	public MovedToSignal(Position pos, String comment, Command causedBy) {
		this.pos = pos;
		this.comment = comment;
		this.causedBy = causedBy;
	}

	public MovedToSignal(Command currentState) {
		this(currentState.pos, currentState.comment, currentState);
	}
	
	@Override
	public String toString() {
		return pos.toString().toLowerCase() + ";" + comment;
	}
}
