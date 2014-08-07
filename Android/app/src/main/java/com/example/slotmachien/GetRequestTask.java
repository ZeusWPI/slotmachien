package com.example.slotmachien;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.IOException;

public class GetRequestTask extends RequestTask {

    public GetRequestTask(MainActivity main) {
        super(main);
    }

    @Override
    protected HttpResponse doInBackground(String... params) {
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
