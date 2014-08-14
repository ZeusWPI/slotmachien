package be.ugent.zeus.slotmachien;

import android.graphics.Color;

/**
 * Created by Lorin.
 */
public enum Status {
    OK(Color.GREEN), ERROR(Color.RED), PROCESSING(Color.BLUE);
    private final int color;

    private Status(int color) {
        this.color = color;
    }

    public int getColor() {
        return color;
    }
}
