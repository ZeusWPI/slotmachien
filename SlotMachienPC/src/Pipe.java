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
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out));
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        try {
            while (true) {
                String line = reader.readLine();
                writer.write(line);
                writer.newLine();
                writer.flush();
            }
        } catch (Exception e) {
            System.exit(0);
        }
    }
    
    public static Thread make(InputStream in, OutputStream out){
        Thread t = new Thread(new Pipe(in, out));
        t.start();
        return t;
    }
}
