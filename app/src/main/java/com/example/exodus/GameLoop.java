package com.example.exodus;

import android.graphics.Canvas;
import android.util.Log;
import android.view.SurfaceHolder;

public class GameLoop extends Thread {
    private final SurfaceHolder surfaceHolder;
    private Thread thread;
    private Game game;

    public static final float MAX_UPS = 60;
    private static final double UPS_PERIOD = 1E+3 / MAX_UPS;
    public static boolean isRunning = false;
    private double avarageUPS;
    private double avarageFPS;

    public GameLoop(Game game, SurfaceHolder surfaceHolder) {
        this.game = game;
        this.surfaceHolder = surfaceHolder;
    }

    public double getAvarageUPS() {
        return avarageUPS;
    }

    public double getAvarageFPS() {
        return avarageFPS;
    }

    void startLoop() {
        Log.d("GameLoop.java", "startLoop()");
        isRunning = true;
        thread = new Thread(this);
        thread.start();
    }

    @Override
    public void run() {
        Log.d("GameLoop.java", "run()");
        super.run();

        //Declare time and cycle count variables
        int updateCount = 0;
        int frameCount = 0;
        long startTime;
        long elapsedTime;
        long sleepTime;

        //Game loop
        Canvas canvas = null;
        startTime = System.currentTimeMillis();
        while (isRunning) {
            //Poskusi posodobiti in render game
            try {
                canvas = surfaceHolder.lockCanvas();
                synchronized (surfaceHolder) {
                    game.update();
                    updateCount++;
                    game.draw(canvas);
                }
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } finally {
                if (canvas != null) {
                    try {
                        surfaceHolder.unlockCanvasAndPost(canvas);
                        frameCount++;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            //Pause game loop to not exceed target UPS
            elapsedTime = System.currentTimeMillis() - startTime;
            sleepTime = (long) (updateCount * UPS_PERIOD - elapsedTime);
            if (sleepTime > 0) {
                try {
                    sleep(sleepTime);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            //Skip frames to keep up with target UPS
            while (sleepTime < 0 && updateCount < MAX_UPS - 1) {
                game.update();
                updateCount++;
                elapsedTime = System.currentTimeMillis() - startTime;
                sleepTime = (long) (updateCount * UPS_PERIOD - elapsedTime);
            }

            //Calculate avarage UPS in FPS
            elapsedTime = System.currentTimeMillis() - startTime;
            if (elapsedTime >= 1000) {
                avarageUPS = updateCount / (1E-3 * elapsedTime);
                avarageFPS = frameCount / (1E-3 * elapsedTime);
                updateCount = 0;
                frameCount = 0;
                startTime = System.currentTimeMillis();
            }
        }
    }

    void stopLoop() {
        Log.d("GameLoop.java", "stopLoop()");
        isRunning = false;
        // Wait for thread to join
        try {
            join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
