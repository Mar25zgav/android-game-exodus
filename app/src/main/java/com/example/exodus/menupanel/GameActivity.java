package com.example.exodus.menupanel;

import android.app.Activity;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;

import com.example.exodus.Game;
import com.example.exodus.GameLoop;

public class GameActivity extends Activity {
    public static int width, height;
    private static Display display;
    private static Point size;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        display = getWindowManager().getDefaultDisplay();
        size = new Point();
        hideSystemUI();
        setContentView(new Game(this));
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            hideSystemUI();
        }
    }

    private void hideSystemUI(){
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        GameLoop.isRunning = false;
    }

    public static int getScreenWidth(){
        display.getSize(size);
        return width = size.x;
    }

    public static int getScreenHeight(){
        display.getRealSize(size);
        return height = size.y;
    }
}
