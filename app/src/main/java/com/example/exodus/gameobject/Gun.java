package com.example.exodus.gameobject;

import android.graphics.drawable.Drawable;
import com.example.exodus.GameLoop;

public class Gun {
    private String name;

    private float speed; // Pixels per second
    private float max_speed;
    private int damage;
    private float radius;
    private int force;

    public Gun(String name, float speed, int damage, float radius, int force) {
        this.name = name;
        this.speed = speed;
        this.damage = damage;
        this.radius = radius;
        this.force = force;

        max_speed = speed / GameLoop.MAX_UPS;
    }

    public String getName() { return name; }

    public float getSpeed() { return max_speed; }

    public int getDamage() { return damage; }

    public float getRadius() { return radius; }

    public int getForce() { return force; }
}
