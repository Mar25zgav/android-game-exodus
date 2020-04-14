package com.example.exodus.gameobject;

import android.content.Context;

import androidx.core.content.ContextCompat;

import com.example.exodus.GameLoop;
import com.example.exodus.R;

public class Spell extends Circle {
    private Gun gun;
    private Player spellcaster;

    private static float SPEED = 800; // per seconds
    private static float MAX_SPEED = SPEED / GameLoop.MAX_UPS;
    private static float radius = 10;
    private static int damage = 1;

    public Spell(Context context, Player spellcaster){
        super(
            context,
            ContextCompat.getColor(context, R.color.player),
            spellcaster.getPositionX(),
            spellcaster.getPositionY(),
            radius
        );
        this.spellcaster = spellcaster;

        velocity.x = spellcaster.getDirectionX() * MAX_SPEED;
        velocity.y = spellcaster.getDirectionY() * MAX_SPEED;
    }

    @Override
    public void update() {
        position.add(velocity);
    }

    public static void reset() {
        MAX_SPEED = SPEED / GameLoop.MAX_UPS;;
        radius = 10;
        damage = 1;
    }

    public static int getDamage() { return damage; }

    public static void setSpeed(float speed) { MAX_SPEED = speed; }

    public static void setRadius(float r) { radius = r; }

    public static void setDamage(int d) { damage = d; }
}
