package com.example.exodus;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainMenuActivity extends Activity implements View.OnClickListener{
    Button btn_play, btn_options, btn_scores, btn_help;

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

        setContentView(R.layout.activity_mainmenu);

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
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.btn_play:
                startGame();
                break;
            case R.id.btn_options:
                startActivity(new Intent(MainMenuActivity.this, Options.class));
                break;
            case R.id.btn_scores:
                break;
            case R.id.btn_help:
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
