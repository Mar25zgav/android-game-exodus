package com.example.exodus.gamepanel;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;

import androidx.core.content.ContextCompat;

import com.example.exodus.Timer;
import com.example.exodus.gameobject.Arena;
import com.example.exodus.menupanel.GameActivity;
import com.example.exodus.gameobject.Player;
import com.example.exodus.R;

import java.util.ArrayList;

public class Hud{
    private Paint time;
    private Paint.FontMetrics timerSize;
    private Typeface font;
    private Drawable life, emptyLife;
    private Paint paint;
    private RectF rectF;
    private Timer timer;

    private ArrayList<Rect> livesPos = new ArrayList<>();
    private int screenWidth = GameActivity.getScreenWidth();
    private int wallSize;
    private int timeColor;
    private int left, top, right, bottom;
    private float topF, leftF, bottomF, rightF;
    private int borderRadius = 10;
    private int inventoryPadding = 4;
    private static float timerHeight, timerWidth;

    public Hud(Context context, Timer timer){
        // Timer
        this.timer = timer;
        font = Typeface.createFromAsset(context.getAssets(),"fonts/titan_one.ttf");
        timeColor = ContextCompat.getColor(context, R.color.time);
        time = new Paint();
        time.setColor(timeColor);
        timeColor = context.getResources().getDimensionPixelSize(R.dimen.timer);
        time.setTextSize(timeColor);
        time.setTypeface(font);
        time.setTextAlign(Paint.Align.CENTER);
        time.setAntiAlias(true);
        time.setSubpixelText(true);
        timerSize = new Paint.FontMetrics();

        //Size for healthbar  and timer
        wallSize = Arena.getWallSize();
        timerSize = time.getFontMetrics();
        timerHeight = Math.abs(timerSize.ascent);
        timerWidth = time.getFontMetrics(timerSize);
        top = (int)(wallSize * 1.5);
        bottom = top * 2;
        right = screenWidth / 2 - (int)timerWidth * 2;
        left = right - (bottom - top);

        // Healthbar
        life = context.getResources().getDrawable(R.drawable.life_filled);
        emptyLife = context.getResources().getDrawable(R.drawable.life_empty);

        for(int i = 0; i < 5; i++){
            livesPos.add(new Rect(left,top,right,bottom));
            right -= (bottom - top);
            left -= (bottom - top);
        }

        // Set rectF size with border for inventory
        topF = (float)(wallSize * 1.45);
        leftF = screenWidth / 2 + timerWidth * 2;
        bottomF = (float)(wallSize + timerHeight * 1.1) + inventoryPadding;
        rightF = leftF + (bottomF - topF) + inventoryPadding;

        rectF = new RectF(leftF, topF, rightF, bottomF);

        // RectF stroke
        paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.WHITE);
        paint.setStrokeWidth(4);
    }

    public void draw(Canvas canvas){
        // Healthbar
        int i=0;
        while(i < 5) {
            if(i < Player.getHealth()) {
                life.setBounds(livesPos.get(i));
                life.draw(canvas);
            } else {
                emptyLife.setBounds(livesPos.get(i));
                emptyLife.draw(canvas);
            }
            i++;
        }

        // Timer
        canvas.drawText(timer.toString(), screenWidth / 2, wallSize + timerHeight, time);

        // Inventory rect with borders for weapon
        canvas.drawRoundRect(rectF, borderRadius, borderRadius, paint);
    }

    static float getTimerWidth() { return timerWidth; }

    static float getTimerHeight() { return timerHeight; }
}
