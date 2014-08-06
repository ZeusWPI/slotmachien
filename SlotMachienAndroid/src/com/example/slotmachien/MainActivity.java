package com.example.slotmachien;

import java.util.concurrent.ExecutionException;

import org.apache.http.HttpResponse;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class MainActivity extends Activity {

    public static final String OPEN_MSG = "open";
    public static final String CLOSE_MSG = "close";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onOpen(View view) throws InterruptedException,
            ExecutionException {
        HttpResponse res = new PostRequestTask().execute(OPEN_MSG).get();
    }

    public void onClose(View view) throws InterruptedException,
            ExecutionException {
        HttpResponse res = new PostRequestTask().execute(CLOSE_MSG).get();
    }

    public void onStatus(View view) throws InterruptedException, ExecutionException {
        HttpResponse res = new GetRequestTask().execute(CLOSE_MSG).get();
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

}
