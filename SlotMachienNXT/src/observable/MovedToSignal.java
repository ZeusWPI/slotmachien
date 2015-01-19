package observable;

import slotmachien.Command;
import slotmachien.Position;

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
}
