import java.io.IOException;
import java.io.OutputStream;
import java.util.Scanner;

import lejos.pc.comm.*;

public class PCMain {

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
            byte a = sc.nextByte();
            oStream.write(new byte[] { a });
            oStream.flush();

        }
    }

}
