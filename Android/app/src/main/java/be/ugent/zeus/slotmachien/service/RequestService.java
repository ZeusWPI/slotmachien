package be.ugent.zeus.slotmachien.service;

import android.app.IntentService;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import java.util.Properties;

/**
 * Created by Lorin.
 */
public abstract class RequestService extends IntentService {

    public static final String MESSAGE = "msg";
    static final String SLOTMACHIEN_URL;
    static final String AUTHORIZATION;
    static final String CONTENT_TYPE;
    private static final int CONNECTION_TIMEOUT = 3000;
    private static final int SOCKET_TIMEOUT = 5000;
    final HttpClient client;

    public RequestService() {
        super("RequestService");

        HttpParams params = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(params, CONNECTION_TIMEOUT);
        HttpConnectionParams.setSoTimeout(params, SOCKET_TIMEOUT);

        this.client = new DefaultHttpClient(params);
    }

    // TODO Fix this horrible shit
    static {
        Properties p = new Properties();
        try {

            p.load(RequestService.class.getClassLoader().getResourceAsStream("assets/slotmachien.properties"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        SLOTMACHIEN_URL = p.getProperty("slotmachien_url");
        AUTHORIZATION = p.getProperty("authorization");
        CONTENT_TYPE = p.getProperty("content_type");
    }
}
