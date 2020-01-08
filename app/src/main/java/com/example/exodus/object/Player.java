package com.example.exodus.object;

import android.content.Context;
import androidx.core.content.ContextCompat;

import com.example.exodus.GameLoop;
import com.example.exodus.Joystick;
import com.example.exodus.R;

/* Player is the main character of the game, which the user can control with a touch joystick.
   The player class is an extension of a Circle, which is an extension of a GameObject. */
public class Player extends Circle{

    public static final double SPEED_PIXELS_PER_SECOND = 400.0;
    public static double MAX_SPEED = SPEED_PIXELS_PER_SECOND / GameLoop.MAX_UPS;
    private final Joystick joystick;
    private double tempPosX;
    private double tempPosY;
    public static int health;

    public Player(Context context, Joystick joystick, double positionX, double positionY, double radius){
        super(context, ContextCompat.getColor(context, R.color.player), positionX, positionY, radius);
        this.joystick = joystick;
        this.health = 500;
    }

    // Update velocity based on actuator of joystick
    public void update() {

        tempPosX = positionX;
        tempPosY = positionY;

        // Update velocity based on actuator of joystick
        velocityX = joystick.getActuatorX() * MAX_SPEED;
        velocityY = joystick.getActuatorY() * MAX_SPEED;

        // Save current position, if collides set position to previous
        positionX += velocityX;
        if(Arena.collision(this)){
            positionX = tempPosX;
        }
        positionY += velocityY;
        if(Arena.collision(this)){
            positionY = tempPosY;
        }

        // Update direction
        if (velocityX != 0 || velocityY != 0) {
            // Normalize velocity to get direction (unit vector of velocity)
            double distance = Utils.getDistanceBetweenPoints(0, 0, velocityX, velocityY);
            directionX = velocityX/distance;
            directionY = velocityY/distance;
        }
    }
}
