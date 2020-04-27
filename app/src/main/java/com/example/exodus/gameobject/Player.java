package com.example.exodus.gameobject;

import android.content.Context;
import androidx.core.content.ContextCompat;
import com.example.exodus.GameLoop;
import com.example.exodus.R;
import com.example.exodus.Utils;
import com.example.exodus.gamepanel.Joystick;
import com.example.exodus.menupanel.GameActivity;

/* Player is the main character of the game, which the user can control with a touch joystick.
   The player class is an extension of a Circle, which is an extension of a GameObject. */
public class Player extends Circle {
    private Gun gun;
    private Joystick joystick;
    private int kills;
    private static int health;
    private boolean hasGun = false;
    public static final float SPEED_PIXELS_PER_SECOND = 250;
    private static float MAX_SPEED = SPEED_PIXELS_PER_SECOND / GameLoop.MAX_UPS;
    private static float radius = GameActivity.getScreenHeight() * 0.035f;

    public Player(Context context, Joystick joystick) {
        super(context,
                ContextCompat.getColor(context, R.color.player),
                GameActivity.getScreenWidth() * 0.5f,
                GameActivity.getScreenHeight() * 0.5f,
                GameActivity.getScreenHeight() * 0.035f);
        this.joystick = joystick;
        health = 5;
        kills = 0;
    }

    public void update() {
        // Update velocity based on actuator of joystick
        velocity.x = (float)joystick.getActuatorX() * MAX_SPEED;
        velocity.y = (float)joystick.getActuatorY() * MAX_SPEED;

        // Save current position, if collides set position to previous
        position.x += velocity.x;
        if(Arena.collision(this)){
            position.x -= velocity.x;
        }
        position.y += velocity.y;
        if(Arena.collision(this)){
            position.y -= velocity.y;
        }

        // Update direction
        if(velocity.x != 0 || velocity.y != 0) {
            // Normalize velocity to get direction (unit vector of velocity)
            float distance = Utils.getDistanceBetweenPoints(0, 0, velocity.x, velocity.y);
            direction.x = velocity.x / distance;
            direction.y = velocity.y / distance;
        }
    }

    public void setGun(Gun gun) {
        this.gun = gun;
        hasGun = true;

    }

    public Gun getGun() { return gun; }

    public boolean hasGun() { return hasGun; }

    public void takeAwayGun() { hasGun = false; }

    public int getKills() { return kills; }

    public void addKill() { this.kills++; }

    public void addHealth() {
        health++;
    }

    public void subHealth() {
        health--;
    }

    public static int getHealth() {
        return health;
    }

    public static float getStaticRadius() {
        return radius;
    }
}
