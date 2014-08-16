package be.ugent.zeus.slotmachien.data;

import android.graphics.Color;

public enum State {
    CLOSED("closed", Color.RED), OPEN("open", Color.GREEN), UNKNOWN("unknown", Color.WHITE);

    private String text;
    private int color;

    private State(String text, int color) {
        this.text = text;
        this.color = color;
    }

    public String getText() {
        return text;
    }

    public int getColor() {
        return color;
    }
}
