package com.example.nmagen.usesdkexample.activities;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.MobileTornado.sdk.model.CallCallbacks;
import com.MobileTornado.sdk.model.data.CallInfo;
import com.MobileTornado.sdk.model.data.Contact;
import com.MobileTornado.sdk.model.data.UserState;
import com.example.nmagen.usesdkexample.R;
import com.example.nmagen.usesdkexample.adapters.ListToViewAdapter;
import com.example.nmagen.usesdkexample.data.AppGroup;
import com.example.nmagen.usesdkexample.listeners.RecyclerTouchListener;
import com.example.nmagen.usesdkexample.presenters.CallPresenter;
import com.example.nmagen.usesdkexample.presenters.GroupPresenter;
import com.example.nmagen.usesdkexample.presenters.PresentersManager;
import com.example.nmagen.usesdkexample.presenters.SOSPresenter;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.function.Predicate;

public class FourButtonsActivity extends AppCompatActivity {
    public static final String GROUP_TAG = "Group Tag";
    public static final String REMOVE_GROUP_TAG = "Remove group tag";
    public static final int REQUEST_CODE = 1;
    public static final int RESULT_REMOVE_GROUP = -0xd0d1;
    public int clickedCallOptionPosition = -1;
    private final String NO_GROUPS_CHOSEN = "No groups chosen";
    private AppGroup selectedGroup = null;
    private List<AppGroup> callOptionsList = new ArrayList<>();
    private List<String> callOptionsNameList = new ArrayList<>();
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
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

        setCallOptionsNameList();
        setRecyclerView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        presentersManager.getClientPresenter().setState(UserState.ONLINE);
        // findViewById(R.id.call_button).setEnabled(false);
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
            if (resultCode == Activity.RESULT_OK) { // Group was selected
                int pos = data.getIntExtra(GROUP_TAG, 0);
                selectedGroup = presentersManager.getGroupPresenter().getGroupList().get(pos);
                selectedGroup.setUnSelected(); // so it would be choose-able again in the groups view
                if (!isGroupFound(callOptionsList, selectedGroup.getGroup().getDisplayName())) {
                    callOptionsList.add(selectedGroup);
                    setCallOptionsNameList();
                    findViewById(R.id.call_button).setEnabled(true);
                    adapter.notifyDataSetChanged();
                }
                else {
                    Toast.makeText(this, "Group already chosen", Toast.LENGTH_SHORT).show();
                }
            }
            else if (resultCode == RESULT_REMOVE_GROUP) { // Group was removed
                String group2RemoveName = data.getStringExtra(REMOVE_GROUP_TAG);
                removeGroupsWithName(group2RemoveName);
                setCallOptionsNameList();
                adapter.notifyDataSetChanged();
            }
        }
    }

    public void onCallClick(View view) {
        GroupPresenter groupPresenter = presentersManager.getGroupPresenter();
        if (groupPresenter.isGroupEmpty(selectedGroup)) {
            Toast.makeText(this, "No members in group", Toast.LENGTH_SHORT).show();
            return;
        }
        if (groupPresenter.isGroupAvailable(selectedGroup)) {
            if (!callPresenter.callGroup(selectedGroup)) {
                Toast.makeText(getApplicationContext(), "Call to " + selectedGroup.getGroup().getDisplayName() + " has failed", Toast.LENGTH_SHORT).show();
            }
        }
        else {
            Toast.makeText(this, "The group is not available for call", Toast.LENGTH_SHORT).show();
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

    private void setCallOptionsNameList() {
        int size = callOptionsList.size();
        callOptionsNameList.clear();
        if (size == 0) {
            callOptionsNameList.add(NO_GROUPS_CHOSEN);
        }
        else {
            for (int i = 0; i < size; i++) {
                callOptionsNameList.add(callOptionsList.get(i).getGroup().getDisplayName());
            }
        }
    }

    private void setRecyclerView() {
        recyclerView = findViewById(R.id.call_options_recyclerView);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        adapter = new ListToViewAdapter(this, callOptionsNameList, R.layout.call_option_list_line, R.id.call_option_name, ListToViewAdapter.NO_BUTTON);
        recyclerView.setAdapter(adapter);
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                clickedCallOptionPosition = position;
                if (callOptionsList.size() == 0) {
                    return;
                }

                Button callButt = findViewById(R.id.call_button);
                if (!callButt.isEnabled()) {
                    callButt.setEnabled(true);
                }

                AppGroup clickedGroup = callOptionsList.get(position);
                if (!clickedGroup.isSelected()) {
                    clickedGroup.setSelected();
                    // view.findViewById(R.id.select_button).setEnabled(true);
                    view.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                    selectedGroup = clickedGroup;
                }

                // unselecting all the rest of the groups in the list
                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                int firstVisiblePos = linearLayoutManager.findFirstVisibleItemPosition();
                int itemCount = linearLayoutManager.findLastVisibleItemPosition() - firstVisiblePos + 1;
                int newPos = position - firstVisiblePos;
                for (int i = 0; i < itemCount; i++) {
                    if (newPos != i) {
                        callOptionsList.get(i + firstVisiblePos).setUnSelected();
                        View v = recyclerView.getChildAt(i);
                        v.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                        // v.findViewById(R.id.select_button).setEnabled(false);
                    }
                }

            }

            @Override
            public void onLongClick(View view, int position) {
                if (!callOptionsList.isEmpty()) {
                    ImageButton trashButt = view.findViewById(R.id.image_button_delete);
                    trashButt.setVisibility(View.VISIBLE);
                    trashButt.setTag(position);
                    /*
                    callOptionsList.remove(position);
                    setCallOptionsNameList();
                    adapter.notifyDataSetChanged();
                    findViewById(R.id.call_button).setEnabled(false);
                    */
                }
            }
        }));
    }

    public void onClickDelete(View view) {
        int pos = (int)view.getTag();
        callOptionsList.remove(pos);
        setCallOptionsNameList();
        adapter.notifyDataSetChanged();
        findViewById(R.id.call_button).setEnabled(false);
        view.setVisibility(View.INVISIBLE);
    }

    public void unSelectCallOption(int pos) {
        callOptionsList.get(pos).setUnSelected();
    }

    public boolean isNoCallOptions() {
        return callOptionsList.isEmpty();
    }

    private void removeGroupsWithName(final String group2RemoveName) {
        int size = callOptionsList.size();
        for (int i = 0; i < size; i++) {
            if (callOptionsList.get(i).getGroup().getDisplayName().matches(group2RemoveName)) {
                callOptionsList.remove(i);
                return;
            }
        }
    }

    private boolean isGroupFound(List<AppGroup> list, String groupName) {
        int size = list.size();
        for (int i = 0; i < size; i++) {
            if (list.get(i).getGroup().getDisplayName().matches(groupName)) {
                return true;
            }
        }
        return false;
    }
}
