import java.io.BufferedWriter;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.Scanner;

// Connect streams!
public class Pipe implements Runnable {
    private InputStream in;
    private OutputStream out;

    public Pipe(InputStream in, OutputStream out) {
        this.in = in;
        this.out = out;
    }

    @Override
    public void run(){
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(in));
             BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out))) {
            while (true) {
                String line = reader.readLine();
                writer.write(line);
                writer.newLine();
                writer.flush();
            }
        } catch (Exception e) {
            System.out.println("lostconnection");
            //e.printStackTrace();
        } finally {
            System.out.println("lostconnection");
            System.exit(1);
        }
    }
    
    public static Thread make(InputStream in, OutputStream out){
        Thread t = new Thread(new Pipe(in, out));
        t.start();
        return t;
    }
}
