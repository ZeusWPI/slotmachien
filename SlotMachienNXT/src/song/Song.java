package song;

import lejos.nxt.Sound;

public class Song {
    private Note[] notes;
    private int[] inst;
    private int bpm;
    private int shift;

    private Thread playing = null;
    private SongRunnable songrunnable;

    public Song(int bpm, int[] inst, int shift, Note... notes) {
        this.bpm = bpm;
        this.shift = shift;
        this.inst = inst;
        this.notes = notes;
    }

    public void play() {
        if (playing == null) {
            songrunnable = new SongRunnable();
            playing = new Thread(songrunnable);
            playing.start();
        } else {
            songrunnable.stop();
            playing = null;
            songrunnable = null;
        }
    }

    private class SongRunnable implements Runnable {

        private volatile boolean stop = false;

        @Override
        public void run() {
            for (Note note : notes) {
                if (stop) {
                    return;
                }
                Sound.playNote(inst, note.freq * shift, note.duration * bpm);
            }
        }

        public void stop() {
            stop = true;
        }

    }

}
