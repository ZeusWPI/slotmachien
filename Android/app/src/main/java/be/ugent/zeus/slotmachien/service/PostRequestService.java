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

    public static final String RESPONSE = "RESPONSE";

    @Override
    protected void onHandleIntent(Intent intent) {
        HttpPost post = new HttpPost(SLOTMACHIEN_URL);

        post.addHeader("Content-Type", CONTENT_TYPE);
        post.addHeader("Authorization", AUTHORIZATION);

        HttpResponse res = null;
        try {
            post.setEntity(new StringEntity("{ \"action\" : \"" + intent.getStringExtra(MESSAGE) + "\" }"));
            System.out.println("executing");
            res = client.execute(post);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            Intent broadcastIntent = new Intent();
            broadcastIntent.setAction(MainActivity.ResponseReceiver.PROCESSED);
            broadcastIntent.addCategory(Intent.CATEGORY_DEFAULT);
            broadcastIntent.putExtra(RESPONSE, res.getStatusLine().getReasonPhrase() + " " + res.getStatusLine().getStatusCode());
            sendBroadcast(broadcastIntent);
        }
    }
}
