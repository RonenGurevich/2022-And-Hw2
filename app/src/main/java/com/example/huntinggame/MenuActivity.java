package com.example.huntinggame;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import java.util.ArrayList;

import Utility.UtilityMethods;
import classes.TopTenItem;

public class MenuActivity extends AppCompatActivity {

    Button start_BTN;
    Button tt_BTN;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        start_BTN = findViewById(R.id.Menu_BTN_start);
        tt_BTN = findViewById(R.id.Menu_BTN_TT);

        Log.d("item", UtilityMethods.loadTopTen(this).toString());

        start_BTN.setOnClickListener(view -> {
            UtilityMethods.switchActivity(this, MainActivity.class);
        });

        tt_BTN.setOnClickListener(view ->  {
            UtilityMethods.switchActivity(this, TopTenActivity.class);
        });
    }

}