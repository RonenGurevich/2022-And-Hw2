package com.example.huntinggame;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;

import Utility.UtilityMethods;

public class MenuActivity extends AppCompatActivity {

    Button start_BTN;
    Button tt_BTN;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        start_BTN = findViewById(R.id.Menu_BTN_start);
        tt_BTN = findViewById(R.id.Menu_BTN_TT);

        start_BTN.setOnClickListener(view -> {
            UtilityMethods.switchActivity(this, SensorActivity.class);
        });

        tt_BTN.setOnClickListener(view ->  {
            UtilityMethods.switchActivity(this, TopTenActivity.class);
        });
    }

}