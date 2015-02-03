package song;

import lejos.nxt.Sound;

public class Song {
    private Note[] notes;
    private int[] inst;
    private int bpm;
    private int shift;
    
    public Song(int bpm, int[] inst, int shift, Note... notes) {
        this.bpm = bpm;
        this.shift = shift;
        this.inst = inst;
        this.notes = notes;
    }

    public void play() {
        new Thread(new Runnable() {

            @Override
            public void run() {
                for(Note note : notes) {
                    Sound.playNote(inst, note.freq * shift, note.duration * bpm);
                }
            }

        }).start();
    }

}
