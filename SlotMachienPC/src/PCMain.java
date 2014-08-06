import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import lejos.pc.comm.NXTComm;
import lejos.pc.comm.NXTCommException;
import lejos.pc.comm.NXTCommFactory;
import lejos.pc.comm.NXTInfo;

public class PCMain {

    private static final Map<String, Byte> TOLK = new HashMap<String, Byte>() {

        private static final long serialVersionUID = 1L;

        {
            put("open", (byte) 1);
            put("close", (byte) 2);
            put("restart", (byte) 3);
            put("test", (byte) 4);
            put("clear", (byte) 5);
            put("quit", (byte) 6);
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
                if (TOLK.containsKey(commando)) {
                    oStream.write(TOLK.get(commando));
                    System.out.print(commando);
                    oStream.flush();
                }
            } catch (Exception ex) {

            } finally {
                oStream.close();
            }
        } catch (Exception ex) {

        } finally {
            nxtComm.close();
        }
    }
}
