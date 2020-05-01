package com.example.exodus.menupanel;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.exodus.R;
import com.example.exodus.SoundPlayer;

public class MainActivity extends Activity implements View.OnClickListener {
    private Button btn_play, btn_shop, btn_options, btn_scores, btn_help;
    public static int width, height;
    private static SoundPlayer soundPlayer;
    private static Display display;
    private static Point size;
    private long backPressedTime;
    private Toast backToast;
    private boolean musicSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mainmenu_layout);

        soundPlayer = new SoundPlayer(this);

        display = getWindowManager().getDefaultDisplay();
        size = new Point();

        //Set buttons
        btn_play = findViewById(R.id.btn_play);
        btn_shop = findViewById(R.id.btn_shop);
        btn_options = findViewById(R.id.btn_options);
        btn_scores = findViewById(R.id.btn_scores);
        btn_help = findViewById(R.id.btn_help);

        btn_play.setOnClickListener(this);
        btn_shop.setOnClickListener(this);
        btn_options.setOnClickListener(this);
        btn_scores.setOnClickListener(this);
        btn_help.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Play music if enabled
        SharedPreferences sharedPreferences = getSharedPreferences("sharedPrefs", MODE_PRIVATE);
        musicSwitch = sharedPreferences.getBoolean("musicSwitch", true);
        if (musicSwitch) {
            soundPlayer.playMainMenu();
        }
    }

    @Override
    protected void onStop() {
        soundPlayer.stopPlaying();
        super.onStop();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            hideSystemUI();
        }
    }

    @Override
    public void onBackPressed() {
        if (backPressedTime + 2000 > System.currentTimeMillis()) {
            backToast.cancel();
            super.onBackPressed();
            return;
        } else {
            backToast = Toast.makeText(getBaseContext(), "Press back again to exit", Toast.LENGTH_SHORT);
            backToast.show();
        }

        backPressedTime = System.currentTimeMillis();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_play:
                startActivity(new Intent(MainActivity.this, GameActivity.class));
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                break;
            case R.id.btn_shop:
                startActivity(new Intent(MainActivity.this, ShopActivity.class));
                break;
            case R.id.btn_options:
                startActivity(new Intent(MainActivity.this, OptionsActivity.class));
                break;
            case R.id.btn_scores:
                startActivity(new Intent(MainActivity.this, ScoresActivity.class));
                break;
            case R.id.btn_help:
                startActivity(new Intent(MainActivity.this, HelpActivity.class));
                break;
        }
    }

    private void hideSystemUI() {
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
    }

    public static int getScreenWidth() {
        display.getRealSize(size);
        return width = size.x;
    }

    public static int getScreenHeight() {
        display.getRealSize(size);
        return height = size.y;
    }

    public static SoundPlayer getSoundPlayer() {
        return soundPlayer;
    }
}
