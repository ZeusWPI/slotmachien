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
            System.exit(1);
        }
        
        safeOpen(nxtComm, nxtInfo[0], "./.lockmachien");

        
        OutputStream os = nxtComm.getOutputStream();
        InputStream is = nxtComm.getInputStream();
        
        Pipe.make(is, System.out);
        Pipe.make(System.in, os).join();  // Wait for end of input
    }
    
    private static void safeOpen(final NXTComm comm, final NXTInfo conn, String lockpath) throws NXTCommException, IOException{
    	
    	LockFile lock = new LockFile(lockpath);
    	lock.create();
    	
        comm.open(conn);
        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
        	@Override
        	public void run() {
        		try {
        			comm.close();
        		} catch (IOException e) {}
        	}
        }));
    	
    }
}