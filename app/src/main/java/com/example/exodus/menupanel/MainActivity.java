package com.example.exodus.menupanel;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.exodus.OracleConnection;
import com.example.exodus.R;
import com.example.exodus.SoundPlayer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Objects;

public class MainActivity extends Activity implements View.OnClickListener {
    private Button btn_play, btn_shop, btn_options, btn_scores, btn_help, btn_login, btn_signup;
    private EditText usernameField, passwordField;
    private Connection connection;
    private Dialog loginDialog;
    private Toast backToast;
    static MainActivity instance;
    private static SoundPlayer soundPlayer;
    private static Display display;
    private static Point size;
    private static int userID;
    private long backPressedTime;
    private boolean musicSwitch;
    private boolean logged;

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

        // Initialize login dialog with no title
        loginDialog = new Dialog(this);
        loginDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        // Set activity content to mainmenu_layout
        setContentView(R.layout.mainmenu_layout);

        //Set buttons and listeners
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

        // Try to connect to DB
        connectToDB();
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Show login dialog if user hasn't played yet on specific device
        SharedPreferences sharedPreferences = getSharedPreferences("sharedPrefs", MODE_PRIVATE);
        logged = sharedPreferences.getBoolean("registered", false);
        userID = sharedPreferences.getInt("userID", 0);
        if (!logged || userID == 0) {
            showLoginDialog();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Play music if user enabled it last time
        SharedPreferences sharedPreferences = getSharedPreferences("sharedPrefs", MODE_PRIVATE);
        musicSwitch = sharedPreferences.getBoolean("musicSwitch", true);
        if (musicSwitch) {
            soundPlayer.playMainMenu();
        }
    }

