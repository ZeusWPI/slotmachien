package com.example.slotmachien;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

public class PostRequestTask extends RequestTask {

    protected HttpResponse doInBackground(String... msg) {
        HttpClient hc = new DefaultHttpClient();
        HttpPost post = new HttpPost(SLOTMACHIEN_URL);

        List<NameValuePair> pairs = new ArrayList<>();
        pairs.add(new BasicNameValuePair("token", TOKEN_VALUE));

        HttpResponse res = null;
        try {
            post.setEntity(new UrlEncodedFormEntity(pairs));
            res = hc.execute(post);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }

}
