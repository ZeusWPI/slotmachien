package com.example.slotmachien;

import java.util.Properties;

import org.apache.http.HttpResponse;

import android.os.AsyncTask;

public abstract class RequestTask extends
        AsyncTask<String, Integer, HttpResponse> {

    // TODO Fix this horrible shit
    static {
        Properties p = new Properties();
        try {

            p.load(RequestTask.class.getClassLoader().getResourceAsStream("slotmachien.properties"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        SLOTMACHIEN_URL = p.getProperty("slotmachien_url");
        TOKEN_VALUE = p.getProperty("token_value");
    }
    public static final String SLOTMACHIEN_URL;
    public static final String TOKEN_VALUE;
}
