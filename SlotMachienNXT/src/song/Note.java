package song;

public class Note {
    
    public final int freq;
    public final int duration;

    public static final int C = 262;
    public static final int D = 294;
    public static final int E = 330;
    public static final int F = 349;
    public static final int G = 392;
    
    public Note(int freq, int duration) {
        this.freq = freq;
        this.duration = duration;
    }
}
