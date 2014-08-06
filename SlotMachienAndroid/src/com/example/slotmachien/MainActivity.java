package com.example.slotmachien;

import java.io.IOException;
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

import android.support.v7.app.ActionBarActivity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ToggleButton;

public class MainActivity extends ActionBarActivity {

    public static final String SLOTMACHIEN_URL = "raspberrypi/slotmachien/";
    public static final String TOKEN_VALUE = "test";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void onToggleClick(View view) {
        String msg = ((ToggleButton) view).isChecked() ? "close" : "open";

        try {
            HttpResponse res = sendPost(msg);
        } catch (IOException e) {
            // TODO Auto-generated catch block
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
