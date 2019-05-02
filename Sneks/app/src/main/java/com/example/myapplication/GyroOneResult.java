package com.example.myapplication;

import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class GyroOneResult extends AppCompatActivity implements View.OnClickListener {
    private Button backtostart; // Declares a button for the start screen
    private Button backtogame; // Declares a button to restart the game
    private TextView scoreScreen; // Text object that says your score

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Creates this activity
        super.onCreate(savedInstanceState);
        setContentView(R.layout.results);

        scoreScreen = (TextView) findViewById(R.id.Score);
        scoreScreen.setText(GyroOne.getScore());
        scoreScreen.setTextColor(Color.WHITE);

        backtostart = (Button) findViewById(R.id.backToMain);
        backtostart.setOnClickListener(this);


        backtogame = (Button) findViewById(R.id.backToGame);
        backtogame.setOnClickListener(this);
    }

    @Override
    public void onClick(View v)
    {
        switch(v.getId()) // Gets the id of the button pressed
        {
            case R.id.backToMain : { // If backToMain button is pressed
                launchGameSelect();
                finish();
                break;
            }
            case R.id.backToGame : { // If backToGame button is pressed
                launchGame();
                finish();
                break;
            }


        }
    }

    private void launchGame()
    {
        Intent newGame = new Intent(GyroOneResult.this, GyroOne.class);
        startActivity(newGame);
        finish();
    }

    private void launchGameSelect()
    {
        Intent backToMenu = new Intent(GyroOneResult.this, GyroMainMenu.class);
        startActivity(backToMenu);
        finish();
    }
}
