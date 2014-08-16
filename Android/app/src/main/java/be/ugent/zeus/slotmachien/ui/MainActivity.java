package be.ugent.zeus.slotmachien.ui;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import be.ugent.zeus.slotmachien.R;
import be.ugent.zeus.slotmachien.data.IntentConstants;
import be.ugent.zeus.slotmachien.data.Model;
import be.ugent.zeus.slotmachien.data.State;
import be.ugent.zeus.slotmachien.services.RequestService;
import be.ugent.zeus.slotmachien.services.RequestType;

public class MainActivity extends Activity {

    private ResponseReceiver receiver;
    private ProgressBar progressBar;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // TODO - Show a dialog requesting the user to fill in this information
        // TODO - The user should be able to change these settings later
        Model.setToken(this, "..............token..........");
        Model.setURL(this, "http://kelder.zeus.ugent.be/slotmachien/");
        Model.setUsername(this, "...............username........");

        receiver = new ResponseReceiver();
        progressBar = (ProgressBar) findViewById(R.id.requestProgressBar);
        textView = (TextView) findViewById(R.id.statusText);

        State state = State.values()[Model.getState(this)];
        changeText("Current state: " + state.getText(), state.getColor());

        if (state == State.UNKNOWN) {
            sendRequest(RequestType.STATUS);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter filter = new IntentFilter();
        filter.addAction(IntentConstants.INTENT_ACTION_PROCESSED);
        filter.addAction(IntentConstants.INTENT_ACTION_PROCESSING);
        filter.addAction(IntentConstants.INTENT_ACTION_PROCESSING_ERROR);
        registerReceiver(receiver, filter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(receiver);
    }

    public void onOpen(View view) {
        sendRequest(RequestType.OPEN);
    }

    public void onClose(View view) {
        sendRequest(RequestType.CLOSE);
    }

    public void onStatus(View view) {
        sendRequest(RequestType.STATUS);
    }

    private void sendRequest(RequestType type) {
        Intent intent = new Intent(this, RequestService.class);
        intent.putExtra(IntentConstants.INTENT_EXTRA_REQUEST, type.ordinal());
        startService(intent);
    }

    private void changeText(String text, int color) {
        textView.setText(text);
        textView.setTextColor(color);
    }

    public class ResponseReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(IntentConstants.INTENT_ACTION_PROCESSED)) {
                progressBar.setVisibility(View.GONE);
                State state = State.values()[Model.getState(MainActivity.this)];
                changeText("Current state: " + state.getText(), state.getColor());
            } else if (intent.getAction().equals(IntentConstants.INTENT_ACTION_PROCESSING)) {
                changeText("Processing...", Color.BLUE);
                progressBar.setVisibility(View.VISIBLE);
            } else if (intent.getAction().equals(IntentConstants.INTENT_ACTION_PROCESSING_ERROR)) {
                changeText("An error occurred.", Color.RED);
                progressBar.setVisibility(View.GONE);
            }
        }
    }
}
