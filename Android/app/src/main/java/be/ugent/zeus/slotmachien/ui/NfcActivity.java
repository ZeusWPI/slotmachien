package be.ugent.zeus.slotmachien.ui;

import android.app.Activity;
import android.content.Intent;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.widget.Toast;

import be.ugent.zeus.slotmachien.data.IntentConstants;
import be.ugent.zeus.slotmachien.data.Model;
import be.ugent.zeus.slotmachien.data.State;
import be.ugent.zeus.slotmachien.services.RequestService;
import be.ugent.zeus.slotmachien.services.RequestType;

/**
 * Created by wouter on 16/08/14.
 */
public class NfcActivity extends Activity {

    // TODO - fill in read ID from purchased NFC Tags
    public static byte[] TAG_ID = new byte[]{};

    @Override
    public void onResume() {
        super.onResume();
        if (NfcAdapter.getDefaultAdapter(this) != null
                && getIntent() != null
                && getIntent().getAction().equals(NfcAdapter.ACTION_NDEF_DISCOVERED)) {

            Tag tag = getIntent().getParcelableExtra(NfcAdapter.EXTRA_TAG);

            if (tag != null && tag.getId().equals(TAG_ID)) {
                Intent intent = new Intent(this, RequestService.class);
                State state = State.values()[Model.getState(this)];
                switch (state){
                    case UNKNOWN:
                    case OPEN:
                        intent.putExtra(IntentConstants.INTENT_EXTRA_REQUEST, RequestType.CLOSE.ordinal());
                        Toast.makeText(this, "Door is closing...", Toast.LENGTH_SHORT).show();
                        break;
                    case CLOSED:
                        intent.putExtra(IntentConstants.INTENT_EXTRA_REQUEST, RequestType.OPEN.ordinal());
                        Toast.makeText(this, "Door is opening...", Toast.LENGTH_SHORT).show();
                        break;
                }
                startService(intent);
            }
        }
        finish();
    }
}
