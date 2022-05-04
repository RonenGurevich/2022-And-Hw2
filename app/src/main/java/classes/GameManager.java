package classes;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.location.Location;
import android.media.MediaPlayer;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.huntinggame.MenuActivity;
import com.example.huntinggame.R;

import java.util.ArrayList;
import java.util.Random;

import Utility.UtilityMethods;

public class GameManager {

    private static final int height = 8;
    private static final int width = 5;
    private static int lifeCount = 3;
    private static final int COIN_SCORE_VALUE = 10;
    int score = 0;
    boolean coinExists = false;
    Random rnd = new Random();

    private Context ctx;
    private ImageView[][] gameBoard;
    private ImageView[] lives;
    private TextView score_TXT;
    private LinearLayout grid;

    private int[] hunterPos = new int[2];
    private int[] huntedPos = new int[2];
    private int[] cashPos = new int[2];
    private int huntedMovement = 3; //default is down so it wont move

    ArrayList<TopTenItem> scores = new ArrayList<>();

    public GameManager(Context ctx, ImageView[][] gameBoard, ImageView[] lives, TextView score_TXT, LinearLayout grid) {
        this.ctx = ctx;
        this.gameBoard = gameBoard;
        this.lives = lives;
        this.score_TXT = score_TXT;
        this.grid = grid;
    }

    public void setHuntedMovement(int huntedMovement) {
        this.huntedMovement = huntedMovement;
    }

    /**
     * Initialize the game's grid
     */
    void initGrid()
    {
        gameBoard = new ImageView[height][width];
        for(int i = 0; i < height; i++)
        {
            LinearLayout row = new LinearLayout(ctx);
            row.setOrientation(LinearLayout.HORIZONTAL);
            for (int j = 0; j < width; j++)
            {
                ImageView cell = new ImageView(ctx);
                cell.setLayoutParams(new LinearLayout.LayoutParams(grid.getWidth() / width, grid.getHeight() / height)); //set cell size
                cell.setBackgroundResource(R.drawable.border);
                row.addView(cell);
                gameBoard[i][j] = cell;
            }
            grid.addView(row);
        }
        initPlayers();
    }

    /**
     * set player's starting positions
     */
    void initPlayers()
    {
        score_TXT.setText(String.valueOf(score));
        gameBoard[hunterPos[1]][hunterPos[0]].setImageResource(0);
        gameBoard[huntedPos[1]][huntedPos[0]].setImageResource(0);
        //save hunter's coordinates
        hunterPos[1] = 0;
        hunterPos[0] = width / 2;

        //save hunted post
        huntedPos[1] = height - 1;
        huntedPos[0] = width / 2;

        gameBoard[hunterPos[1]][hunterPos[0]].setImageResource(R.drawable.boss); // set hunter's base location in the middle of top row
        gameBoard[huntedPos[1]][huntedPos[0]].setImageResource(R.drawable.man); // set hunted base location in the middle of bottom row
    }

    /**
     * create a popup that is shown at the end of a game, that lets the user the option to replay or go back to menu
     */
    void createPopUp()
    {
        Dialog popup = new Dialog(ctx);
        popup.setContentView(R.layout.game_over_popup);

        Button retryBTN = popup.findViewById(R.id.Popup_BTN_Retry);
        Button menuBTN = popup.findViewById(R.id.Popup_BTN_Menu);

        retryBTN.setOnClickListener(view -> {
            restartGame();
            popup.dismiss();
        });

        menuBTN.setOnClickListener(view -> {
            popup.dismiss();

            scores.sort((a,b) -> b.getScore() - a.getScore());

            int len = Math.min(scores.size(), 10);
            ArrayList<TopTenItem> subList = new ArrayList<>();
            for (int i = 0; i < len; i++) {
                subList.add(scores.get(i));
            }
            UtilityMethods.saveTopTen(ctx, subList);

            Activity a = (Activity) ctx;
            a.finish();
            UtilityMethods.switchActivity(ctx, MenuActivity.class);
        });

        popup.show();
    }

    /**
     * Start the gmae's clock
     */
    public void startGame() {
        lifeCount = 3;
        scores = UtilityMethods.loadTopTen(ctx);
        grid.post(() -> { //since I need the layout's size, I use the post method
            initGrid();
            startClock();
        });
    }

    /**
     * Restart the game in case the user chose to replay
     */
    void restartGame()
    {
        lifeCount = 3;
        for (ImageView life: lives) {
            life.setVisibility(View.VISIBLE);
        }
        score = 0;
        huntedMovement = 3;
        initPlayers();
        startClock();
    }

    /**
     *  run the game's clock
     */
    void startClock() {
        Handler clock = new Handler();
        clock.postDelayed(new Runnable() {
            @Override
            public void run() {
                tick();
                if(lifeCount > 0)
                    clock.postDelayed(this, 1000);
            }
        },1000);
    }

