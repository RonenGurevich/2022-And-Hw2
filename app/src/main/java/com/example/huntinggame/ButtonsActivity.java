package com.example.huntinggame;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import classes.GameManager;

public class ButtonsActivity extends AppCompatActivity {

    GameManager gm;

    private enum directions {
        RIGHT,UP,LEFT,DOWN
    }

    Button leftBTN;
    Button rightBTN;
    Button upBTN;
    Button downBTN;

    ImageView[][] gameBoard;
    ImageView[] lives;
    TextView score_TXT;
    LinearLayout grid;


    /**
     * setup buttons for the app, find their views and set listeners for each
     * assign player's movement direction according to the button clicked:
     * 0 -> right, 1 -> up, 2 -> left, 3-> down
     */
    void setButtons()
    {
        leftBTN = findViewById(R.id.Main_BTN_Left);
        rightBTN = findViewById(R.id.Main_BTN_Right);
        upBTN = findViewById(R.id.Main_BTN_up);
        downBTN = findViewById(R.id.Main_BTN_down);


        rightBTN.setOnClickListener((v) -> gm.setHuntedMovement(directions.RIGHT.ordinal()));
        upBTN.setOnClickListener((v) -> gm.setHuntedMovement(directions.UP.ordinal()));
        leftBTN.setOnClickListener((v) -> gm.setHuntedMovement(directions.LEFT.ordinal()));
        downBTN.setOnClickListener((v) -> gm.setHuntedMovement(directions.DOWN.ordinal()));
    }

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
        setContentView(R.layout.activity_buttons);
        findViews();
        setButtons();
        gm = new GameManager(this, gameBoard, lives, score_TXT, grid);

        gm.startGame();
    }
}