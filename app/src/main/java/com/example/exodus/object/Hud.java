package com.example.exodus.object;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

import androidx.core.content.ContextCompat;

import com.example.exodus.MainActivity;
import com.example.exodus.R;
import com.example.exodus.Timer;

public class Hud{
    private Paint greenPaint, redPaint, time;
    private Rect greenBar, redBar;
    private int health, timeColor;
    private int offset = Arena.wallSize;
    public static Timer timer;
    private int screenHeight, screenWidth, wallSize;
    private Paint.FontMetrics timerSize;

    public Hud(Context context){
        // Healthbar
        this.health = Player.health;
        greenPaint = new Paint();
        greenPaint.setColor(ContextCompat.getColor(context, R.color.health));
        redPaint = new Paint();
        redPaint.setColor(ContextCompat.getColor(context, R.color.enemy));

        // Timer
        time = new Paint();
        timeColor = ContextCompat.getColor(context, R.color.time);
        time.setColor(timeColor);
        time.setTextSize(75);
        timerSize = new Paint.FontMetrics();
        timer = new Timer();
        timer.start();

        //Set vars
        screenHeight = MainActivity.getScreenHeight();
        screenWidth = MainActivity.getScreenWidth();
        wallSize = (int)(screenWidth*0.03);
    }

    public void draw(Canvas canvas){
        // Healthbar
        greenBar = new Rect(offset,offset/2-15, 500+offset,offset/2+15);
        redBar = new Rect(health+offset, offset/2-15, 500+offset, offset/2+15);
        canvas.drawRect(greenBar, greenPaint);
        canvas.drawRect(redBar, redPaint);

        // Timer
        canvas.drawText(timer.toString(), screenWidth/2-(time.getFontMetrics(timerSize)/2), 150, time);
    }

    public void update(){
        health = Player.health;
    }
}
