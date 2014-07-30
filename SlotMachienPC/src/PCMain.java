import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Scanner;

import lejos.pc.comm.*;

public class PCMain {

	public static void main(String[] args) throws NXTCommException,
			IOException, InterruptedException {
		NXTComm nxtComm = NXTCommFactory.createNXTComm(NXTCommFactory.USB);
		NXTInfo[] nxtInfo = nxtComm.search(null);
		System.out.println(Arrays.deepToString(nxtInfo));
		nxtComm.open(nxtInfo[0]);
		Thread.sleep(1000);
		int b = 1;
		System.out.print(b);
		OutputStream oStream = nxtComm.getOutputStream();
		Scanner sc = new Scanner(System.in);
		while (true) {
			byte a = sc.nextByte();
			oStream.write(new byte[] {a});
			oStream.flush();
		}
		// InputStream iStream = nxtComm.getInputStream();

	}

}
