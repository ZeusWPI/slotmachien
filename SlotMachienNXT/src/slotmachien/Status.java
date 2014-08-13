package slotmachien;
public enum Status {
	OPEN(0), CLOSED(1), DEADZONED(2);

	private byte b;

	private Status(int b) {
		this.b = (byte) b;
	}

	public byte toByte() {
		return b;
	}
}