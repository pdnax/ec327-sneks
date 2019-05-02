package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

public class SplashScreen extends AppCompatActivity{
    private ImageView Title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);

        Title=(ImageView) findViewById(R.id.TitleImage);
        Title.setImageResource(R.drawable.image);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                startService(new Intent(SplashScreen.this, MusicService.class));
                Intent main = new Intent(SplashScreen.this, MainMenu.class);
                startActivity(main); // Launches MainMenu
                finish();
            }
        }, 2000);   //2 second
    }
}
