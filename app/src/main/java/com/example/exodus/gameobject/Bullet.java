package com.example.exodus.gameobject;

import android.content.Context;
import androidx.core.content.ContextCompat;
import com.example.exodus.GameLoop;
import com.example.exodus.R;

public class Bullet extends Circle {
    private static float SPEED = 600; // per seconds
    private static float MAX_SPEED = SPEED / GameLoop.MAX_UPS;
    private static float radius = Player.getStaticRadius() / 3;
    private static double bps = 2; // bullets per second
    private static double damage = 1;
    private static long lastShot;

    public Bullet(Context context, Player spellcaster) {
        super(
            context,
            ContextCompat.getColor(context, R.color.player),
            spellcaster.getPositionX(),
            spellcaster.getPositionY(),
            radius
        );

        velocity.x = spellcaster.getDirectionX() * MAX_SPEED;
        velocity.y = spellcaster.getDirectionY() * MAX_SPEED;
    }

    @Override
    public void update() {
        position.add(velocity);
    }

    public static boolean readyToShoot() {
        if (System.currentTimeMillis() - (1000 / bps) >= lastShot) {
            lastShot = System.currentTimeMillis();
            return true;
        }
        return false;
    }

    public static void reset() {
        MAX_SPEED = SPEED / GameLoop.MAX_UPS;
        radius = Player.getStaticRadius() / 3;
        damage = 1;
        bps = 2;
    }

    public static void setSpeed(float speed) {
        MAX_SPEED = speed / GameLoop.MAX_UPS;
    }

    public static double getDamage() {
        return damage;
    }

    public static float getStaticRadius() {
        return radius;
    }

    public static void setRadius(float r) { radius = r; }

    public static void setDamage(double d) {
        damage = d;
    }

    public static void setBps(double b) {
        bps = b;
    }
}
