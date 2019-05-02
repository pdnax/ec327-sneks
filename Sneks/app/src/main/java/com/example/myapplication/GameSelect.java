package com.example.myapplication;

import android.content.Intent;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.view.View.OnClickListener;


public class GameSelect extends AppCompatActivity implements OnClickListener {
    private Button one; // Creates a button object for single player game
    private Button two; // Creates a button object for multi player game
    private Button back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Creates this activity (essentially a background rectangle that you can
        //put stuff on)
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_select);

        one = (Button) findViewById(R.id.One); // Creates the button connection with xml for game one
        one.setOnClickListener(this); //On click listen function used
        two = (Button) findViewById(R.id.Two); // Creates the button connection with xml for game two
        two.setOnClickListener(this); //On click listen function used
        back = (Button) findViewById(R.id.Back); // Creates the button connection with xml for game two
        back.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) // Gets the id of the button pressed
        {
            case R.id.One: {
                launchSingle();
                break;
            }
            case R.id.Two: {
                launchMulti();
                break;
            }
            case R.id.Back: {
                launchMain();
                break;
            }
        }
    }

    private void launchSingle()
    {
        Intent gyroOne = new Intent(GameSelect.this, GameOne.class); // Creates object intent to launch a new activity (game one)
        startActivity(gyroOne);
        finish();
    }

    private void launchMulti()
    {
        Intent gameTwo = new Intent(GameSelect.this, GameTwo.class); // Creates object intent to launch a new activity (game two)
        startActivity(gameTwo);
        finish();
    }

    private void launchMain()
    {
        Intent mainMenu = new Intent(GameSelect.this, MainMenu.class); // Creates object intent to launch a new activity (game two)
        startActivity(mainMenu);
        finish();
    }
}