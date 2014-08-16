package be.ugent.zeus.slotmachien.services;

import android.graphics.Color;

/**
 * Created by wouter on 16/08/14.
 */
public enum RequestResponse {
    TIMEOUT(Color.RED, "socket timeout"), UNSPECIFIED_REQUEST(Color.RED, "unspecified request"), OK(Color.GREEN, "ok"), BAD_REQUEST(Color.RED, "bad request"), UNAUTHORIZED(Color.RED, "unauthorized"), INTERNAL_SERVER_ERROR(Color.RED, "internal server error"), UNKNOWN_ERROR(Color.RED, "unknown error");
    private final int color;
    private final String text;

    private RequestResponse(int color, String text) {
        this.color = color;
        this.text = text;
    }

    public int getColor() {
        return color;
    }

    public String getText() {
        return text;
    }
}
