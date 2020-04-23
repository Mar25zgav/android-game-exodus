package com.example.exodus.menupanel;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import com.example.exodus.Game;
import com.example.exodus.GameLoop;
import com.example.exodus.R;

import java.util.Objects;

public class GameActivity extends Activity {
    public static int width, height;
    private static Display display;
    private static Point size;
    private Game game;
    private Dialog pauseDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        display = getWindowManager().getDefaultDisplay();
        size = new Point();

        hideSystemUI();

        game = new Game(this);
        setContentView(game);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        pauseDialog = new Dialog(this);

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
        if(GameLoop.isRunning) {
            game.pause();
            showPauseMenu();
        } else {
            game.resume();
        }
    }

    public void showPauseMenu() {
        dialogHideNav();
        pauseDialog.setContentView(R.layout.pause_layout);
        Objects.requireNonNull(pauseDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        pauseDialog.setCanceledOnTouchOutside(false);
        pauseDialog.show();
    }

    public static int getScreenWidth(){
        display.getSize(size);
        return width = size.x;
    }

    public static int getScreenHeight(){
        display.getRealSize(size);
        return height = size.y;
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

    private void dialogHideNav() {
        pauseDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        Objects.requireNonNull(pauseDialog.getWindow()).setFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE, WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);
        pauseDialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        pauseDialog.getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
    }
}
