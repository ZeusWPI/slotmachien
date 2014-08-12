import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import lejos.pc.comm.NXTComm;
import lejos.pc.comm.NXTCommException;
import lejos.pc.comm.NXTCommFactory;
import lejos.pc.comm.NXTInfo;

public class PCMain {

	private static final Map<String, Byte> OUT_TOLK = new HashMap<String, Byte>() {

		private static final long serialVersionUID = 1L;

		{
			put("open", (byte) 1);
			put("close", (byte) 2);
			put("status", (byte) 3);
		}
	};
	
	private static final Map<Byte, String> IN_TOLK = new HashMap<Byte, String>() {

		{
			put((byte) 1, "open");
			put((byte) 2, "close");
		}
	};	 
	

	public static void main(String[] args) throws NXTCommException,
			IOException, InterruptedException {

		// communicatie opstellen met de brick
		NXTComm nxtComm = NXTCommFactory.createNXTComm(NXTCommFactory.USB);
		NXTInfo[] nxtInfo = nxtComm.search(null);
		nxtComm.open(nxtInfo[0]);
		
		try {
			// datastream richting brick opstellen op basis van de stdin
			OutputStream oStream = nxtComm.getOutputStream();
			try {
				String commando = args[0].toLowerCase();
				if (OUT_TOLK.containsKey(commando)) {
					oStream.write(OUT_TOLK.get(commando));
					oStream.flush();
					if (OUT_TOLK.get(commando) == (byte) 3) {
						InputStream iStream = nxtComm.getInputStream();
						System.out.println("stream established");
						byte b = (byte) iStream.read();
						System.out.print(IN_TOLK.get(b));
						iStream.close();
					}
				}
			} catch (Exception ex) {
				ex.printStackTrace(System.err);
			} finally {
				oStream.close();
			}
		} catch (Exception ex) {
			ex.printStackTrace(System.err);
		} finally {
			nxtComm.close();
		}
	}
}
