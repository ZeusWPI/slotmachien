package be.ugent.zeus.slotmachien.services;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import be.ugent.zeus.slotmachien.data.IntentConstants;
import be.ugent.zeus.slotmachien.data.Model;
import be.ugent.zeus.slotmachien.data.State;

/**
 * Created by Lorin.
 */
public class RequestService extends IntentService {

    private static final int CONNECTION_TIMEOUT = 3000;
    private static final int SOCKET_TIMEOUT = 5000;

    protected final HttpClient client;

    public RequestService() {
        super("RequestService");

        HttpParams params = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(params, CONNECTION_TIMEOUT);
        HttpConnectionParams.setSoTimeout(params, SOCKET_TIMEOUT);

        this.client = new DefaultHttpClient(params);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (!intent.hasExtra(IntentConstants.INTENT_EXTRA_REQUEST)) {
            Intent broadcastIntent = new Intent();
            broadcastIntent.setAction(IntentConstants.INTENT_ACTION_PROCESSING_ERROR);
            sendBroadcast(broadcastIntent);
            return;
        }

        Intent broadcastIntent = new Intent();
        broadcastIntent.setAction(IntentConstants.INTENT_ACTION_PROCESSING);
        sendBroadcast(broadcastIntent);

        HttpPost post = new HttpPost(Model.getURL(this));
        post.addHeader("Content-Type", "application/json");
        post.addHeader("Authorization", Model.getToken(this));

        HttpResponse res;
        JSONObject json = new JSONObject();

        RequestResponse response = null;
        try {
            int requestValue = intent.getIntExtra(IntentConstants.INTENT_EXTRA_REQUEST, State.UNKNOWN.ordinal());
            RequestType requestType = RequestType.values()[requestValue];

            json.put("action", requestType.getAction());
            json.put("user_name", Model.getUsername(this));
            post.setEntity(new StringEntity(json.toString()));
            res = client.execute(post);

            String jsonString = EntityUtils.toString(res.getEntity());
            String result = new JSONObject(jsonString).getString("status");

            switch (res.getStatusLine().getStatusCode()) {
                case 200:
                    response = RequestResponse.OK;
                    if (result.equals(State.CLOSED.getText())) {
                        Model.setState(this, State.CLOSED.ordinal());
                    } else if (result.equals(State.OPEN.getText())) {
                        Model.setState(this, State.OPEN.ordinal());
                    } else {
                        Model.setState(this, State.UNKNOWN.ordinal());
                    }
                    break;
                case 400:
                    response = RequestResponse.BAD_REQUEST;
                    break;
                case 401:
                    response = RequestResponse.UNAUTHORIZED;
                    break;
                case 500:
                    response = RequestResponse.INTERNAL_SERVER_ERROR;
                    break;
                default:
                    response = RequestResponse.UNKNOWN_ERROR;
                    break;
            }
        } catch (Exception e) {
            broadcastIntent = new Intent();
            broadcastIntent.setAction(IntentConstants.INTENT_ACTION_PROCESSING_ERROR);
            sendBroadcast(broadcastIntent);
            return;
        }

        broadcastIntent = new Intent();
        broadcastIntent.setAction(IntentConstants.INTENT_ACTION_PROCESSED);
        broadcastIntent.putExtra(IntentConstants.INTENT_EXTRA_RESPONSE, response.ordinal());
        sendBroadcast(broadcastIntent);
    }
}
