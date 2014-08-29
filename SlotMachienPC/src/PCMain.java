import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import lejos.pc.comm.NXTComm;
import lejos.pc.comm.NXTCommException;
import lejos.pc.comm.NXTCommFactory;
import lejos.pc.comm.NXTInfo;

public class PCMain {

    public static void main(String[] args) throws NXTCommException,
            IOException, InterruptedException {
         
        NXTComm nxtComm = NXTCommFactory.createNXTComm(NXTCommFactory.USB);
        NXTInfo[] nxtInfo = nxtComm.search(null);
        nxtComm.open(nxtInfo[0]);
        
        OutputStream os = nxtComm.getOutputStream();
        InputStream is = nxtComm.getInputStream();
        
        Pipe.make(System.in, os);
        Pipe.make(is, System.out);
    }
}
