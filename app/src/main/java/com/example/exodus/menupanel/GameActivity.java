package com.example.exodus.menupanel;

import android.app.Activity;
import android.app.Dialog;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.exodus.Game;
import com.example.exodus.GameLoop;
import com.example.exodus.R;

import java.util.Objects;

public class GameActivity extends Activity implements View.OnClickListener {
    private Button btn_resume, btn_restart, btn_mainmenu, btn_exit, btn_playagain;
    private Dialog pauseDialog, gameoverDialog;
    private static Display display;
    private static Point size;
    static GameActivity instance;
    private Toast backToast;
    private TextView scoreLabel, bestScoreLabel;
    private Game game;
    private long backPressedTime;
    public static int width, height;
    private String SHARED_PREFS = "sharedPrefs";
    private String BEST_SCORE = "bestScore";
    private int best_score;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        instance = this;

        display = getWindowManager().getDefaultDisplay();
        size = new Point();

        hideSystemUI();

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        pauseDialog = new Dialog(this);
        gameoverDialog = new Dialog(this);
        pauseDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        gameoverDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

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
        dialogHideNav(pauseDialog);

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

    public void showGameOver() {
        this.runOnUiThread(() -> {
            // Hide nav bar
            dialogHideNav(gameoverDialog);

            gameoverDialog.setContentView(R.layout.gameover_layout);
            gameoverDialog.setCanceledOnTouchOutside(false);
            Objects.requireNonNull(gameoverDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

            btn_exit = gameoverDialog.findViewById(R.id.btn_exit);
            btn_playagain = gameoverDialog.findViewById(R.id.btn_playagain);
            scoreLabel = gameoverDialog.findViewById(R.id.score_label);
            bestScoreLabel = gameoverDialog.findViewById(R.id.best_score_label);
            btn_exit.setOnClickListener(instance);
            btn_playagain.setOnClickListener(instance);

            loadData();
            // Save score if higher than best
            if (Game.SCORE > best_score) {
                saveData();
            }
            updateViews();

            gameoverDialog.show();
        });
    }

    private void saveData() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(BEST_SCORE, Game.SCORE);
        editor.apply();
    }

    private void loadData() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        // Get best score
        best_score = sharedPreferences.getInt(BEST_SCORE, 0);
    }

    private void updateViews() {
        // Set score labels
        bestScoreLabel.setText(best_score + "");
        scoreLabel.setText(Game.SCORE + "");
    }

    public static GameActivity getInstance() {
        return instance;
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
            case R.id.btn_exit:
                gameoverDialog.dismiss();
                finish();
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                break;
            case R.id.btn_playagain:
                gameoverDialog.dismiss();
                game = new Game(this);
                setContentView(game);
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

    private static void dialogHideNav(Dialog d) {
        Objects.requireNonNull(d.getWindow()).setFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE, WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);
        d.getWindow().addFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        d.getWindow().getDecorView().setSystemUiVisibility(
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
