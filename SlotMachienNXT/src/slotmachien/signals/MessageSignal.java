package slotmachien.signals;


/**
 * A signal representing a message
 * 
 * @author pietervdvn
 *
 */
public class MessageSignal extends Signal{

    public final String head;
	public final String body;

	public MessageSignal(String head, String body) {
		this.head = head;
		this.body = body;
	}
	
	@Override
	public String toString() {
	    return head + ";" + body;
	}
}
