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
        
        final NXTComm nxtComm = NXTCommFactory.createNXTComm(NXTCommFactory.USB);
        NXTInfo[] nxtInfo = nxtComm.search(null);
        if (nxtInfo.length == 0){
        	System.out.println("No connection!");
            System.exit(1);
        }
        
        final OutputStream os = nxtComm.getOutputStream();
        final InputStream is = nxtComm.getInputStream();
        
        nxtComm.open(nxtInfo[0]);

        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
        	@Override
        	public void run() {
        		try {
				os.close();
				is.close();
        			nxtComm.close();
				System.exit(0);
        		} catch (IOException e) {
				System.exit(1);
			}
        	}
        }));

        Pipe.make(is, System.out);
        Pipe.make(System.in, os).join();  // Wait for end of input
    }
    
}
