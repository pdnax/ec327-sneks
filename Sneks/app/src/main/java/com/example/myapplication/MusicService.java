package com.example.myapplication;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.media.MediaPlayer;

public class MusicService extends Service {

    private MediaPlayer player;
    public static boolean running;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        running = true;
        //getting systems default ringtone
        player = MediaPlayer.create(this, R.raw.app_music);
        //setting loop play to true
        player.setLooping(true);

        //staring the player
        player.start();

        //we have some options for service
        //start sticky means service will be explicity started and stopped
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        running = false;
        super.onDestroy();
        //stopping the player when service is destroyed
        player.stop();
    }
}
