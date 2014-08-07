package com.example.slotmachien;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;

public class PostRequestTask extends RequestTask {

    public PostRequestTask(MainActivity main) {
        super(main);
    }

    @Override
    protected HttpResponse doInBackground(String... msg) {
        HttpPost post = new HttpPost(SLOTMACHIEN_URL);

        post.addHeader("Content-Type", CONTENT_TYPE);
        post.addHeader("Authorization", AUTHORIZATION);

        HttpResponse res = null;
        try {
            post.setEntity(new StringEntity("{ \"action\" : \"" + msg[0] + "\" }"));
            res = client.execute(post);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }

}
