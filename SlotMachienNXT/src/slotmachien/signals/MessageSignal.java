package slotmachien.signals;

import observable.Signal;

/**
 * A signal representing a message
 * 
 * @author pietervdvn
 *
 */
public class MessageSignal extends Signal{

	public final String content;

	public MessageSignal(String content) {
		this.content = content;
	}
}
