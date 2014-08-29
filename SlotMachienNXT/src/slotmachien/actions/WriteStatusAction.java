package slotmachien.actions;

import io.UsbIO;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

import lejos.nxt.Sound;
import observable.Observer;
import slotmachien.SMMotorHandler;

public class WriteStatusAction extends Action {
    private SMMotorHandler smmh;
    private UsbIO conn;
    
    public WriteStatusAction(SMMotorHandler smmh, UsbIO conn){
        this.smmh = smmh;
        this.conn = conn;
    }
    
    @Override
    public void perform() {
        try {
           conn.writeLine(smmh.getStatus().toString());
        } catch (IOException e) {
            // Connection failure, this writer is obsolete and a new one should be created.
            smmh.removeObserver(this);
        }
    }

}
