package com.example.exodus;

public class Utils {

    // getDistanceBetweenPoints returns the distance between 2d points p1 and p2
    public static float getDistanceBetweenPoints(float p1x, float p1y, float p2x, float p2y) {
        return (float)Math.sqrt(Math.pow(p1x - p2x, 2) + Math.pow(p1y - p2y, 2));
    }
}
