package be.ugent.zeus.slotmachien.service;

import android.content.Intent;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.params.BasicHttpParams;

import be.ugent.zeus.slotmachien.LocalConstants;
import be.ugent.zeus.slotmachien.MainActivity;

/**
 * Created by Lorin.
 */
public class GetRequestService extends RequestService {
    @Override
    protected void onHandleIntent(Intent intent) {
        HttpGet get = new HttpGet(SLOTMACHIEN_URL);

        get.addHeader("Authorization", AUTHORIZATION);

        HttpResponse res;
        String s = "Undefined";

        get.setParams(new BasicHttpParams().setParameter("user_name", USER_NAME));
        try {
            res = client.execute(get);
            s = res.getStatusLine().getReasonPhrase() + " " + res.getStatusLine().getStatusCode();
        } catch (Exception e) {
            e.printStackTrace();
        }

        Intent broadcastIntent = new Intent();
        broadcastIntent.setAction(LocalConstants.INTENT_ACTION_PROCESSED);
        broadcastIntent.addCategory(Intent.CATEGORY_DEFAULT);
        broadcastIntent.putExtra(RESPONSE, s);
        sendBroadcast(broadcastIntent);
    }
}
