package com.example.nmagen.usesdkexample;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class FourButtonsActivity extends AppCompatActivity {
    private final String TEXT_PRELIM = "I'm number ";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_four_buttons);
    }

    public void OnClickToast(View view) {
        Button button = (Button) view;
        String buttNum = button.getText().toString();
        Toast.makeText(this, TEXT_PRELIM + buttNum, Toast.LENGTH_LONG).show();
    }
}