    /**
     * since 0-3 are used to choose direction, conversion is needed to transform it into an yStep,xStep
     * I map the numbers 0-3 into {1,0,1,0} accordingly using %2 to get axis of movement: 1->y axis, 0 -> x axis
     * then I map it to get {1,-1,-1,1} to decide the movement direction on the axis
     * @param num a number to represent movement direction
     * @return and array consisting of [yStep, xStep]
     */
    int[] numberToDirection(int num)
    {
        int[] movement = {0, 0};
        int sign = num >= 2 ? -1 : 1;
        int axis = (num + 1) % 2;
        movement[axis] = sign * (axis * 2 -1);

        return movement;
    }

    /**
     *
     * @param item the item to move, hunter or hunted
     * @param xStep the amount to move on the x Axis
     * @param yStep the amount to move on the y Axis
     * @param drawable the picture to draw, to move the hunter\hunted on the screen
     */
    void moveItem(int[] item, int xStep, int yStep, int drawable)
    {
        int new_X = item[0] + xStep;
        int new_Y = item[1] + yStep;

        if(new_X >= width ||  new_X < 0 || new_Y >= height || new_Y < 0) //out of bounds
        {
            return;
        }
        gameBoard[item[1]][item[0]].setImageResource(0); //delete prev pic
        item[0] += xStep;
        item[1] += yStep;
        gameBoard[item[1]][item[0]].setImageResource(drawable); //delete prev pic
    }

    /**
     * Method to check whether the cash was picked up by one of the players
     * if the user (hunted) picks it up, gains extra points on pickup
     * if the hunter picks it up, then the coin is gone
     */
    private void checkCashPickUp()
    {
        boolean playerPickUp = huntedPos[0] == cashPos[0] && huntedPos[1] == cashPos[1];
        boolean hunterPickUp = hunterPos[0] == cashPos[0] && hunterPos[1] == cashPos[1];
        if(playerPickUp || hunterPickUp)
        {
            //"zero" cash position values with unreachable location (just in case)
            cashPos[0] = -1;
            cashPos[1] = -1;
            if(playerPickUp) {
                score += COIN_SCORE_VALUE;
                playSound(ctx, R.raw.cash_pickup_sound);
            }
            coinExists = false;
        }
    }

    /**
     * function to handle the cash spawn on the board
     * @param chanceThreshold - a number to define a certain chance for a coin to spawn when one doesn't exist
     */
    private void handleCashSpawn(float chanceThreshold) {
        if(!coinExists)
        {
            cashPos[0] = rnd.nextInt(width);
            cashPos[1] = rnd.nextInt(height);

            float chance = rnd.nextFloat();
            if(chance <= chanceThreshold) { // chance for coin spawn
                gameBoard[cashPos[1]][cashPos[0]].setImageResource(R.drawable.cash);
                coinExists = true;
            }
        }
    }
    /**
     * tick function for the game's clock.
     * 2 game over checks are required, once to check if hunter stepped on hunter, or the opposite
     */
    void tick()
    {
        score++;
        handleCashSpawn(0.5f);
        if(checkGameOver()) // check for game over
        {
            initPlayers();
            return;
        }
        int[] move = numberToDirection(huntedMovement);
        moveItem(huntedPos, move[1], move[0],R.drawable.man);
        if(checkGameOver())// if hunted steps on hunter.
        {
            initPlayers();
            return;
        }
        int dir = rnd.nextInt(4); //0-3
        move = numberToDirection(dir);
        moveItem(hunterPos, move[1], move[0], R.drawable.boss);
        checkCashPickUp();
        score_TXT.setText(String.valueOf(score));
    }


    /**
     * test if game is over and reduce lives each time player is hit
     * @return if the game is over
     */
    boolean checkGameOver()
    {
        boolean hit = hunterPos[0] == huntedPos[0] && hunterPos[1] == huntedPos[1];
        if(hit) {
            playSound(ctx , R.raw.hitsound);
            lifeCount--;
            lives[lifeCount].setVisibility(View.INVISIBLE);
            if(lifeCount == 0)
            {
                try {
                    Location loc = UtilityMethods.getLocation(ctx);
                    scores.add(new TopTenItem(score, loc.getLatitude(),loc.getLongitude()));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                createPopUp();
            }
            return true;
        }
        return false;
    }

    /**
     * play a sound effect
     * @param ctx - context of the game
     * @param soundEffect - an integer that represents the media file to be played
     */
    void playSound(Context ctx, int soundEffect)
    {
        MediaPlayer mp = MediaPlayer.create(ctx, soundEffect);
        mp.start();
    }
}