    @Override
    public void onBackPressed() {
        // If player taps back button two times in 2s exit game
        if (backPressedTime + 2000 > System.currentTimeMillis()) {
            backToast.cancel();
            super.onBackPressed();
            return;
        } else {
            // Show toast with text
            backToast = Toast.makeText(getBaseContext(), "Press back again to exit", Toast.LENGTH_SHORT);
            backToast.show();
        }

        // Save time user pressed back button
        backPressedTime = System.currentTimeMillis();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_play:
                // If play button clicked open game activity with animation
                startActivity(new Intent(MainActivity.this, GameActivity.class));
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                break;
            case R.id.btn_shop:
                // Open shop activity on shop button pressed
                startActivity(new Intent(MainActivity.this, ShopActivity.class));
                break;
            case R.id.btn_options:
                // Open option activity on options button pressed
                startActivity(new Intent(MainActivity.this, OptionsActivity.class));
                break;
            case R.id.btn_scores:
                // Open scores activity with leaderboard when scores button pressed
                startActivity(new Intent(MainActivity.this, ScoresActivity.class));
                break;
            case R.id.btn_help:
                // Open help activity when help button pressed
                startActivity(new Intent(MainActivity.this, HelpActivity.class));
                break;
            case R.id.btn_login:
                // Validate user input when button pressed
                validate();
                break;
            case R.id.btn_signup:
                // If username not taken -> sign up
                if (!userExists()) {
                    signupUser();
                    validate();
                } else {
                    Toast.makeText(MainActivity.this, "Username not available!", Toast.LENGTH_SHORT).show();
                }
        }
    }

    @Override
    protected void onStop() {
        // Stop music if playing
        soundPlayer.stopPlaying();
        super.onStop();
    }

    private void showLoginDialog() {
        // Set dialog options
        GameActivity.dialogHideNav(loginDialog);
        loginDialog.setCanceledOnTouchOutside(false);
        loginDialog.setContentView(R.layout.login_layout);
        Objects.requireNonNull(loginDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        loginDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);

        // Set fields and buttons with listeners
        usernameField = loginDialog.findViewById(R.id.usernameField);
        passwordField = loginDialog.findViewById(R.id.passwordField);
        btn_login = loginDialog.findViewById(R.id.btn_login);
        btn_signup = loginDialog.findViewById(R.id.btn_signup);
        btn_login.setOnClickListener(this);
        btn_signup.setOnClickListener(this);

        // Disable mainmenu button -> user cant leave login window
        btn_play.setEnabled(false);
        btn_shop.setEnabled(false);
        btn_options.setEnabled(false);
        btn_scores.setEnabled(false);
        btn_help.setEnabled(false);

        // Show login dialog
        loginDialog.show();
    }

    public void connectToDB() {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        // Try to connect to db
        try {
            this.connection = OracleConnection.createConnection();
            //Toast.makeText(ScoresActivity.this, "Connected", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            //Toast.makeText(ScoresActivity.this, ""+e, Toast.LENGTH_SHORT).show();
            Toast.makeText(MainActivity.this, "No internet connection!", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    private void validate() {
        // Initialize all to null
        Statement stmt = null;
        ResultSet rs = null;
        try {
            // Create statement
            stmt = connection.createStatement();
            // Select query
            rs = stmt.executeQuery("SELECT id, username, password FROM exodus");
            // Going through the result set
            while (rs.next()) {
                // If username and password entered matches any in the DB -> login
                if (rs.getString("username").equals(usernameField.getText().toString()) &&
                        rs.getString("password").equals(passwordField.getText().toString())) {
                    // Save user ID for saving data later
                    userID = rs.getInt("id");
                    // Close login dialog
                    loginDialog.dismiss();
                    // Save that user has logged in on this device
                    saveLogin();
                    // Enable back all buttons
                    btn_play.setEnabled(true);
                    btn_shop.setEnabled(true);
                    btn_options.setEnabled(true);
                    btn_scores.setEnabled(true);
                    btn_help.setEnabled(true);
                }
            }
            // If login details are wrong and login dialog is still shown
            if (loginDialog.isShowing())
                Toast.makeText(MainActivity.this, "Wrong username or password!", Toast.LENGTH_SHORT).show();
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

    private void signupUser() {
        if (!usernameField.getText().toString().equals("") && !passwordField.getText().toString().equals("")) {
            PreparedStatement preparedStatement = null;
            try {
                String strSQL = "INSERT INTO exodus (id, username, password, bestscore, diff) ";
                strSQL += "VALUES (exodus_seq.NEXTVAL,?,?,?,?)";
                preparedStatement = connection.prepareStatement(strSQL);
                connection.setAutoCommit(false);
                preparedStatement.setString(1, usernameField.getText().toString());
                preparedStatement.setString(2, passwordField.getText().toString());
                preparedStatement.setInt(3, 0);
                preparedStatement.setInt(4, 2);
                preparedStatement.executeUpdate();
                connection.commit();
                Toast.makeText(MainActivity.this, "Signed up!", Toast.LENGTH_SHORT).show();
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
        } else {
            Toast.makeText(MainActivity.this, "Enter username and password!", Toast.LENGTH_SHORT).show();
        }
    }

    private void saveLogin() {
        // Save that user is logged in on this device in shared references
        SharedPreferences sharedPreferences = getSharedPreferences("sharedPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("registered", true);
        editor.putInt("userID", userID);
        editor.apply();
    }

    public int getWeaponsNum() {
        // Initialize all to null
        Statement stmt = null;
        ResultSet rs = null;
        connection = getConnection();
        int num = 0;
        try {
            // Create statement
            stmt = connection.createStatement();
            // Select query
            rs = stmt.executeQuery("SELECT weapons FROM exodus WHERE id = " + getUserID());
            // Going through the result set
            while (rs.next()) {
                num = rs.getInt("weapons");
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
        return num;
    }

    private boolean userExists() {
        // Initialize all to null
        Statement stmt = null;
        ResultSet rs = null;
        try {
            // Create statement
            stmt = connection.createStatement();
            // Select query
            rs = stmt.executeQuery("SELECT username FROM exodus");
            // Going through the result set
            while (rs.next()) {
                // If username entered matches any in the DB -> username taken
                if (rs.getString("username").equals(usernameField.getText().toString())) {
                    return true;
                }
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
        return false;
    }

    private void hideSystemUI() {
        // Hide all system UI
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
        // Return calculated screen width
        display.getRealSize(size);
        return size.x;
    }

    public static int getScreenHeight() {
        // Return calculated screen height
        display.getRealSize(size);
        return size.y;
    }

    public static int getUserID() {
        return userID;
    }

    public static SoundPlayer getSoundPlayer() {
        return soundPlayer;
    }

    public Connection getConnection() {
        return connection;
    }

    public static MainActivity getInstance() {
        return instance;
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        // If window gets focus hide all system UI
        if (hasFocus) {
            hideSystemUI();
        }
    }
}
