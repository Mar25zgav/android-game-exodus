package com.example.exodus;

import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Timer {
    private static final int DURATION_INFINITY = -1;
    private volatile boolean isRunning = false;
    private long interval;
    private long elapsedTime;
    private long duration;
    private Future<?> future = null;
    private ScheduledExecutorService execService = Executors
            .newSingleThreadScheduledExecutor();

    // Default constructor which sets the interval to 1000 ms (1s) and the duration to DURATION_INFINITY
    public Timer() {
        this(1000, -1);
    }

    private Timer(long interval, long duration) {
        this.interval = interval;
        this.duration = duration;
        this.elapsedTime = 0;
    }

    // Starts the timer. If the timer was already running, this call is ignored.
    public void start() {
        if (isRunning)
            return;

        isRunning = true;
        future = execService.scheduleWithFixedDelay(new Runnable() {
            @Override
            public void run() {
                onTick();
                elapsedTime += interval;
                if (duration > 0) {
                    if (elapsedTime >= duration) {
                        onFinish();
                        future.cancel(false);
                    }
                }
            }
        }, 1000, this.interval, TimeUnit.MILLISECONDS);
    }

    // Paused the timer. If the timer is not running, this call is ignored.
    void pause() {
        if (!isRunning) return;
        future.cancel(false);
        isRunning = false;
    }

    // Resumes the timer if it was paused, else starts the timer.
    void resume() {
        this.start();
    }

    // This method is called periodically with the interval set as the delay between subsequent calls.
    private void onTick() {
    }

    // This method is called once the timer has run for the specified duration. If the duration was set as infinity, then this method is never called.
    private void onFinish() {
    }

    // Stops the timer. If the timer is not running, then this call does nothing.
    public void cancel() {
        pause();
        this.elapsedTime = 0;
    }

    // Return the elapsed time (in millis) since the start of the timer.
    public long getElapsedTime() {
        return this.elapsedTime;
    }

    // Return the time remaining (in millis) for the timer to stop. If the duration was set to DURATION_INFINITY, then -1 is returned.
    public long getRemainingTime() {
        if (this.duration < 0) {
            return DURATION_INFINITY;
        } else {
            return duration - elapsedTime;
        }
    }

    // Return true if the timer is currently running, and false otherwise.
    public boolean isRunning() {
        return isRunning;
    }

    // Calculate time in seconds and minutes the return formated string.
    public String toString() {
        long diff = getElapsedTime();
        long secs = (diff / 1000) % 60;
        long mins = (diff / (1000 * 60)) % 60;

        return String.format("%02d:%02d", mins, secs);
    }
}
