import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

// Connect streams!
public class Pipe implements Runnable {
    private InputStream in;
    private OutputStream out;

    public Pipe(InputStream in, OutputStream out) {
        this.in = in;
        this.out = out;
    }

    @Override
    public void run() {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(in));
             BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out))) {
            while (true) {
                writer.write(reader.readLine());
                writer.newLine();
                writer.flush();
            }
        } catch (IOException e) {
            System.out.println("Something broke");
        }
    }
    
    public static void make(InputStream in, OutputStream out){
        new Thread(new Pipe(in, out)).start();
    }
}
