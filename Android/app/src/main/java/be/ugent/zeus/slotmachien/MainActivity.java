package be.ugent.zeus.slotmachien;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import java.util.concurrent.ExecutionException;

import be.ugent.zeus.slotmachien.service.PostRequestService;
import be.ugent.zeus.slotmachien.service.RequestService;

public class MainActivity extends Activity {

    private ProgressBar progress;
    private ResponseReceiver receiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        progress = (ProgressBar) findViewById(R.id.progressBar1);

        IntentFilter filter = new IntentFilter(ResponseReceiver.PROCESSED);
        filter.addCategory(Intent.CATEGORY_DEFAULT);
        receiver = new ResponseReceiver();
        registerReceiver(receiver, filter);
    }

    public void onOpen(View view) throws InterruptedException, ExecutionException {
        postRequest(getResources().getString(R.string.open));
    }

    public void onClose(View view) throws InterruptedException, ExecutionException {
        postRequest(getResources().getString(R.string.close));
    }

    public void postRequest(String msg) {
        Intent intent = new Intent(this, PostRequestService.class);
        intent.putExtra(RequestService.MESSAGE, msg);
        startService(intent);
        startProgressBar();
    }


    public void onStatus(View view) throws InterruptedException, ExecutionException {
        //TODO
    }

    public AlertDialog createErrorDialog(String msg) {
        System.out.println(msg);
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

    public void startProgressBar() {
        progress.setVisibility(View.VISIBLE);
    }

    public void stopProgressBar() {
        progress.setVisibility(View.GONE);
    }

    public class ResponseReceiver extends BroadcastReceiver {
        public static final String PROCESSED = "be.ugent.zeus.slotmachien.PROCESSED";

        @Override
        public void onReceive(Context context, Intent intent) {
            stopProgressBar();
            createErrorDialog(intent.getStringExtra(PostRequestService.RESPONSE)).show();
        }
    }
}


