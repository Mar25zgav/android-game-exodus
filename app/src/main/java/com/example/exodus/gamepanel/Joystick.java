package com.example.exodus.gamepanel;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.example.exodus.gameobject.Arena;
import com.example.exodus.menupanel.GameActivity;

public class Joystick {
    private Paint outerCirclePaint;
    private Paint innerCirclePaint;
    private float outerCircleRadius;
    private float innerCircleRadius;
    private float outerCircleCenterPositionX;
    private float outerCircleCenterPositionY;
    private float innerCircleCenterPositionX;
    private float innerCircleCenterPositionY;
    private float joystickCenterToTouchDistance;
    private float actuatorX;
    private float actuatorY;
    private boolean isPressed;

    public Joystick() {
        //Outer and inner circle make up the joystick
        outerCircleCenterPositionX = Arena.getWallSize() * 6.2f;
        outerCircleCenterPositionY = GameActivity.getScreenHeight() - Arena.getWallSize() * 6.2f;
        innerCircleCenterPositionX = Arena.getWallSize() * 6.2f;
        innerCircleCenterPositionY = GameActivity.getScreenHeight() - Arena.getWallSize() * 6.2f;

        //Radius of each circle
        this.outerCircleRadius = Arena.getWallSize() * 3.2f;
        this.innerCircleRadius = Arena.getWallSize() * 2.2f;

        //Paint of circles
        outerCirclePaint = new Paint();
        outerCirclePaint.setColor(Color.GRAY);
        outerCirclePaint.setStyle(Paint.Style.FILL_AND_STROKE);

        innerCirclePaint = new Paint();
        innerCirclePaint.setColor(Color.WHITE);
        innerCirclePaint.setStyle(Paint.Style.FILL_AND_STROKE);
    }

    public void draw(Canvas canvas) {
        //Draw outer circle
        canvas.drawCircle(outerCircleCenterPositionX, outerCircleCenterPositionY, outerCircleRadius, outerCirclePaint);

        //Draw inner circle
        canvas.drawCircle(innerCircleCenterPositionX, innerCircleCenterPositionY, innerCircleRadius, innerCirclePaint);
    }

    public void update() {
        updateInnerCirclePosition();
    }

    private void updateInnerCirclePosition() {
        innerCircleCenterPositionX = outerCircleCenterPositionX + actuatorX * outerCircleRadius;
        innerCircleCenterPositionY = outerCircleCenterPositionY + actuatorY * outerCircleRadius;
    }

    public boolean isPressed(float touchPositionX, float touchPositionY) {
        joystickCenterToTouchDistance = (float) Math.sqrt(
                Math.pow(outerCircleCenterPositionX - touchPositionX, 2) +
                        Math.pow(outerCircleCenterPositionY - touchPositionY, 2)
        );
        return joystickCenterToTouchDistance < outerCircleRadius;
    }

    public void setIsPressed(boolean isPressed) {
        this.isPressed = isPressed;
    }

    public boolean getIsPressed() {
        return isPressed;
    }

    public void setActuator(float touchPositionX, float touchPositionY) {
        float deltaX = touchPositionX - outerCircleCenterPositionX;
        float deltaY = touchPositionY - outerCircleCenterPositionY;
        float deltaDistance = (float) Math.sqrt(Math.pow(deltaX, 2) + Math.pow(deltaY, 2));

        if (deltaDistance < outerCircleRadius) {
            actuatorX = deltaX / outerCircleRadius;
            actuatorY = deltaY / outerCircleRadius;
        } else {
            actuatorX = deltaX / deltaDistance;
            actuatorY = deltaY / deltaDistance;
        }
    }

    public void resetActuator() {
        actuatorX = 0;
        actuatorY = 0;
    }

    public double getActuatorX() {
        return actuatorX;
    }

    public double getActuatorY() {
        return actuatorY;
    }
}
