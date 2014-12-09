package io;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import lejos.nxt.comm.NXTConnection;
import lejos.nxt.comm.USB;

// SHIT'S FUCKED YO
public class UsbIO {
    
    private NXTConnection conn;
    private BufferedReader reader;
    private BufferedWriter writer;
    private Runnable onBreak;
    
    public UsbIO(NXTConnection conn){
        this.conn = conn;
        DataInputStream in = conn.openDataInputStream();
        // Buffered reader is required for generating an OutOfMemoryException #shitsfucked
        this.reader = new BufferedReader(new InputStreamReader(in));
        
        DataOutputStream out = conn.openDataOutputStream();
        this.writer = new BufferedWriter(new OutputStreamWriter(out));
    }
    
    public void setOnBreak(Runnable r){
        this.onBreak = r;
    }
    
    public static UsbIO waitForUsbIO(){
        NXTConnection conn = USB.waitForConnection();
        return new UsbIO(conn);
    }
    
    public void writeLine(String line) throws IOException{
        writer.write(line);
        writer.newLine();
        writer.flush();
    }
    
    public String readLine() throws IOException{
    	char temp;
    	String s = "";
    	do {
    		temp = (char) reader.read();
    		System.out.print(temp);

    		if(temp == '\n') {
    	    	System.out.println(s);
    	        return s;
    		}

    		s = s + temp;
    		
    		if(s.length() > 6 || temp == '\0') {
                // OutOfMemoryError implies a broken connection
                close();
                new Thread(onBreak).start();
                System.out.println("NULL");
                throw new IOException();
    		}
    	} while(temp != '\n');
    	return null;
    }
    
    public void close() throws IOException{
        writer.close();
        reader.close();
        conn.close();
    }
    
}
