package com.example.exodus.gamepanel;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;

import androidx.core.content.ContextCompat;

import com.example.exodus.R;
import com.example.exodus.menupanel.GameActivity;

// GameOver is a panel which draws the text Game Over to the screen.
public class GameOver {
    private Typeface font;
    private Paint paint;
    private String text;
    private float x, y;
    private int textColor;

    public GameOver(Context context) {
        text = "Game Over";
        x = (float) (GameActivity.getScreenWidth() / 2);
        y = (float) (GameActivity.getScreenHeight() / 2);
        font = Typeface.createFromAsset(context.getAssets(), "fonts/titan_one.ttf");
        textColor = ContextCompat.getColor(context, R.color.time);
        paint = new Paint();
        paint.setColor(textColor);
        textColor = context.getResources().getDimensionPixelSize(R.dimen.timer);
        paint.setTextSize(textColor);
        paint.setTypeface(font);
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setAntiAlias(true);
        paint.setSubpixelText(true);
    }

    public void draw(Canvas canvas) {
        canvas.drawText(text, x, y, paint);
    }
}
