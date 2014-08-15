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
    public static final String RESPONSE = "RESPONSE";
    public static final String STATE = "STATE";

    public static final String SUCCESS = "SUCCESS";

    public static final String SLOTMACHIEN_URL;
    public static final String AUTHORIZATION;
    public static final String CONTENT_TYPE;
    //TEMPORARY USER_NAME CONSTANT
    public static final String USER_NAME;

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
        USER_NAME = p.getProperty("user_name");
    }
}
