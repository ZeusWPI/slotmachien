package be.ugent.zeus.slotmachien.data;

import android.content.Context;
import android.preference.PreferenceManager;

/**
 * Created by wouter on 16/08/14.
 */
public class Model {

    private static final String STATE = "be.ugent.zeus.slotmachien.STATE";
    private static final String URL = "be.ugent.zeus.slotmachien.URL";
    private static final String TOKEN = "be.ugent.zeus.slotmachien.TOKEN";
    private static final String USERNAME = "be.ugent.zeus.slotmachien.USERNAME";

    public static int getState(Context c) {
        return getValue(c, STATE, State.UNKNOWN.ordinal());
    }

    public static void setState(Context c, int value) {
        setValue(c, STATE, value);
    }

    public static String getURL(Context c) {
        return getValue(c, URL, "");
    }

    public static void setURL(Context c, String value) {
        setValue(c, URL, value);
    }

    public static String getToken(Context c) {
        return getValue(c, TOKEN, "");
    }

    public static void setToken(Context c, String value) {
        setValue(c, TOKEN, value);
    }

    public static String getUsername(Context c) {
        return getValue(c, USERNAME, "");
    }

    public static void setUsername(Context c, String value) {
        setValue(c, USERNAME, value);
    }

    private static int getValue(Context context, String data, int defaultValue) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getInt(data, defaultValue);
    }

    private static void setValue(Context context, String data, int value) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putInt(data, value)
                .commit();
    }

    private static String getValue(Context context, String data, String defaultValue) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getString(data, defaultValue);
    }

    private static void setValue(Context context, String data, String value) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putString(data, value)
                .commit();
    }
}
