package slotmachien;

import java.io.File;

import lejos.nxt.Sound;
import lejos.util.Delay;

public class CenterAction implements Action {

    @Override
    public void performAction() {
        // MAAK KABAAL LOL
        NXTMain.drawString("LOLOLOLOLOLOLOL");
        File muziekje = new File("muziekje.wav");
        Sound.playSample(muziekje, 100);
        Delay.msDelay(11000);
        NXTMain.drawString(NXTMain.MOTORS.getStatus().toString());
    }
}
