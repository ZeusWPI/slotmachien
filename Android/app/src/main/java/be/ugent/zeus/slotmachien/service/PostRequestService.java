package be.ugent.zeus.slotmachien.service;

import android.content.Intent;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;

import be.ugent.zeus.slotmachien.MainActivity;

/**
 * Created by Lorin on 8/08/2014.
 */
public class PostRequestService extends RequestService {

    @Override
    protected void onHandleIntent(Intent intent) {
        HttpPost post = new HttpPost(SLOTMACHIEN_URL);

        post.addHeader("Content-Type", CONTENT_TYPE);
        post.addHeader("Authorization", AUTHORIZATION);

        HttpResponse res = null;
        try {
            post.setEntity(new StringEntity("{ \"action\" : \"" + intent.getStringExtra(MESSAGE) + "\" }"));
            res = client.execute(post);
            System.out.println(res.getStatusLine().getReasonPhrase() + " " + res.getStatusLine().getStatusCode());
        } catch (Exception e) {
            e.printStackTrace();
        }

        Intent broadcastIntent = new Intent();
        broadcastIntent.setAction(MainActivity.ResponseReceiver.PROCESSED);
        broadcastIntent.addCategory(Intent.CATEGORY_DEFAULT);
        sendBroadcast(broadcastIntent);
    }
}
