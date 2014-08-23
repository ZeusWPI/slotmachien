package slotmachien;

public enum Status {
    OPEN(0, NXTMain.POSITION_OPEN), CLOSED(1, NXTMain.POSITION_CLOSED), DEADZONED(2, 60);

    private byte b;
    private int position;

    private Status(int b, int position) {
        this.b = (byte) b;
        this.position = position;
    }

    public byte toByte() {
        return b;
    }

    public int getPosition() {
    	return position;
    }

}
