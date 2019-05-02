package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class RuleTexts extends AppCompatActivity implements View.OnClickListener {
    private Button back;
    private ImageView image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rule);

        back = (Button) findViewById(R.id.Back);
        back.setOnClickListener(this);

        image=(ImageView) findViewById(R.id.image);
        image.setImageResource(R.drawable.new_rule);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.Back) {
            Intent mainMenu = new Intent(RuleTexts.this, MainMenu.class);
            startActivity(mainMenu);
            finish();
        }
    }
}
