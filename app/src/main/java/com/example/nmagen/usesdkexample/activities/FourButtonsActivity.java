package com.example.nmagen.usesdkexample.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.nmagen.usesdkexample.R;

public class FourButtonsActivity extends AppCompatActivity {
    private static final String TEXT_PRELIM = "I'm number ";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_four_buttons);
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
