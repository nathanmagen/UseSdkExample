package com.example.nmagen.usesdkexample.activities;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.MobileTornado.sdk.model.CallCallbacks;
import com.MobileTornado.sdk.model.data.CallInfo;
import com.MobileTornado.sdk.model.data.Contact;
import com.MobileTornado.sdk.model.data.UserState;
import com.example.nmagen.usesdkexample.R;
import com.example.nmagen.usesdkexample.data.AppGroup;
import com.example.nmagen.usesdkexample.presenters.CallPresenter;
import com.example.nmagen.usesdkexample.presenters.PresentersManager;
import com.example.nmagen.usesdkexample.presenters.SOSPresenter;

public class FourButtonsActivity extends AppCompatActivity {
    public static final String GROUP_TAG = "Group Tag";
    public static final int REQUEST_CODE = 1;
    private AppGroup selectedGroup = null;
    private PresentersManager presentersManager = PresentersManager.getInstance();
    private CallPresenter callPresenter = presentersManager.getCallPresenter();
    private CallCallbacks callCallbacks = new CallCallbacks() {
        @Override
        public void onIncomingCall(@NonNull CallInfo callInfo) {

        }

        @Override
        public void onIncomingCallRejected() {

        }

        @Override
        public void onCallConnected(CallInfo callInfo) {
            findViewById(R.id.ptt_button).setEnabled(true);
            findViewById(R.id.call_button).setEnabled(false);
            findViewById(R.id.end_call_button).setEnabled(true);
            Toast.makeText(getApplicationContext(), "On call with " + callInfo.getName(), Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCallModified(CallInfo callInfo) {

        }

        @Override
        public void onCallEnded(CallInfo callInfo, int reason) {
            findViewById(R.id.ptt_button).setEnabled(false);
            findViewById(R.id.end_call_button).setEnabled(false);
            if (selectedGroup != null) {
                findViewById(R.id.call_button).setEnabled(true);
            }
            Toast.makeText(getApplicationContext(), "Call to " + callInfo.getName() + " ended", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCurrentTalkerUpdated(boolean isMe, @Nullable Contact talker) {
            if (!isMe && talker != null) {
                Toast.makeText(getApplicationContext(), talker.getDisplayName() + " is talking", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onAvailableToStartTalking(boolean isAvailable) {

        }

        @Override
        public void onAvailableToEndCall(boolean isAvailable) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_four_buttons);

        presentersManager.getClientPresenter().setState(UserState.ONLINE);
        callPresenter.setCallCallbacks(callCallbacks);

        Button pttButton = findViewById(R.id.ptt_button); // assigning functionality to ptt button for pressing and releasing
        pttButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) { // button is being pressed
                    if (callPresenter.isAbleToStartTalking()) {
                        callPresenter.startTalking();
                        Toast.makeText(getApplicationContext(), "Recording, start talking...", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(getApplicationContext(), "Unable to start talking", Toast.LENGTH_SHORT).show();
                    }
                }
                if (motionEvent.getAction() == MotionEvent.ACTION_UP) { // button is being released
                    Toast.makeText(getApplicationContext(), "Stopped recording", Toast.LENGTH_SHORT).show();
                    callPresenter.stopTalking();
                }
                return false;
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        presentersManager.getClientPresenter().setState(UserState.ONLINE);
    }

    @Override // inflating the menu
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.sos_menu, menu);
        return true;
    }

    @Override // triggered one of the icons is pressed
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == R.id.action_sos) {
            onSOSClick();
        }

        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                int pos = data.getIntExtra(GROUP_TAG, 0);
                selectedGroup = presentersManager.getGroupPresenter().getGroupList().get(pos);
                selectedGroup.setUnSelected(); // so it would be choose-able again in the groups view
                TextView selectedGroupView = findViewById(R.id.selected_group);
                selectedGroupView.setText("Selected group: " + selectedGroup.getGroup().getDisplayName());
                findViewById(R.id.call_button).setEnabled(true);
            }
        }
    }

    public void onCallClick(View view) {
        if (!callPresenter.callGroup(selectedGroup)) {
            Toast.makeText(getApplicationContext(), "Call to " + selectedGroup.getGroup().getDisplayName() + " has failed", Toast.LENGTH_SHORT).show();
        }
    }

    public void onEndCallClick(View view) {
        if (callPresenter.isAbleToEndCall()) {
            callPresenter.endCall(); // Triggers the endCall callback
        }
        else {
            Toast.makeText(this, "Unable to end call", Toast.LENGTH_SHORT).show();
        }
    }

    public void onSOSClick() {
        SOSPresenter sosPresenter = presentersManager.getSosPresenter();
        if (sosPresenter.isAvailable()) {
            sosPresenter.sendRegularSOS();
            Toast.makeText(getApplicationContext(), "Sending SOS", Toast.LENGTH_LONG).show();
        }
        else {
            Toast.makeText(getApplicationContext(), "SOS is unavailable", Toast.LENGTH_LONG).show();
        }
    }

    /*
    public void onClickToast(View view) {
        Button button = (Button) view;
        String buttNum = button.getText().toString();
        Toast.makeText(this, TEXT_PRELIM + buttNum, Toast.LENGTH_LONG).show();
    }
    */

    public void onClickGroups(View view) {
        Intent intent = new Intent(this, GroupListActivity.class);
        startActivityForResult(intent, REQUEST_CODE);
    }

    public void onClickAddGroup(View view) {
        Toast.makeText(this, "Option moved to the group screen menu", Toast.LENGTH_SHORT).show();
    }
}
