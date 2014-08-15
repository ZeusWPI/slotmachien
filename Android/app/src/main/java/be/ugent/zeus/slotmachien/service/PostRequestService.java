package be.ugent.zeus.slotmachien.service;

import android.content.Intent;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.json.JSONObject;

import be.ugent.zeus.slotmachien.DoorState;
import be.ugent.zeus.slotmachien.LocalConstants;
import be.ugent.zeus.slotmachien.MainActivity;
import be.ugent.zeus.slotmachien.R;

/**
 * Created by Lorin.
 */
public class PostRequestService extends RequestService {

    @Override
    protected void onHandleIntent(Intent intent) {
        Intent broadcastIntent = new Intent();
        broadcastIntent.setAction(LocalConstants.INTENT_ACTION_PROCESSING);
        sendBroadcast(broadcastIntent);

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

        DoorState doorState;
        if(success) {
            if(intent.getStringExtra(MESSAGE).equals(getResources().getString(R.string.close))){
                doorState = DoorState.CLOSED;
            } else {
                doorState = DoorState.OPEN;
            }
        } else {
            doorState = DoorState.ERROR;
        }

        broadcastIntent = new Intent();
        broadcastIntent.setAction(LocalConstants.INTENT_ACTION_PROCESSED);
        broadcastIntent.putExtra(RESPONSE, s);
        broadcastIntent.putExtra(STATE, doorState.ordinal());
        broadcastIntent.putExtra(SUCCESS, success);
        sendBroadcast(broadcastIntent);
    }
}
