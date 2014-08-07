package com.example.slotmachien;

import android.os.AsyncTask;

import org.apache.http.HttpResponse;

import java.util.Properties;

public abstract class RequestTask extends
        AsyncTask<String, Integer, HttpResponse> {

    // TODO Fix this horrible shit
    static {
        Properties p = new Properties();
        try {

            p.load(RequestTask.class.getClassLoader().getResourceAsStream("assets/slotmachien.properties"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        SLOTMACHIEN_URL = p.getProperty("slotmachien_url");
        TOKEN_VALUE = p.getProperty("token_value");
    }

    public static final String SLOTMACHIEN_URL;
    public static final String TOKEN_VALUE;
}
