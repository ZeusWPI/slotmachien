package com.example.slotmachien;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import java.util.concurrent.ExecutionException;

public class MainActivity extends Activity {

    public static final String OPEN_MSG = "open";
    public static final String CLOSE_MSG = "close";

    //TODO implement progressbar
    private ProgressBar progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onOpen(View view) throws InterruptedException, ExecutionException {
        new PostRequestTask().execute(OPEN_MSG);
    }

    public void onClose(View view) throws InterruptedException, ExecutionException {
        new PostRequestTask().execute(CLOSE_MSG);
    }

    public void onStatus(View view) throws InterruptedException, ExecutionException {
        new GetRequestTask().execute(CLOSE_MSG);
    }

    public AlertDialog createErrorDialog(String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this).setMessage(
                msg).setNegativeButton(getString(android.R.string.cancel),
                new OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();

                    }
                }
        );
        return builder.create();
    }

}
