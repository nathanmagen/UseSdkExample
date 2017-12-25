package com.example.nmagen.usesdkexample.activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.MobileTornado.sdk.model.CallCallbacks;
import com.MobileTornado.sdk.model.MessagingModule;
import com.MobileTornado.sdk.model.data.CallInfo;
import com.MobileTornado.sdk.model.data.Contact;
import com.MobileTornado.sdk.model.data.Conversation;
import com.MobileTornado.sdk.model.data.Message;
import com.example.nmagen.usesdkexample.R;
import com.example.nmagen.usesdkexample.data.AppGroup;
import com.example.nmagen.usesdkexample.presenters.CallPresenter;
import com.example.nmagen.usesdkexample.presenters.GroupPresenter;
import com.example.nmagen.usesdkexample.presenters.MessagePresenter;
import com.example.nmagen.usesdkexample.presenters.PresentersManager;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class MessagingActivity extends AppCompatActivity {
    public static final String VIEW_STRING_KEY = "com.example.nmagen.usesdkexample.activities.MessagingActivity";
    public static final String LAST_CHAT_FILE_NAME = "last_chat";
    private PresentersManager presentersManager = PresentersManager.getInstance();
    private MessagePresenter messagePresenter = presentersManager.getMessagePresenter();
    private GroupPresenter groupPresenter = presentersManager.getGroupPresenter();
    private CallPresenter callPresenter = presentersManager.getCallPresenter();
    private final String MY_NAME = presentersManager.getClientPresenter().getName();
    private EditText curMsgBox;
    private TextView msgDisplayView;
    private MessagingModule.NewMessageListener newMessageListener = new MessagingModule.NewMessageListener() {
        @Override
        public void onNewMessage(Conversation conversation, Message message) {
            if (presentersManager.getClientPresenter().getId() != message.getSenderId()) {
                String dispMsg = message.getSenderName() + ": " + message.getText() + " " + getTime();
                if (messagePresenter.isCurrentConversation(conversation)) {
                    msgDisplayView.append(dispMsg);
                }
                else {
                    appendColorString(dispMsg, R.color.RoyalBlue);
                }
                msgDisplayView.append("\n");
                // messagePresenter.markMessageAsRead(message);
            }
        }
    };

    private CallCallbacks callCallbacks = new CallCallbacks() {
        @Override
        public void onIncomingCall(@NonNull CallInfo callInfo) {

        }

        @Override
        public void onIncomingCallRejected() {

        }

        @Override
        public void onCallConnected(CallInfo callInfo) {
            callPresenter.removeCallCallbacks(callCallbacks);
            finish();
        }

        @Override
        public void onCallModified(CallInfo callInfo) {

        }

        @Override
        public void onCallEnded(CallInfo callInfo, int reason) {

        }

        @Override
        public void onCurrentTalkerUpdated(boolean isMe, @Nullable Contact talker) {

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
        setContentView(R.layout.activity_messaging);

        // Setting the text from the last session in to the view screen
        curMsgBox = findViewById(R.id.msgBox);
        msgDisplayView = findViewById(R.id.msgDisplay);
        SharedPreferences sharedPreferences = getSharedPreferences(LAST_CHAT_FILE_NAME, Context.MODE_PRIVATE);
        String lastViewText = sharedPreferences.getString(VIEW_STRING_KEY, "");
        msgDisplayView.setText(lastViewText);

        msgDisplayView.setMovementMethod(new ScrollingMovementMethod()); // Making the messaging view scrollable

        messagePresenter.addNewMessageListener(newMessageListener);
        callPresenter.setCallCallbacks(callCallbacks);

        String msgGroupName = getIntent().getStringExtra(FourButtonsActivity.MSG_GROUP_NAME_KEY);
        ((TextView) findViewById(R.id.messaeGroupNameTextView)).setText(msgGroupName); // Setting the group name to view
        AppGroup selectedGroup = groupPresenter.getGroupByName(msgGroupName); // Getting the group by it's name

        if (groupPresenter.isGroupAvailable(selectedGroup)) {
            messagePresenter.startConversation(selectedGroup);
        }
        else {
            Toast.makeText(this, "The group is not available for messaging", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Saving the call session
        SharedPreferences sharedPreferences = getSharedPreferences(LAST_CHAT_FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(VIEW_STRING_KEY, msgDisplayView.getText().toString());
        editor.apply();

        // messagePresenter.removeNewMessagesListener(newMessageListener);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        messagePresenter.removeNewMessagesListener(newMessageListener);
    }

    @Override // inflating the menu
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.messaging_menu, menu);
        return true;
    }

    @Override // triggered one of the icons is pressed
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == R.id.clear_all_item) {
            clearAll();
        }

        return true;
    }

    private void clearAll() {
        hideSoftKeyboard();
        setRegularScreenVisibility(View.INVISIBLE);
        setClearQuestionVisibility(View.VISIBLE);
    }

    public void onSendClick(View view) {
        String sndMsg = curMsgBox.getText().toString();
        if (messagePresenter.sendMessage(sndMsg)) {
            String dispMsg = MY_NAME + ": " + sndMsg + " " + getTime();
            appendColorString(dispMsg, R.color.Crimson);
            msgDisplayView.append("\n");
            curMsgBox.setText("");
        }
        else {
            Toast.makeText(this, "Could not send the message", Toast.LENGTH_SHORT).show();
        }

        hideSoftKeyboard();
    }

    public void onYesClearClick(View view) {
        msgDisplayView.setText("");
        setClearQuestionVisibility(View.INVISIBLE);
        setRegularScreenVisibility(View.VISIBLE);
    }

    public void onNoClearClick(View view) {
        setClearQuestionVisibility(View.INVISIBLE);
        setRegularScreenVisibility(View.VISIBLE);
    }

    // Can be used by other activities
    public static String getTime() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("(HH:mm)", new Locale("English"));
        return simpleDateFormat.format(Calendar.getInstance().getTime());
    }

    private void appendColorString(String str, int color) {
        str = "<font color='" + getResources().getColor(color) + "'>" + str + "</font>";
        msgDisplayView.append(Html.fromHtml(str));
    }

    private void setClearQuestionVisibility(int mode) {
        findViewById(R.id.clearAllTextView).setVisibility(mode);
        findViewById(R.id.yesClearButton).setVisibility(mode);
        findViewById(R.id.noClearButton).setVisibility(mode);
    }

    private void setRegularScreenVisibility(int mode) {
        msgDisplayView.setVisibility(mode);
        curMsgBox.setVisibility(mode);
        findViewById(R.id.sendButton).setVisibility(mode);
    }

    private void hideSoftKeyboard(){
        View focused = this.getCurrentFocus();
        if (focused != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(focused.getWindowToken(), 0);
        }
    }
}
