package slotmachien;

import observable.Signal;

public class Command extends Signal{
	public final Position pos;
	public final String comment;
	
	public Command(Position pos, String comment) {
		this.pos = pos;
		this.comment = comment;
	}
}
