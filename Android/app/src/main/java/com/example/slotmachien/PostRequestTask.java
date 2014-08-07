package com.example.slotmachien;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import android.util.Log;

public class PostRequestTask extends RequestTask {

    @Override
    protected HttpResponse doInBackground(String... msg) {
        HttpClient hc = new DefaultHttpClient();
        HttpPost post = new HttpPost(SLOTMACHIEN_URL);
        publishProgress(1);

        post.addHeader("Content-Type", "application/json");
        post.addHeader("Authorization", "test");
        publishProgress(1);

        HttpResponse res = null;
        try {
            post.setEntity(new StringEntity("{ \"action\" : \"" + msg[0]
                    + "\" }"));
            res = hc.execute(post);
            publishProgress(1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }

    @Override
    protected void onPostExecute(HttpResponse res) {
        Log.d("post", "" + res.getStatusLine().getStatusCode() + " "
                + res.getStatusLine().getReasonPhrase());
    }
    
    @Override
    protected void onProgressUpdate(Integer...integers) {
        System.out.println(integers[0]);
    }

}
