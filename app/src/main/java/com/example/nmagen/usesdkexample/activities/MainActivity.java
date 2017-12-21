package com.example.nmagen.usesdkexample.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import com.example.nmagen.usesdkexample.R;
import com.example.nmagen.usesdkexample.presenters.ClientPresenter;
import com.example.nmagen.usesdkexample.presenters.PresentersManager;

import static android.os.SystemClock.sleep;


public class MainActivity extends AppCompatActivity {
    public static final String LOG_TAG = MainActivity.class.getSimpleName() + "_NatesLog";
    private static final String SIGNED_IN_MSG = "Signed in";
    private static final String ALREADY_SIGNED_IN_MSG = "Already signed in";
    private static final String ERR_SIGN_IN  = "There was a problem signing in";
    private int attempts = 0;

    PresentersManager presentersManager = PresentersManager.getInstance();
    ClientPresenter clientPresenter = presentersManager.getClientPresenter();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Setting focus on the user name box and requesting to pop up the keyboard
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
    protected void onResume() {
        super.onResume();
        if (clientPresenter.isSignedIn()) {
            clientPresenter.signOut();
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        clientPresenter.stop();
        Log.d(LOG_TAG, "App ended");
        super.onDestroy();
    }

    public void onClickSignIn(View view) {
        final ProgressBar progressBar = findViewById(R.id.progressBarSignIn);

        if (clientPresenter.isSignedIn()) {
            putMessage(ALREADY_SIGNED_IN_MSG);
            presentersManager.initModules();
            Intent intent = new Intent(this, FourButtonsActivity.class);
            startActivity(intent);
            return;
        }

        EditText userNameView = findViewById(R.id.userName);
        final String userName = userNameView.getText().toString();
        EditText passwordView = findViewById(R.id.password);
        final String password = passwordView.getText().toString();
        EditText serverIdView = findViewById(R.id.serverId);
        final String serverId = serverIdView.getText().toString();

        SharedPreferences sharedPreferences = getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(getString(R.string.user_name),userName);
        editor.putString(getString(R.string.password),password);
        editor.putString(getString(R.string.server_id),serverId);
        editor.commit();

        clientPresenter.signIn(userName, password, serverId);
        new CountDownTimer(1000, 200) {
            @Override
            public void onFinish() {
                progressBar.setVisibility(View.INVISIBLE);
                attempts = 0;
                while(true) {
                    if (clientPresenter.isSignedIn()) {
                        putMessage(SIGNED_IN_MSG);
                        startFourButtons();
                        break;
                    }
                    else {
                        attempts++;
                        if (attempts == 20) {
                            putMessage(ERR_SIGN_IN);
                            break;
                        }
                    }
                    sleep(500);
                }
            }

            @Override
            public void onTick(long millsTillFin) {
                progressBar.setVisibility(View.VISIBLE);
            }
        }.start();
    }

    private void putMessage(String msg) {
        TextView textView = findViewById(R.id.textViewMsg2user);
        textView.setText(msg);
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    private void startFourButtons() {
        presentersManager.initModules();
        Intent intent = new Intent(this, FourButtonsActivity.class);
        startActivity(intent);
    }
}

