import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
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
			put((byte) 0, "open");
			put((byte) 1, "closed");
			put((byte) 2, "deadzoned");
		}
	};

	public static void main(String[] args) throws NXTCommException,
			IOException, InterruptedException {

		// communicatie opstellen met de brick
		PrintStream oldOut = System.out;
		System.setOut(System.err);
		NXTComm nxtComm = NXTCommFactory.createNXTComm(NXTCommFactory.USB);
		NXTInfo[] nxtInfo = nxtComm.search(null);
		nxtComm.open(nxtInfo[0]);
		System.setOut(oldOut);

		// datastream richting brick opstellen op basis van de stdin
		try (OutputStream oStream = nxtComm.getOutputStream()) {
			String commando = args[0].toLowerCase();
			if (OUT_TOLK.containsKey(commando)) {
				oStream.write(OUT_TOLK.get(commando));
				oStream.flush();
				try (InputStream iStream = nxtComm.getInputStream()) {
					byte b = (byte) iStream.read();
					System.out.println(IN_TOLK.get(b));
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace(System.err);
			System.exit(1);
		}

		nxtComm.close();
	}
}
