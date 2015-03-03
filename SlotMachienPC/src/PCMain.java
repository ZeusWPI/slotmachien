import java.io.IOException;
import java.io.PrintStream;
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
            System.out.println("noconnection");
            System.exit(1);
        }

        final OutputStream os = nxtComm.getOutputStream();
        final InputStream is = nxtComm.getInputStream();

        nxtComm.open(nxtInfo[0]);

        System.out.println("openconnection");

        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            @Override
            public void run() {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        PrintStream out = System.out;
                        System.setOut(System.err);

                        try {
                            nxtComm.close();
                        } catch (Exception e){}

                        System.setOut(out);
                    }
                }).start();
		try {
		    Thread.sleep(500);
            System.out.println("lostconnection");
		} catch(Exception e) {}
            }
        }));

        Pipe.make(is, System.out);
        Pipe.make(System.in, os).join();  // Wait for end of input
    }

}
