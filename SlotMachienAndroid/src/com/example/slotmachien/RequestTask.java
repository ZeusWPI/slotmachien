package com.example.slotmachien;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.http.HttpResponse;

import android.os.AsyncTask;

public abstract class RequestTask extends AsyncTask<String, Void, HttpResponse> {

    //TODO Fix this horrible shit
    static {
        Properties p = new Properties();
        InputStream input = RequestTask.class.getResourceAsStream("info.properties");
        try {
            p.load(input);
        } catch (IOException e) {
            e.printStackTrace();
        }
        SLOTMACHIEN_URL = p.getProperty("slotmachien_url");
        TOKEN_VALUE = p.getProperty("token_value");
    }
    public static final String SLOTMACHIEN_URL;
    public static final String TOKEN_VALUE;
    
}
