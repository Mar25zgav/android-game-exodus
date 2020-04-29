package com.example.exodus.menupanel;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.exodus.R;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ScoresActivity extends Activity {
    private static final String DEFAULT_DRIVER = "oracle.jdbc.driver.OracleDriver";
    private static final String DEFAULT_URL = "jdbc:oracle:thin:@193.2.139.22:1521:ers";
    private static final String DEFAULT_USERNAME = "markz";
    private static final String DEFAULT_PASSWORD = "markz";
    private Connection connection;
    private TextView tv;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        hideSystemUI();

        setContentView(R.layout.scores_layout);

        getWindow().setLayout((int) (MainActivity.getScreenWidth() * .55), (int) (MainActivity.getScreenHeight() * .85));

        tv = findViewById(R.id.hello);

        try {
            this.connection = createConnection();

            Toast.makeText(ScoresActivity.this, "Connected", Toast.LENGTH_SHORT).show();

            Statement stmt = connection.createStatement();

            StringBuilder stringBuffer = new StringBuilder();

            ResultSet rs = stmt.executeQuery("select username from exodus where id = 1");

            while (rs.next()) {
                stringBuffer.append(rs.getString(1)).append("\n");
            }

            tv.setText(stringBuffer.toString());
            connection.close();
        } catch (Exception e) {
            Toast.makeText(ScoresActivity.this, "" + e,
                    Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    public static Connection createConnection(String driver, String url, String username, String password) throws ClassNotFoundException, SQLException {
        Class.forName(driver);
        return DriverManager.getConnection(url, username, password);
    }

    public static Connection createConnection() throws ClassNotFoundException, SQLException {
        return createConnection(DEFAULT_DRIVER, DEFAULT_URL, DEFAULT_USERNAME, DEFAULT_PASSWORD);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            hideSystemUI();
        }
    }

    private void hideSystemUI() {
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN);
    }
}
