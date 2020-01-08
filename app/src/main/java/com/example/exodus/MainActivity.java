package com.example.exodus;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

//class MainActivity je vstopna točka za aplikacijo
public class MainActivity extends Activity {

    public static int width;
    public static int height;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set fullscreen
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
            View.SYSTEM_UI_FLAG_IMMERSIVE
            | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            | View.SYSTEM_UI_FLAG_FULLSCREEN);

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        width = size.x;
        height = size.y;
        System.out.println(width+" "+height);

        //damo content view za Game, da lahko objekti v Game classu rišejo na zaslon
        setContentView(new Game(this));
    }

    @Override
    public void onDestroy() {
        GameLoop.isRunning = false;
        super.onDestroy();
    }

    public static int getScreenWidth() {
        return width;
    }

    public static int getScreenHeight() {
        return height;
    }
}
