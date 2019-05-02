package com.example.myapplication;

import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class GameTwoResult extends AppCompatActivity implements View.OnClickListener {

    private Button backtostart; // Declares a button for the start screen
    private Button backtogame; // Declares a button to restart the game
    private TextView scoreScreen; // Text object that says your score

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.results);

        scoreScreen = (TextView) findViewById(R.id.Score);
        scoreScreen.setText(GameTwo.getScore());
        if(GameTwo.color == "white")
            scoreScreen.setTextColor(Color.WHITE);
        else if(GameTwo.color == "blue")
            scoreScreen.setTextColor((Color.BLUE));
        else if(GameTwo.color == "red")
            scoreScreen.setTextColor(Color.RED);

        backtostart = (Button) findViewById(R.id.backToMain);
        backtostart.setOnClickListener(this);


        backtogame = (Button) findViewById(R.id.backToGame);
        backtogame.setOnClickListener(this);
    }

    @Override
    public void onClick(View v)
    {
        switch(v.getId())
        {
            case R.id.backToMain : {
                launchMain();
                break;
            }
            case R.id.backToGame : {
                launchGame();
                break;
            }
        }
    }

    private void launchGame()
    {
        Intent twoActivity = new Intent(GameTwoResult.this, GameTwo.class);
        startActivity(twoActivity);
        finish();
    }

    private void launchMain()
    {
        Intent introActivity = new Intent(GameTwoResult.this, GameSelect.class);
        startActivity(introActivity);
        finish();
    }
}
