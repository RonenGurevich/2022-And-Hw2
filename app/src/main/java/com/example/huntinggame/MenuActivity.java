package com.example.huntinggame;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;

import Utility.UtilityMethods;

public class MenuActivity extends AppCompatActivity {

    Button start_Sensors_BTN;
    Button start_Buttons_BTN;
    Button tt_BTN;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        start_Sensors_BTN = findViewById(R.id.Menu_BTN_Sensors);
        start_Buttons_BTN = findViewById(R.id.Menu_BTN_Buttons);
        tt_BTN = findViewById(R.id.Menu_BTN_TT);

        start_Sensors_BTN.setOnClickListener(view -> {
            UtilityMethods.switchActivity(this, SensorActivity.class);
        });

        start_Buttons_BTN.setOnClickListener(view -> {
            UtilityMethods.switchActivity(this, ButtonsActivity.class);
        });

        tt_BTN.setOnClickListener(view ->  {
            UtilityMethods.switchActivity(this, TopTenActivity.class);
        });
    }

}