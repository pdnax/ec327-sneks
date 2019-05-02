package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;

public class SettingSelect extends AppCompatActivity implements View.OnClickListener{
    private Button back;
    private Button confirm;
    private Switch gameMode;
    private Switch mute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting);

        back = (Button) findViewById(R.id.Back);
        back.setOnClickListener(this);
        confirm = (Button) findViewById(R.id.Confirm);
        confirm.setOnClickListener(this);

        mute = (Switch) findViewById(R.id.mute);
        gameMode = (Switch) findViewById(R.id.GameMode);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) // Gets the id of the button pressed
        {
            case R.id.Back: {
                if (gameMode.isChecked()) {
                    launchGyroMain();
                } else {
                    launchMain();
                }
                break;
            }
            case R.id.Confirm: {
                if (mute.isChecked() && MusicService.running) {
                    stopService(new Intent(this, MusicService.class));
                } else if (!mute.isChecked() && !MusicService.running){
                    startService(new Intent(this, MusicService.class));
                }
                break;
            }
        }
    }

    private void launchGyroMain()
    {
        Intent gyroMainMenu = new Intent(SettingSelect.this, GyroMainMenu.class);
        startActivity(gyroMainMenu);
        finish();
    }

    private void launchMain()
    {
        Intent mainMenu = new Intent(SettingSelect.this, MainMenu.class);
        startActivity(mainMenu);
        finish();
    }

}


