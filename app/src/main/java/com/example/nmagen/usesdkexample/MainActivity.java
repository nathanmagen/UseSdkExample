package com.example.nmagen.usesdkexample;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import static android.os.SystemClock.sleep;

public class MainActivity extends AppCompatActivity {
    private static final String LOG_TAG = MainActivity.class.getSimpleName() + "_NatesLog";
    private static final String SIGNED_IN_MSG = "Signed in";
    private static final String ERR_SIGN_IN  = "There was a problem signing in";

    ClientPresenter clientPresenter = new ClientPresenter();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // setting focus on the user name box and requesting to pop up the keyboard, not working
        EditText userNameView = findViewById(R.id.userName);
        InputMethodManager inpMan = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inpMan.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
        userNameView.setFocusableInTouchMode(true);

        // Filling the text boxes if there where saved credentials
        SharedPreferences sharedPreferences = getPreferences(Context.MODE_PRIVATE);

        String userName = sharedPreferences.getString(getString(R.string.user_name), "empty");
        String password = sharedPreferences.getString(getString(R.string.password), "empty");
        String serverId = sharedPreferences.getString(getString(R.string.server_id), "empty");

        if ("empty" != serverId || "empty" != password || "empty" != userName)  {
            userNameView.setText(userName);
            EditText passwordView = findViewById(R.id.password);
            passwordView.setText(password);
            EditText serverIdView = findViewById(R.id.serverId);
            serverIdView.setText(serverId);
        }

        clientPresenter.start(getApplicationContext());
    }

    @Override
    protected void onDestroy() {
        clientPresenter.stop();
        Log.d(LOG_TAG, "App ended");
        super.onDestroy();
    }



    public void onClickSignIn(View view) {
        if (clientPresenter.isSignedIn()) {
            TextView textView = findViewById(R.id.textViewMsg2user);
            textView.setText("Already signed in");
        }
        else {
            EditText userNameView = findViewById(R.id.userName);
            String userName = userNameView.getText().toString();
            EditText passwordView = findViewById(R.id.password);
            String password = passwordView.getText().toString();
            EditText serverIdView = findViewById(R.id.serverId);
            String serverId = serverIdView.getText().toString();

            CheckBox rememberMe = findViewById(R.id.rememberCheckBox);
            if (rememberMe.isChecked()) {
                SharedPreferences sharedPreferences = getPreferences(Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(getString(R.string.user_name),userName);
                editor.putString(getString(R.string.password),password);
                editor.putString(getString(R.string.server_id),serverId);
                editor.commit();
            }

            clientPresenter.signIn(userName, password, serverId);
            sleep(1000);
            if (!clientPresenter.isSignedIn()) {
                putMessage(ERR_SIGN_IN);
            }
            else {
                putMessage(SIGNED_IN_MSG);
            }
        }

    }

    private void putMessage(String msg) {
        TextView textView = findViewById(R.id.textViewMsg2user);
        textView.setText(msg);
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }
}

