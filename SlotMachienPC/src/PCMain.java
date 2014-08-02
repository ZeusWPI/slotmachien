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
    
    private static final Map<String, Byte> TOLK = new HashMap<String, Byte>() {{
        put("open", (byte) 1);
        put("close", (byte) 2); 
        put("restart", (byte) 3);
        put("quit", (byte) 4);        
    }};

    public static void main(String[] args) throws NXTCommException,
            IOException, InterruptedException {

        // communicatie opstellen met de brick
        NXTComm nxtComm = NXTCommFactory.createNXTComm(NXTCommFactory.USB);
        NXTInfo[] nxtInfo = nxtComm.search(null);
        nxtComm.open(nxtInfo[0]);

        // datastream richting brick opstellen op basis van de stdin
        OutputStream oStream = nxtComm.getOutputStream();
        Scanner sc = new Scanner(System.in);
        while (true) {
            String s = sc.nextLine();
            if (TOLK.containsKey(s.toLowerCase())) {
                oStream.write(TOLK.get(s));
                oStream.flush();                
            }
            sc.close();    
        }
    }

}
