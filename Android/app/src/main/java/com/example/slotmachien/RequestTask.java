package com.example.slotmachien;

import android.os.AsyncTask;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

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
        AUTHORIZATION = p.getProperty("authorization");
        CONTENT_TYPE = p.getProperty("content_type");
    }

    public static final String SLOTMACHIEN_URL;
    public static final String AUTHORIZATION;
    public static final String CONTENT_TYPE;

    private MainActivity main;
    protected HttpClient client;

    public RequestTask(MainActivity main) {
        this.main = main;

        HttpParams params = new BasicHttpParams();
        int connectionTimeout = 3000;
        int socketTimeout = 5000;
        HttpConnectionParams.setConnectionTimeout(params, connectionTimeout);
        HttpConnectionParams.setSoTimeout(params, socketTimeout);
        this.client = new DefaultHttpClient(params);

        main.startProgressBar();
    }

    @Override
    protected void onPostExecute(HttpResponse res) {
        main.stopProgressBar();
    }
}
