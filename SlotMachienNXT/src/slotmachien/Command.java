package slotmachien;

public class Command {
	public final Position pos;
	public final String comment;
	
	public Command(Position pos, String comment) {
		this.pos = pos;
		this.comment = comment;
	}
}
