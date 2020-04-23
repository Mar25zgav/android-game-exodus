package com.example.exodus;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.core.content.ContextCompat;

import com.example.exodus.gameobject.Arena;

public class BackgroundGame extends SurfaceView implements SurfaceHolder.Callback{
    private Arena arena;
    private GameLoop gameLoop;

    public BackgroundGame(Context context, AttributeSet attrs) {
        super(context, attrs);

        //Get surface holder and add callback
        SurfaceHolder surfaceHolder = getHolder();
        surfaceHolder.addCallback(this);

        //gameLoop = new GameLoop(this, surfaceHolder);

        arena = new Arena(getContext());
    }

    public void update() {}

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);

        canvas.drawColor(ContextCompat.getColor(getContext(), R.color.background));

        arena.draw(canvas);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        Log.d("BackgroundGame.java", "surfaceCreated()");
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        Log.d("BackgroundGame.java", "surfaceChanged()");
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        Log.d("BackgroundGame.java", "surfaceDestroyed()");
    }
}
