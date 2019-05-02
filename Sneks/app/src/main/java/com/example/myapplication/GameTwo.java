package com.example.myapplication;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.example.myapplication.engine.GameEngineMulti;
import com.example.myapplication.enums.GameState;
import com.example.myapplication.views.SnakeView;

import static com.example.myapplication.enums.Direction.Right;
import static com.example.myapplication.enums.Direction.Up;
import static com.example.myapplication.enums.Direction.Down;
import static com.example.myapplication.enums.Direction.Left;

public class GameTwo extends AppCompatActivity implements OnClickListener{

    private Button upOne;
    private Button upTwo;
    private Button rightOne;
    private Button rightTwo;
    private Button downOne;
    private Button downTwo;
    private Button leftOne;
    private Button leftTwo;
    private TextView time;

    private GameEngineMulti gameEngineMulti; // Creates the GameEngineTwo object
    private SnakeView snakeView; // Starts making the game map
    private final Handler handler = new Handler();
    private final long updateDelay = 150; // Determines the speed of the snake

    public static String scoreMessage = ""; // Blank String
    public static String color = "white"; // Declares a string to change the color of the end screen text

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_two);
        //Declares the button needed to pay the game
        upOne= (Button) findViewById(R.id.up); // Creates the button to move up
        upOne.setOnClickListener(this); //On click listen function used
        upTwo= (Button) findViewById(R.id.up2); // Creates the button to move up
        upTwo.setOnClickListener(this); //On click listen function used
        rightOne= (Button) findViewById(R.id.right); // Creates the button to move up
        rightOne.setOnClickListener(this); //On click listen function used
        rightTwo= (Button) findViewById(R.id.right2); // Creates the button to move up
        rightTwo.setOnClickListener(this); //On click listen function used
        downOne= (Button) findViewById(R.id.down); // Creates the button to move up
        downOne.setOnClickListener(this); //On click listen function used
        downTwo= (Button) findViewById(R.id.down2); // Creates the button to move up
        downTwo.setOnClickListener(this); //On click listen function used
        leftOne= (Button) findViewById(R.id.left); // Creates the button to move up
        leftOne.setOnClickListener(this); //On click listen function used
        leftTwo= (Button) findViewById(R.id.left2); // Creates the button to move up
        leftTwo.setOnClickListener(this); //On click listen function used
        time= (TextView) findViewById(R.id.time); // Creates the box for the timer

        final MediaPlayer mediaPlayer =
                MediaPlayer.create(GameTwo.this,R.raw.app_music);
        mediaPlayer.setLooping(true);

        // Starts the game engine
        gameEngineMulti = new GameEngineMulti();
        gameEngineMulti.initGame();
        snakeView = (SnakeView) findViewById(R.id.snakeView);
        CountDownTimer countDownTimer = new CountDownTimer(60000, 1000) {
            public void onTick(long millisUntilFinished) {
                time.setText(String.valueOf(millisUntilFinished/1000));
            }
            public void onFinish() {
                gameEngineMulti.setCurrentGameState(GameState.Over);
            }
        }.start();
        startUpdateHandler();
    }


    private void startUpdateHandler(){
        handler.postDelayed(new Runnable(){
            @Override
            public void run(){
                gameEngineMulti.Update();

                if(gameEngineMulti.getCurrentGameState() == GameState.Running){
                    handler.postDelayed(this, updateDelay );
                }

                if(gameEngineMulti.getCurrentGameState() == GameState.Lost){
                    OnGameLost(1);
                }
                if(gameEngineMulti.getCurrentGameState() == GameState.Lost2){
                    OnGameLost(2);
                }
                if(gameEngineMulti.getCurrentGameState() == GameState.Draw){
                    OnGameLost(0);
                }
                if(gameEngineMulti.getCurrentGameState() == GameState.Over){
                    OnGameLost(-1); // Time runs out
                }
                snakeView.setSnakeViewMap(gameEngineMulti.getMap());
                snakeView.invalidate();

            }
        }, updateDelay);

    }
    private void OnGameLost(int player){ // What happens when you lose
        if(player == 1) { // Player one lost
            scoreMessage = "Blue snake won with " + gameEngineMulti.score2 + " points";
            color = "blue";

        }
        else if(player == 2){ // Player two lost
            scoreMessage = "Red snake won with " + gameEngineMulti.score + " points";
            color = "red";
        }
        else if(player == 0){
            scoreMessage = "It's a draw";
            color = "white";
        }
        else if(player == -1)
        {
            if(gameEngineMulti.score > gameEngineMulti.score2) {
                scoreMessage = "Red snake won with " + gameEngineMulti.score + " points";
                color = "red";
            }
            else if(gameEngineMulti.score2 > gameEngineMulti.score) {
                scoreMessage = "Blue snake won with " + gameEngineMulti.score2 + " points";
                color = "blue";
            }
            else {
                scoreMessage = "It's a draw";
                color = "white";
            }
        }
        Intent results = new Intent(GameTwo.this, GameTwoResult.class);
        startActivity(results);
        finish();

    }

    public static String getScore() // Get's the end of game message for other activities to use
    {
        return scoreMessage;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.up: {
                gameEngineMulti.UpdateDirection(Up);
                break;
            }
            case R.id.right: {
                gameEngineMulti.UpdateDirection(Right);
                break;
            }
            case R.id.down: {
                gameEngineMulti.UpdateDirection(Down);
                break;
            }
            case R.id.left: {
                gameEngineMulti.UpdateDirection(Left);
                break;
            }

            // Directions for the second snake
            case R.id.up2: {
                gameEngineMulti.UpdateDirection2(Up);
                break;
            }
            case R.id.right2: {
                gameEngineMulti.UpdateDirection2(Right);
                break;
            }
            case R.id.down2: {
                gameEngineMulti.UpdateDirection2(Down);
                break;
            }
            case R.id.left2: {
                gameEngineMulti.UpdateDirection2(Left);
                break;
            }
        }
    }
}