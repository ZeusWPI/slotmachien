package slotmachien;

import io.UsbIO;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import lejos.nxt.LCD;
import lejos.nxt.Sound;
import lejos.util.Delay;
import observable.AbstractObservable;
import observable.Observable;
import observable.Observer;

public class StreamObserver {
    
    private UsbIO conn;
    private Map<String, Observable> lineMap;
    
    public StreamObserver(UsbIO conn){
        this.conn = conn;
        this.lineMap = new HashMap<>();
        new Thread(new LineReader()).start();
    }
    
    public Observable getObserver(String line){
        Observable o = lineMap.get(line);
        if (o == null){
            o = new LineOccurence();
            lineMap.put(line, o);
        }
        return o;
        
    }
    public void addObserver(String line, Observer o){
        Observable obs = getObserver(line);
        obs.addObserver(o);
    }
    
    public void removeObserver(String line, Observer o){
        Observable obs = lineMap.get(line);
        if (obs != null){
            obs.removeObserver(o);
            if (obs.observerCount() == 0){
                lineMap.remove(obs);
            }
        }
    }
    
    class LineOccurence extends AbstractObservable {
    }
    
    class LineReader implements Runnable {
        @Override
        public void run() {
            try{
                while(true){
                    String line = conn.readLine();
                    LCD.clear();
                    LCD.drawString(line, 0, 0);
                    Delay.msDelay(1000);
                    // commands should be shorter than 6 chars because leJOS
                    if (line.length() < 6 && lineMap.containsKey(line)){
                        lineMap.get(line).notifyObservers();
                    }
                }
            } catch (IOException e) {
                // Stream ended
            }
        }
    }
}
