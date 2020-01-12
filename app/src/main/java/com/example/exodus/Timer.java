package com.example.exodus;

public class Timer{
    private long startTime, stopTime;
    private boolean running;
    String time = "";

    public Timer() {
        startTime = System.currentTimeMillis();
        running = true;
    }

    public void start() {
        startTime = System.currentTimeMillis();
        running = true;
    }

    public void stop() {
        stopTime = System.currentTimeMillis();
        running = false;
    }

    public long diff() {
        if (running)
            return System.currentTimeMillis()-startTime;
        else
            return stopTime-startTime;
    }

    public String toString() {
        long diff = diff();
        long secs = (diff/1000)%60;
        long mins = (diff/(1000*60))%60;

        time = String.format("%02d:%02d", mins, secs);

        return time;
    }
}
