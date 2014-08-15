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
import android.widget.TextView;

import be.ugent.zeus.slotmachien.service.GetRequestService;
import be.ugent.zeus.slotmachien.service.PostRequestService;
import be.ugent.zeus.slotmachien.service.RequestService;

public class MainActivity extends Activity {

    private ResponseReceiver receiver;
    private IntentFilter filter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        filter = new IntentFilter(LocalConstants.INTENT_ACTION_PROCESSED);
        filter.addCategory(Intent.CATEGORY_DEFAULT);
        receiver = new ResponseReceiver();
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(receiver, filter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(receiver);
    }

    public void onOpen(View view) {
        postRequest(getResources().getString(R.string.open));
    }

    public void onClose(View view) {
        postRequest(getResources().getString(R.string.close));
    }

    private void postRequest(String msg) {
        Intent intent = new Intent(this, PostRequestService.class);
        intent.putExtra(RequestService.MESSAGE, msg);
        startService(intent);
        startProgressBar();
    }

    private void getRequest(String msg) {
        Intent intent = new Intent(this, GetRequestService.class);
        intent.putExtra(RequestService.MESSAGE, msg);
        startService(intent);
        startProgressBar();
    }

    public void onStatus(View view) {
        getRequest("status");
    }

    private AlertDialog createErrorDialog(String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setMessage(msg)
                .setNegativeButton(getString(android.R.string.cancel),
                        new OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        }
                );

        return builder.create();
    }

    private void changeText(String s, RequestStatus requestStatus) {
        TextView t = (TextView) findViewById(R.id.statusText);
        t.setText(s);

        t.setTextColor(requestStatus.getColor());
    }

    private ProgressBar getRequestProgressBar() {
        return (ProgressBar) findViewById(R.id.requestProgressBar);
    }

    private void startProgressBar() {
        changeText("Processing...", RequestStatus.PROCESSING);
        getRequestProgressBar().setVisibility(View.VISIBLE);
    }

    private void stopProgressBar() {
        getRequestProgressBar().setVisibility(View.GONE);
    }

    public class ResponseReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            stopProgressBar();
            String res = intent.getStringExtra(PostRequestService.RESPONSE);
            boolean b = intent.getBooleanExtra(RequestService.SUCCESS, false);
            changeText(res, b ? RequestStatus.OK : RequestStatus.ERROR);
        }
    }
}
