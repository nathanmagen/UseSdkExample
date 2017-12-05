package com.example.nmagen.usesdkexample.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.MobileTornado.sdk.model.data.UserState;
import com.example.nmagen.usesdkexample.R;
import com.example.nmagen.usesdkexample.presenters.PresentersManager;

public class FourButtonsActivity extends AppCompatActivity {
    private static final String TEXT_PRELIM = "I'm number ";
    private PresentersManager presentersManager = PresentersManager.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_four_buttons);

        // disable user from getting calls in this activity
        presentersManager.getClientPresenter().setState(UserState.DND);
    }

    @Override
    protected void onStart() {
        super.onStart();
        presentersManager.getClientPresenter().setState(UserState.DND);
    }

    @Override
    protected void onResume() {
        super.onResume();
        presentersManager.getClientPresenter().setState(UserState.DND);
    }

    public void onClickToast(View view) {
        Button button = (Button) view;
        String buttNum = button.getText().toString();
        Toast.makeText(this, TEXT_PRELIM + buttNum, Toast.LENGTH_LONG).show();
    }

    public void onClickGroups(View view) {
        Intent intent = new Intent(this, GroupListActivity.class);
        startActivity(intent);
    }
}
