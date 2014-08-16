package be.ugent.zeus.slotmachien.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import be.ugent.zeus.slotmachien.R;
import be.ugent.zeus.slotmachien.data.IntentConstants;
import be.ugent.zeus.slotmachien.data.Model;
import be.ugent.zeus.slotmachien.data.State;
import be.ugent.zeus.slotmachien.services.RequestResponse;
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

        Model.setURL(this, "http://kelder.zeus.ugent.be/slotmachien/");

        if(Model.getToken(this).isEmpty() || Model.getUsername(this).isEmpty()) {
            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setTitle("Sign in please.");
            alert.setMessage("SIGN IN!!!");
            LayoutInflater inflater = getLayoutInflater();
            final View view = inflater.inflate(R.layout.dialog, null);
            alert.setView(view);
            alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    EditText username = (EditText) view.findViewById(R.id.username);
                    EditText token = (EditText) view.findViewById(R.id.token);
                    Model.setUsername(MainActivity.this, username.getText().toString());
                    Model.setToken(MainActivity.this, token.getText().toString());
                }
            });
            alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    MainActivity.this.finish();
                }
            });

            alert.show();
        }

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
                String msg = "";
                if(intent.hasExtra(IntentConstants.INTENT_EXTRA_RESPONSE)) {
                    int resp = intent.getIntExtra(IntentConstants.INTENT_EXTRA_RESPONSE, RequestResponse.UNKNOWN_ERROR.ordinal());
                    msg = RequestResponse.values()[resp].toString();
                }

                if(!msg.isEmpty()){
                    changeText(msg, Color.RED);
                } else {
                    changeText("An error occurred.", Color.RED);
                }
                progressBar.setVisibility(View.GONE);
            }
        }
    }
}
