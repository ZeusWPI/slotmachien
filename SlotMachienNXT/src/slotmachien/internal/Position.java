package slotmachien.internal;

public enum Position {
    OPEN(-180), CLOSED(0);
    
    private final int pos;
    
    private Position(int pos) {
    	this.pos = pos;
    }
    
    public int getPos() {
		return pos;
	}
}
