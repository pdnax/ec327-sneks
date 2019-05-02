package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class GyroRuleTexts extends AppCompatActivity implements View.OnClickListener{
    private Button back;
    private ImageView rule;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gyro_rule);

        back = (Button) findViewById(R.id.Back);
        back.setOnClickListener(this);

        rule=(ImageView) findViewById(R.id.rules);
        rule.setImageResource(R.drawable.new_rule);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.Back) {
            Intent gyroMainMenu = new Intent(GyroRuleTexts.this, GyroMainMenu.class);
            startActivity(gyroMainMenu);
            finish();
        }
    }
}
