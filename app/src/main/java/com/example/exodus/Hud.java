package com.example.exodus;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;

import androidx.core.content.ContextCompat;

import com.example.exodus.Activities.GameActivity;
import com.example.exodus.Activities.MainActivity;

import java.util.ArrayList;

public class Hud{
    private Paint time;
    private int timeColor;
    public static Timer timer;
    private int screenWidth = GameActivity.getScreenWidth();
    private int wallSize;
    private Paint.FontMetrics timerSize;
    private Typeface font;

    private Drawable life, emptyLife;
    private int left, top, right, bottom, lifeWidth;
    private ArrayList<Rect> livesPos = new ArrayList<>();
    private float timerHeight, timerWidth;

    public Hud(Context context){
        // Timer
        font = Typeface.createFromAsset(context.getAssets(),"fonts/titan_one.ttf");
        timeColor = ContextCompat.getColor(context, R.color.time);
        time = new Paint();
        time.setColor(timeColor);
        timeColor = context.getResources().getDimensionPixelSize(R.dimen.timerFontSize);
        time.setTextSize(timeColor);
        time.setTypeface(font);
        time.setTextAlign(Paint.Align.CENTER);
        time.setAntiAlias(true);
        time.setSubpixelText(true);
        timerSize = new Paint.FontMetrics();
        timer = new Timer();
        timer.start();

        //Set vars
        wallSize = Arena.wallSize;
        timerSize = time.getFontMetrics();
        timerHeight = Math.abs(timerSize.ascent);
        timerWidth = time.getFontMetrics(timerSize);
        top = (int)(wallSize * 1.5);
        bottom = top * 2;
        right = screenWidth / 2 - (int)timerWidth * 2;
        left = right - (bottom - top);

        System.out.println(timerHeight + " "+timerWidth);
        System.out.println((int)timerHeight + " "+(int)timerWidth);

        // Healthbar
        life = context.getResources().getDrawable(R.drawable.life_filled);
        emptyLife = context.getResources().getDrawable(R.drawable.life_empty);

        for(int i = 0; i < 5; i++){
            livesPos.add(new Rect(left,top,right,bottom));
            right -= (bottom - top);
            left -= (bottom - top);
        }
    }

    public void draw(Canvas canvas){
        // Healthbar
        int i=0;
        while(i < 5){
            if(i<Player.health){
                life.setBounds(livesPos.get(i));
                life.draw(canvas);
            }else{
                emptyLife.setBounds(livesPos.get(i));
                emptyLife.draw(canvas);
            }
            i++;
        }

        // Timer
        canvas.drawText(timer.toString(), screenWidth / 2, wallSize + timerHeight, time);
    }

    public void update(){}
}
