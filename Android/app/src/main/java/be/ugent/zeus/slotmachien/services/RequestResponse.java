package be.ugent.zeus.slotmachien.services;

import android.graphics.Color;

/**
 * Created by wouter on 16/08/14.
 */
public enum RequestResponse {
    NO_CONNECTION("no internet connection"),
    TIMEOUT("socket timeout"),
    UNSPECIFIED_REQUEST("unspecified request"),
    OK("ok"),
    BAD_REQUEST("bad request"),
    UNAUTHORIZED("unauthorized"),
    INTERNAL_SERVER_ERROR("internal server error"),
    UNKNOWN_ERROR("unknown error");

    private final String text;

    private RequestResponse(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return text;
    }
}
