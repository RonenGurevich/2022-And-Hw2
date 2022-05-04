package com.example.huntinggame;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Random;

import Utility.UtilityMethods;
import classes.GameManager;
import classes.TopTenItem;

public class SensorActivity extends AppCompatActivity{

    GameManager gm;

    private enum directions {
        RIGHT,UP,LEFT,DOWN
    }
    private SensorManager sensorManager;
    private Sensor sensor;

    ImageView[][] gameBoard;
    ImageView[] lives;
    TextView score_TXT;
    LinearLayout grid;

    /**
     * find the relevant views in the app
     */
    void findViews()
    {
        score_TXT = findViewById(R.id.Main_TXT_Score);
        grid = findViewById(R.id.Main_Layout_Grid);

        lives = new ImageView[] {findViewById(R.id.Main_IMG_Heart1),
                findViewById(R.id.Main_IMG_Heart2),
                findViewById(R.id.Main_IMG_Heart3)};
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViews();
        gm = new GameManager(this, gameBoard, lives, score_TXT, grid);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        gm.startGame();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(multipleSensorEventListener, sensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(multipleSensorEventListener);
    }

    private SensorEventListener multipleSensorEventListener = new SensorEventListener() {

        @Override
        public void onSensorChanged(SensorEvent event) {
            /*
                handle Sensor changes
                In this case, an Accelerometer is used. the base position for the phone is z=0,y=0,x=-90
             */
            if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
                float x = event.values[0];
                float y = event.values[1];
                if (Math.abs(x) > Math.abs(y)) {
                    if (x < 0) {
                        gm.setHuntedMovement(directions.RIGHT.ordinal());
                    }
                    if (x > 0) {
                        gm.setHuntedMovement(directions.LEFT.ordinal());
                    }
                } else {
                    if (y < 0) {
                        gm.setHuntedMovement(directions.UP.ordinal());
                    }
                    if (y > 0) {
                        gm.setHuntedMovement(directions.DOWN.ordinal());
                    }
                }
                if (x > (-2) && x < (2) && y > (-2) && y < (2)) { //no tilt
                }
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int i) {
            Log.d("pttt", "onAccuracyChanged");
        }
    };

}

