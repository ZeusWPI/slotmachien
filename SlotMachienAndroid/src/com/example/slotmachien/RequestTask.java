package com.example.slotmachien;

import org.apache.http.HttpResponse;

import android.os.AsyncTask;

public abstract class RequestTask extends AsyncTask<String, Void, HttpResponse> {

    public static final String SLOTMACHIEN_URL = "http://requestb.in/ueyr6uue";
    public static final String TOKEN_VALUE = "test";
    
}
