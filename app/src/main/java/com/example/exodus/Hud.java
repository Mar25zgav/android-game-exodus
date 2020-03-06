package com.example.exodus;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
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

    private Drawable life, emptyLife;
    private int lifeWidth = 60;
    private int top, right;
    private int left = (int)(screenWidth*0.25);
    private ArrayList<Rect> livesPos = new ArrayList<Rect>();
    private float timerHeight, timerWidth;

    public Hud(Context context){
        // Timer
        time = new Paint();
        timeColor = ContextCompat.getColor(context, R.color.time);
        time.setColor(timeColor);
        time.setTextSize(75);
        timerSize = new Paint.FontMetrics();
        timer = new Timer();
        timer.start();

        //Set vars
        wallSize = (int)(screenWidth*0.03);
        timerSize = time.getFontMetrics();
        timerHeight = Math.abs(timerSize.ascent);
        timerWidth = time.getFontMetrics(timerSize);
        top = wallSize+(int)timerHeight/2;

        // Healthbar
        life = context.getResources().getDrawable(R.drawable.life_filled);
        emptyLife = context.getResources().getDrawable(R.drawable.life_empty);

        for(int i=0; i<5; i++){
            right = left+lifeWidth;
            livesPos.add(new Rect(left,top,right,top+lifeWidth));
            left += lifeWidth;
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
        canvas.drawText(timer.toString(), screenWidth/2-timerWidth, (float)(wallSize*1.37)+Math.abs(timerHeight), time);
    }

    public void update(){};
}
