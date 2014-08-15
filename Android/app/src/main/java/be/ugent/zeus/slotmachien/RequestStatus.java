package be.ugent.zeus.slotmachien;

import android.graphics.Color;

/**
 * Created by Lorin.
 */
public enum RequestStatus {
    OK(Color.GREEN), ERROR(Color.RED), PROCESSING(Color.BLUE);
    private final int color;

    private RequestStatus(int color) {
        this.color = color;
    }

    public int getColor() {
        return color;
    }
}
