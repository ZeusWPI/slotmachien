package be.ugent.zeus.slotmachien.service;

import android.content.Intent;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;

import be.ugent.zeus.slotmachien.MainActivity;

/**
 * Created by Lorin.
 */
public class PostRequestService extends RequestService {

    public static final String RESPONSE = "RESPONSE";

    @Override
    protected void onHandleIntent(Intent intent) {
        HttpPost post = new HttpPost(SLOTMACHIEN_URL);

        post.addHeader("Content-Type", CONTENT_TYPE);
        post.addHeader("Authorization", AUTHORIZATION);

        HttpResponse res;
        String s = "Undefined";
        try {
            post.setEntity(new StringEntity("{ \"action\" : \"" + intent.getStringExtra(MESSAGE) + "\" }"));
            res = client.execute(post);
            s = res.getStatusLine().getReasonPhrase() + " " + res.getStatusLine().getStatusCode();
        } catch (Exception e) {
            e.printStackTrace();
        }

        Intent broadcastIntent = new Intent();
        broadcastIntent.setAction(MainActivity.ResponseReceiver.PROCESSED);
        broadcastIntent.addCategory(Intent.CATEGORY_DEFAULT);
        broadcastIntent.putExtra(RESPONSE, s);
        sendBroadcast(broadcastIntent);
    }
}
