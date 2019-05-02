package com.example.myapplication;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.example.myapplication.engine.GameEngineOne;
import com.example.myapplication.enums.GameState;
import com.example.myapplication.views.SnakeView;

import static com.example.myapplication.enums.Direction.Right;
import static com.example.myapplication.enums.Direction.Up;
import static com.example.myapplication.enums.Direction.Down;
import static com.example.myapplication.enums.Direction.Left;

public class GameOne extends AppCompatActivity implements OnClickListener {

    private Button up;
    private Button down;
    private Button right;
    private Button left;

    private GameEngineOne gameEngine; // Creates the GameEngineTwo object
    private SnakeView snakeView; // Starts making the game map
    private final Handler handler = new Handler();

    public static String scoreMessage = ""; // Blank String
    private TextView scoreCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_one);
        scoreCount = (TextView) findViewById(R.id.gameScore);

        //Declares the button needed to pay the game
        up = (Button) findViewById(R.id.up);
        up.setOnClickListener(this);
        down = (Button) findViewById(R.id.down);
        down.setOnClickListener(this);
        right = (Button) findViewById(R.id.right);
        right.setOnClickListener(this);
        left = (Button) findViewById(R.id.left);
        left.setOnClickListener(this);

        gameEngine = new GameEngineOne();
        gameEngine.initGame();
        snakeView = (SnakeView) findViewById(R.id.snakeView);
        startUpdateHandler();
    }

    private void startUpdateHandler(){
        handler.postDelayed(new Runnable(){
            @Override
            public void run(){
                gameEngine.Update();

                if(gameEngine.getCurrentGameState() == GameState.Running){
                    handler.postDelayed(this, gameEngine.updateDelay );
                }
                if(gameEngine.getCurrentGameState() == GameState.Lost){
                    OnGameLost();
                }

                snakeView.setSnakeViewMap(gameEngine.getMap());
                snakeView.invalidate();

                scoreCount.setText(String.valueOf(gameEngine.score));
            }
        }, gameEngine.updateDelay);
    }

    private void OnGameLost() { // Game ends
        Intent result = new Intent(GameOne.this, GameOneResult.class);
        scoreMessage = "you scored " + gameEngine.score + " points";
        startActivity(result);
        finish();
    }

    public static String getScore()
    {
        return scoreMessage;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) // Gets the id of the button pressed
        {
            case R.id.up: {
                gameEngine.UpdateDirection(Up);
                break;
            }
            case R.id.left: {
                gameEngine.UpdateDirection(Left);
                break;
            }
            case R.id.right: {
                gameEngine.UpdateDirection(Right);
                break;
            }
            case R.id.down: {
                gameEngine.UpdateDirection(Down);
                break;
            }
        }
    }
}
