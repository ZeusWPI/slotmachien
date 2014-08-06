package com.example.slotmachien;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

public class MainActivity extends Activity {

    public static final String SLOTMACHIEN_URL = "raspberrypi/slotmachien/";
    public static final String TOKEN_VALUE = "test";
    public static final String OPEN_MSG = "open";
    public static final String CLOSE_MSG = "close";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
    }

    public void onOpen(View view) {
        try {
            sendPost(OPEN_MSG);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void onClose(View view) {
        try {
            sendPost(CLOSE_MSG);
        } catch (Exception e) {
            e.printStackTrace();
        }        
    }

    /**
     *  Send POST request to open/close door
     * @param msg "open" or "close"
     * @return HTTP Response
     * @throws UnsupportedEncodingException
     */
    public HttpResponse sendPost(String msg)
            throws UnsupportedEncodingException {
        HttpClient hc = new DefaultHttpClient();
        HttpPost post = new HttpPost(SLOTMACHIEN_URL + msg);

        List<NameValuePair> pairs = new ArrayList<>();
        pairs.add(new BasicNameValuePair("token", TOKEN_VALUE));
        post.setEntity(new UrlEncodedFormEntity(pairs));

        HttpResponse res;
        try {
            res = hc.execute(post);
        } catch (Exception e) {
            
            createErrorDialog("Slotmachien niet gevonden, bent u verbonden met het Zeus netwerk?").show();
            return null;
        }
        return res;
    }

    public AlertDialog createErrorDialog(String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this).setMessage(
                msg).setNegativeButton(getString(android.R.string.cancel),
                new OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();

                    }
                });
        return builder.create();
    }

    public boolean isOpen() {
        // TODO
        return false;
    }

}
