package com.example.exodus;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;

public abstract class Rect extends GameObject {

    private Paint paint;
    private android.graphics.Rect rectangle;
    private int color;
    private double positionX;
    private double positionY;
    private int sideLength;
    private int sideWidth;

    public Rect(Context context, int color, double positionX, double positionY, int sideLength, int sideWidth){
        super(positionX,positionY);

        this.color = color;
        this.positionX = positionX;
        this.positionY = positionY;
        this.sideLength = sideLength;
        this.sideWidth = sideWidth;

        // Set color of rect
        paint = new Paint();
        paint.setColor(color);
    }

    public void draw(Canvas canvas) {
        rectangle = new android.graphics.Rect((int)positionX, (int)positionY, sideLength, sideWidth);
        canvas.drawRect(rectangle, paint);
    }
}
