package com.example.exodus.Activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.widget.Button;

import com.example.exodus.GameLoop;
import com.example.exodus.R;

public class MainMenuActivity extends Activity implements View.OnClickListener{
    Button btn_play, btn_options, btn_scores, btn_help;
    public static int width, height;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set fullscreen
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_FULLSCREEN);

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        width = size.x;
        height = size.y;

        setContentView(R.layout.mainmenu_layout);

        //Set buttons
        btn_play = findViewById(R.id.btn_play);
        btn_options = findViewById(R.id.btn_options);
        btn_scores = findViewById(R.id.btn_scores);
        btn_help = findViewById(R.id.btn_help);

        btn_play.setOnClickListener(this);
        btn_options.setOnClickListener(this);
        btn_scores.setOnClickListener(this);
        btn_help.setOnClickListener(this);
    }

    @Override
    public void onClick(View v){
        switch(v.getId()){
            case R.id.btn_play:
                startGame();
                break;
            case R.id.btn_options:
                startActivity(new Intent(MainMenuActivity.this, OptionsActivity.class));
                break;
            case R.id.btn_scores:
                startActivity(new Intent(MainMenuActivity.this, ScoresActivity.class));
                break;
            case R.id.btn_help:
                startActivity(new Intent(MainMenuActivity.this, HelpActivity.class));
                break;
        }
    }

    public void startGame(){
        Intent intent = new Intent(this, GameActivity.class);
        startActivity(intent);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    @Override
    public void onDestroy(){
        GameLoop.isRunning = false;
        super.onDestroy();
    }
}
