package com.example.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.view.View.OnClickListener;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.widget.ImageView;

public class MainMenu extends AppCompatActivity implements View.OnClickListener {
    private Button Start;
    private Button Setting;
    private Button Rules;
    private ImageView Title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Create activity
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_menu);

        Start = (Button) findViewById(R.id.Start);
        Start.setOnClickListener(this);

        Setting = (Button) findViewById(R.id.Setting);
        Setting.setOnClickListener(this);

        Rules = (Button) findViewById(R.id.Rules);
        Rules.setOnClickListener(this);

        Title=(ImageView) findViewById(R.id.TitleImage);
        Title.setImageResource(R.drawable.image);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) // Gets the id of the button pressed
        {
            case R.id.Start: {
                launchStart();
                break;
            }
            case R.id.Setting: {
                launchSetting();
                break;
            }
            case R.id.Rules: {
                launchRules();
                break;
            }
        }
    }

    private void launchStart()
    {
        Intent gameSelect = new Intent(MainMenu.this, GameSelect.class); // Creates object intent to launch a new activity
        startActivity(gameSelect); // Launches the new activity
        finish();
    }

    private void launchSetting()
    {
        Intent settingSelect = new Intent(MainMenu.this, SettingSelect.class); // Creates object intent to launch a new activity (game two)
        startActivity(settingSelect); // Launches the new activity
        finish();
    }

    private void launchRules() {
        Intent rule = new Intent(MainMenu.this, RuleTexts.class); // Creates object intent to launch a new activity (game two)
        startActivity(rule); // Launches the new activity
        finish();
    }

}
