package com.example.exodus.menupanel;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import com.example.exodus.R;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import de.codecrafters.tableview.TableView;
import de.codecrafters.tableview.toolkit.SimpleTableDataAdapter;
import de.codecrafters.tableview.toolkit.SimpleTableHeaderAdapter;

public class ScoresActivity extends Activity {
    private String[] tableHeader = {"Position", "Username", "Score", "Difficulty"};
    private String[][] tableData;
    private TableView<String[]> tableView;
    private Connection connection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Hide all system UI
        hideSystemUI();
        // Set activity content to scores layout
        setContentView(R.layout.scores_layout);
        // Set window size
        getWindow().setLayout((int) (MainActivity.getScreenWidth() * .7), (int) (MainActivity.getScreenHeight() * .9));
        // Create table view
        createTable();
        // Fill table view with data
        populateTable();
    }

    private void populateTable() {
        // Initialize all to null
        Statement stmt = null;
        ResultSet rs = null;
        connection = MainActivity.getInstance().getConnection();
        try {
            // Create statement
            stmt = connection.createStatement();
            // Select query
            rs = stmt.executeQuery("SELECT * FROM exodus ORDER BY bestscore desc");
            int i = 0;
            // Going through the result set and filling up table
            while (rs.next() && i < tableData.length) {
                tableData[i][0] = i + 1 + "";
                tableData[i][1] = rs.getString("username");
                tableData[i][2] = rs.getString("bestscore");
                tableData[i][3] = rs.getString("diff");
                i++;
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

    private void createTable() {
        // Table for users
        tableData = new String[5][4];

        // Create and set table header properties
        SimpleTableHeaderAdapter simpleTableHeader = new SimpleTableHeaderAdapter(this, tableHeader);
        simpleTableHeader.setTextSize(16);

        // Create and set table data properties
        SimpleTableDataAdapter simpleTableData = new SimpleTableDataAdapter(this, tableData);
        simpleTableData.setPaddingTop(8);
        simpleTableData.setPaddingBottom(8);
        simpleTableData.setTextSize(15);

        // Create table view and set properties
        tableView = (TableView<String[]>) findViewById(R.id.scoreboardTable);
        tableView.setHeaderBackgroundColor(Color.parseColor("#ffffff"));
        tableView.setHeaderAdapter(simpleTableHeader);
        tableView.setDataAdapter(simpleTableData);
        tableView.setColumnCount(4);
    }

    private void hideSystemUI() {
        // Hide all system UI
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        // If window get focus hide all system UI
        if (hasFocus) {
            hideSystemUI();
        }
    }
}
