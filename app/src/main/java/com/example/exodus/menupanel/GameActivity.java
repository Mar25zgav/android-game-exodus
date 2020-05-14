package com.example.exodus.menupanel;

import android.app.Activity;
import android.app.Dialog;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
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
import com.example.exodus.SoundPlayer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Objects;

public class GameActivity extends Activity implements View.OnClickListener {
    private Button btn_resume, btn_restart, btn_mainmenu, btn_exit, btn_playagain;
    private Dialog pauseDialog, gameoverDialog;
    private TextView scoreLabel, bestScoreLabel;
    private SoundPlayer soundPlayer;
    private Connection connection;
    private Toast backToast;
    private Game game;
    static GameActivity instance;
    private static Display display;
    private static Point size;
    private long backPressedTime;
    private int best_score;
    private boolean musicOn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        instance = this;

        // Initialize sound player
        soundPlayer = new SoundPlayer(this);

        // Calculate display size
        display = getWindowManager().getDefaultDisplay();
        size = new Point();

        // Hide system ui and keep screen on
        hideSystemUI();
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        // Initialize pause and game over dialog with no title
        pauseDialog = new Dialog(this);
        gameoverDialog = new Dialog(this);
        pauseDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        gameoverDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        // Create a new game and set content view to it
        game = new Game(this);
        setContentView(game);

        // Load best score
        loadData();
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Play music if music switch is on
        SharedPreferences sharedPreferences = getSharedPreferences("sharedPrefs", MODE_PRIVATE);
        musicOn = sharedPreferences.getBoolean("musicSwitch", true);
        if (musicOn)
            soundPlayer.playGame();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_resume:
                // Resume game, play game music and close pause dialog
                if (musicOn)
                    soundPlayer.playGame();
                pauseDialog.dismiss();
                game.resume();
                break;
            case R.id.btn_restart:
                // Restart game music, close pause dialog and start a new game
                if (musicOn) {
                    soundPlayer.resetMusic();
                    soundPlayer.playGame();
                }
                pauseDialog.dismiss();
                game = new Game(this);
                setContentView(game);
                break;
            case R.id.btn_mainmenu:
                // Close pause dialog, finish and go to previous activity
                pauseDialog.dismiss();
                finish();
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                break;
            case R.id.btn_exit:
                // Stop game over music and close game over dialog, finish activity and go to previous
                if (musicOn) {
                    soundPlayer.stopPlaying();
                }
                gameoverDialog.dismiss();
                finish();
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                break;
            case R.id.btn_playagain:
                // Reset game music, close game over dialog and start a new game
                soundPlayer.playGame();
                gameoverDialog.dismiss();
                game = new Game(this);
                setContentView(game);
                break;
        }
    }

    @Override
    public void onBackPressed() {
        // Pause game if it is running and show pause menu
        if (GameLoop.isRunning) {
            game.pause();
            showPauseMenu();
        } else {
            // If back button pressed twice in 2 seconds exit app
            if (backPressedTime + 2000 > System.currentTimeMillis()) {
                backToast.cancel();
                pauseDialog.dismiss();
                finish();
                moveTaskToBack(true);
            } else {
                backToast = Toast.makeText(getBaseContext(), "Press back again to exit", Toast.LENGTH_SHORT);
                backToast.show();
            }
            // Save time when back button pressed
            backPressedTime = System.currentTimeMillis();
        }
    }

    @Override
    protected void onPause() {
        // Pause game and finish activity
        game.pause();
        finish();
        super.onPause();
    }

    public void showPauseMenu() {
        soundPlayer.pauseMusic();

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
            // Play music
            if (musicOn) {
                soundPlayer.stopPlaying();
                soundPlayer.playGameOver();
            }

            // Hide nav bar
            dialogHideNav(gameoverDialog);

            // Set game over dialog
            gameoverDialog.setContentView(R.layout.gameover_layout);
            gameoverDialog.setCanceledOnTouchOutside(false);
            Objects.requireNonNull(gameoverDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

            // Set buttons in game over dialog and listeners
            btn_exit = gameoverDialog.findViewById(R.id.btn_exit);
            btn_playagain = gameoverDialog.findViewById(R.id.btn_playagain);
            scoreLabel = gameoverDialog.findViewById(R.id.score_label);
            bestScoreLabel = gameoverDialog.findViewById(R.id.best_score_label);
            btn_exit.setOnClickListener(instance);
            btn_playagain.setOnClickListener(instance);

            // Save score if higher than best
            if (Game.SCORE > best_score) {
                saveData();
            }

            // Update score and best score in game over dialog
            updateViews();

            // Show game over dialog
            gameoverDialog.show();
        });
    }

    private void saveData() {
        PreparedStatement preparedStatement = null;
        try {
            String strSQL = "UPDATE exodus SET bestscore = " + Game.SCORE;
            strSQL += ", diff = '" + getDifficulty() + "'";
            if (Game.SCORE >= 1000) {
                strSQL += ", weapons = " + getWeaponsUnlock();
            }
            strSQL += " WHERE id = " + MainActivity.getUserID();
            preparedStatement = connection.prepareStatement(strSQL);
            connection.setAutoCommit(false);
            preparedStatement.executeUpdate();
            connection.commit();
        } catch (SQLException e) {
            e.printStackTrace();
            Log.d(null, "SQL error: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            Log.d(null, "SQL error: " + e.getMessage());
        } finally {
            try {
                preparedStatement.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void loadData() {
        // Initialize statement result set and connection from main activity
        Statement stmt = null;
        ResultSet rs = null;
        connection = MainActivity.getInstance().getConnection();
        try {
            // Create statement
            stmt = connection.createStatement();
            // Select query
            rs = stmt.executeQuery("SELECT bestscore FROM exodus WHERE id = " + MainActivity.getUserID());
            // Going through the result set
            while (rs.next()) {
                // Get best score from current user
                best_score = rs.getInt("bestscore");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) { /* ignored */}
            }
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) { /* ignored */}
            }
        }
    }

    private void updateViews() {
        // Set score labels
        bestScoreLabel.setText(best_score + "");
        scoreLabel.setText(Game.SCORE + "");
    }

    public int getWeaponsUnlock() {
        int weapons = 1;
        if (Game.SCORE >= 1000)
            weapons = 2;
        if (Game.SCORE >= 2000)
            weapons = 3;
        if (Game.SCORE >= 4000)
            weapons = 4;
        if (Game.SCORE >= 8000)
            weapons = 5;
        if (Game.SCORE >= 10000)
            weapons = 6;
        return weapons;
    }

    public String getDifficulty() {
        SharedPreferences sharedPreferences = getSharedPreferences("sharedPrefs", MODE_PRIVATE);
        int diff = sharedPreferences.getInt("diffSpinner", 1);
        switch (diff) {
            case 0:
                return "easy";
            case 1:
                return "medium";
            case 2:
                return "hard";
        }
        return "medium";
    }

    public static void dialogHideNav(Dialog d) {
        // Hide all system UI when dialog open
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

    public static int getScreenWidth() {
        // Return calculated screen width
        display.getSize(size);
        return size.x;
    }

    public static int getScreenHeight() {
        // Return calculated screen height
        display.getRealSize(size);
        return size.y;
    }

    public static GameActivity getInstance() {
        return instance;
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

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        // If focus is on this window hide all system UI
        if (hasFocus) {
            hideSystemUI();
        }
    }
}
