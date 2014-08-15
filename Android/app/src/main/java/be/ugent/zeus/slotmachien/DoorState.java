package be.ugent.zeus.slotmachien;

import android.graphics.Color;

public enum DoorState {
    CLOSED("closed", Color.RED), OPEN("open", Color.GREEN), ERROR("error", Color.DKGRAY), UNKNOWN("unknown", Color.WHITE);

    private String text;
    private int color;
    private DoorState(String text, int color) {
        this.text = text;
        this.color = color;
    }

    public String getText(){
        return text;
    }

    public int getColor(){
        return color;
    }
}
