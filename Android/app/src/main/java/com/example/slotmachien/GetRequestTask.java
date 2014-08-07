package com.example.slotmachien;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

public class GetRequestTask extends RequestTask {

    @Override
    protected HttpResponse doInBackground(String... params) {
        HttpClient client = new DefaultHttpClient();
        HttpGet request = new HttpGet(SLOTMACHIEN_URL);

        HttpResponse response = null;
        try {
            client.execute(request);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return response;
    }

}
