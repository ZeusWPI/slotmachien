package slotmachien.debug;

import slotmachien.internal.UsbIO;
import lejos.nxt.Button;


/**
 * Contains code to test USB-internal
 * @author pietervdvn
 *
 */
public class UsbDebug {
	
	public static void main() {
		System.out.println("USB-debug!");
		
		UsbIO io = UsbIO.waitForUsbIO();
		
		System.out.println("IO CONNECTED!");
		try {
			io.writeLine("Online!");
			System.out.println("READ: "+io.readLine());
			io.writeLine("Hi");
			io.close();
			System.out.println("Bye!");
		} catch (Exception e) {
			System.out.println("CAUGHT EXC");
			System.out.println(e.getMessage());
		}
		
		System.out.println("Done!");
		Button.waitForAnyPress();
		
		
		
	}
	

}
