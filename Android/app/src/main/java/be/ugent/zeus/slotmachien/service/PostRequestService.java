package be.ugent.zeus.slotmachien.service;

import android.content.Intent;

import org.apache.http.HttpResponse;
import org.apache.http.auth.AUTH;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.json.JSONObject;

import be.ugent.zeus.slotmachien.MainActivity;

/**
 * Created by Lorin.
 */
public class PostRequestService extends RequestService {

    @Override
    protected void onHandleIntent(Intent intent) {
        HttpPost post = new HttpPost(SLOTMACHIEN_URL);

        post.addHeader("Content-Type", CONTENT_TYPE);
        post.addHeader("Authorization", AUTHORIZATION);

        HttpResponse res;
        String s = null;
        JSONObject json = new JSONObject();
        boolean success = true;
        try {
            json.put("action", intent.getStringExtra(MESSAGE));
            json.put("user_name", USER_NAME);
            post.setEntity(new StringEntity(json.toString()));
            System.out.println(json.toString());
            res = client.execute(post);
            s = res.getStatusLine().getStatusCode() + " " + res.getStatusLine().getReasonPhrase();
        } catch (Exception e) {
            success = false;
            e.printStackTrace();
        }

        Intent broadcastIntent = new Intent();
        broadcastIntent.setAction(MainActivity.ResponseReceiver.PROCESSED);
        broadcastIntent.addCategory(Intent.CATEGORY_DEFAULT);
        broadcastIntent.putExtra(RESPONSE, s);
        broadcastIntent.putExtra(SUCCESS, success);
        sendBroadcast(broadcastIntent);
    }
}
