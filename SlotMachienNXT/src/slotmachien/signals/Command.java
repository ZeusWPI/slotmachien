package slotmachien.signals;

import observable.Signal;
import slotmachien.internal.Position;

public class Command extends Signal{
	public final Position pos;
	public final String comment;
	
	public Command(Position pos, String comment) {
		this.pos = pos;
		this.comment = comment;
	}
	
	@Override
	public String toString() {
		return new MovedToSignal(this).toString();
	}
}
