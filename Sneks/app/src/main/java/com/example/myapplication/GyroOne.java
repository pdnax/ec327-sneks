package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.example.myapplication.engine.GameEngine;
import com.example.myapplication.enums.GameState;
import com.example.myapplication.views.SnakeView;

import static com.example.myapplication.enums.Direction.Right;
import static com.example.myapplication.enums.Direction.Up;
import static com.example.myapplication.enums.Direction.Down;
import static com.example.myapplication.enums.Direction.Left;

public class GyroOne extends AppCompatActivity implements View.OnTouchListener, SensorEventListener{
    private GameEngine gameEngine;
    private SnakeView snakeView;
    private final Handler handler = new Handler();

    private SensorManager mSensorManager;
    private Sensor mSensorAccelerometer;
    private Sensor mSensorMagnetometer;
    private float[] mAccelerometerData = new float[3];
    private float[] mMagnetometerData = new float[3];
    private int index;
    private static final float VALUE_DRIFT = 0.15f;
    private static final float VALUE_TILT = 0f;
    private Display mDisplay;
    public static String scoreMessage = ""; // Blank String
    private TextView scoreCount; // Text object that says your score

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gyro_one);
        scoreCount = (TextView) findViewById(R.id.gameScore);

        mSensorManager = (SensorManager) getSystemService(
                Context.SENSOR_SERVICE);
        mSensorAccelerometer = mSensorManager.getDefaultSensor(
                Sensor.TYPE_ACCELEROMETER);
        mSensorMagnetometer = mSensorManager.getDefaultSensor(
                Sensor.TYPE_MAGNETIC_FIELD);
        if (mSensorAccelerometer != null) {
            mSensorManager.registerListener(this, mSensorAccelerometer, 0);
        }
        if (mSensorMagnetometer != null) {
            mSensorManager.registerListener(this, mSensorMagnetometer, 0);
        }

        gameEngine = new GameEngine();
        gameEngine.initGame();
        snakeView = (SnakeView) findViewById(R.id.snakeView);
        startUpdateHandler();
    }

    @Override
    protected void onResume(){
        super.onResume();
    }
    @Override
    protected void onPause(){
        super.onPause();
    }
    private void startUpdateHandler(){
        handler.postDelayed(new Runnable(){
            @Override
            public void run(){
                gameEngine.Update();

                if(gameEngine.getCurrentGameState() == GameState.Running){
                    handler.postDelayed(this, gameEngine.updateDelay );
                    if(index==0){
                        gameEngine.UpdateDirection(Up);
                    }
                    if(index==1){
                        gameEngine.UpdateDirection(Right);
                    }
                    if(index==2){
                        gameEngine.UpdateDirection(Down);
                    }
                    if(index==3){
                        gameEngine.UpdateDirection(Left);
                    }
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
    private void OnGameLost(){ // What happens when you lose
        Intent result = new Intent(GyroOne.this, GyroOneResult.class);
        scoreMessage = "you scored " + String.valueOf(gameEngine.score) + " points";
        mSensorManager.unregisterListener(this, mSensorAccelerometer);
        mSensorManager.unregisterListener(this, mSensorMagnetometer);
        startActivity(result); // Launches the new activity
        finish();
    }

    public static String getScore()
    {
        return scoreMessage;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) { // What occurs when the screen is touch
        return true;
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        int sensorType = sensorEvent.sensor.getType();
        // The sensorEvent object is reused across calls to onSensorChanged().
        // clone() gets a copy so the data doesn't change out from under us
        switch (sensorType) {
            case Sensor.TYPE_ACCELEROMETER:
                mAccelerometerData = sensorEvent.values.clone();
                break;
            case Sensor.TYPE_MAGNETIC_FIELD:
                mMagnetometerData = sensorEvent.values.clone();
                break;
            default:
                return;
        }
        // Compute the rotation matrix: merges and translates the data
        // from the accelerometer and magnetometer, in the device coordinate
        // system, into a matrix in the world's coordinate system.
        //
        // The second argument is an inclination matrix, which isn't
        // used in this example.
        float[] rotationMatrix = new float[9];
        boolean rotationOK = SensorManager.getRotationMatrix(rotationMatrix,
                null, mAccelerometerData, mMagnetometerData);
        mDisplay = ((WindowManager) getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        // Get the orientation of the device (pitch, roll) based
        // on the rotation matrix. Output units are radians.
        float[] rotationMatrixAdjusted = new float[9];
        switch (mDisplay.getRotation()) {
            case Surface.ROTATION_0:
                rotationMatrixAdjusted = rotationMatrix.clone();
                break;
            case Surface.ROTATION_90:
                SensorManager.remapCoordinateSystem(rotationMatrix,
                        SensorManager.AXIS_Y, SensorManager.AXIS_MINUS_X,
                        rotationMatrixAdjusted);
                break;
            case Surface.ROTATION_180:
                SensorManager.remapCoordinateSystem(rotationMatrix,
                        SensorManager.AXIS_MINUS_X, SensorManager.AXIS_MINUS_Y,
                        rotationMatrixAdjusted);
                break;
            case Surface.ROTATION_270:
                SensorManager.remapCoordinateSystem(rotationMatrix,
                        SensorManager.AXIS_MINUS_Y, SensorManager.AXIS_X,
                        rotationMatrixAdjusted);
                break;
        }
        float orientationValues[] = new float[3];
        if (rotationOK) {
            SensorManager.getOrientation(rotationMatrixAdjusted,
                    orientationValues);
        }


        // Pull out the individual values from the array.
        float azimuth = orientationValues[0];
        float pitch = orientationValues[1];
        float roll = orientationValues[2];
        // Pitch and roll values that are close to but not 0 cause the
        // animation to flash a lot. Adjust pitch and roll to 0 for very
        // small values (as defined by VALUE_DRIFT).
        if (Math.abs(pitch) < VALUE_DRIFT) {
            pitch = 0;
        }
        if (Math.abs(roll) < VALUE_DRIFT) {
            roll = 0;
        }

        if (gameEngine != null) {
            //Decides what direction to take depending on tilt orientations
            if (roll > 0 && Math.abs(roll) > Math.abs(pitch)) {
                //East
                index = 1;
            } else if (roll < 0 && Math.abs(roll) > Math.abs(pitch)) {
                //West
                index = 3;
            } else if (pitch > 0 && Math.abs(roll) < Math.abs(pitch)) {
                //North
                index = 0;
            } else if (pitch < 0 && Math.abs(roll) < Math.abs(pitch)) {
                //South
                index = 2;
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

}
