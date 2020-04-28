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
import android.widget.Button;
import android.widget.Toast;

import com.example.exodus.Game;
import com.example.exodus.GameLoop;
import com.example.exodus.R;

import java.util.Objects;

public class GameActivity extends Activity implements View.OnClickListener {
    private static Display display;
    private static Point size;
    private Game game;
    private Dialog pauseDialog;
    private Toast backToast;
    private Button btn_resume, btn_restart, btn_mainmenu;
    private long backPressedTime;
    public static int width, height;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        display = getWindowManager().getDefaultDisplay();
        size = new Point();

        hideSystemUI();

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        pauseDialog = new Dialog(this);
        pauseDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        game = new Game(this);
        setContentView(game);
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
        // Pause game if it is running and show pause menu
        if (GameLoop.isRunning) {
            game.pause();
            showPauseMenu();
        } else { // If back button pressed twice in 2 seconds exit app
            if (backPressedTime + 2000 > System.currentTimeMillis()) {
                backToast.cancel();
                pauseDialog.dismiss();
                finish();
                moveTaskToBack(true);
            } else {
                backToast = Toast.makeText(getBaseContext(), "Press back again to exit", Toast.LENGTH_SHORT);
                backToast.show();
            }

            backPressedTime = System.currentTimeMillis();
        }
    }

    public void showPauseMenu() {
        // Hide nav bar
        dialogHideNav();

        pauseDialog.setCanceledOnTouchOutside(false);
        pauseDialog.setContentView(R.layout.pause_layout);
        Objects.requireNonNull(pauseDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        // Set buttons
        btn_resume = pauseDialog.findViewById(R.id.btn_resume);
        btn_restart = pauseDialog.findViewById(R.id.btn_restart);
        btn_mainmenu = pauseDialog.findViewById(R.id.btn_mainmenu);
        btn_resume.setOnClickListener(this);
        btn_restart.setOnClickListener(this);
        btn_mainmenu.setOnClickListener(this);

        pauseDialog.show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_resume:
                pauseDialog.dismiss();
                game.resume();
                break;
            case R.id.btn_restart:
                pauseDialog.dismiss();
                game = new Game(this);
                setContentView(game);
                break;
            case R.id.btn_mainmenu:
                pauseDialog.dismiss();
                finish();
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                break;
        }
    }

    public static int getScreenWidth() {
        display.getSize(size);
        return width = size.x;
    }

    public static int getScreenHeight() {
        display.getRealSize(size);
        return height = size.y;
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

    private void dialogHideNav() {
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

    @Override
    protected void onPause() {
        game.pause();
        finish();
        super.onPause();
    }
}
