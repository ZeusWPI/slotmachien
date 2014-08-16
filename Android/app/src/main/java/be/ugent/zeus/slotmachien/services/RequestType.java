package be.ugent.zeus.slotmachien.services;

/**
 * Created by wouter on 16/08/14.
 */
public enum RequestType {
    NONE("none"), OPEN("open"), CLOSE("close"), STATUS("status");

    private String actionString;

    private RequestType(String actionString) {
        this.actionString = actionString;
    }

    public String getAction() {
        return actionString;
    }
}
